package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.BillProductRequestDto;
import tech.dut.safefood.dto.request.ProductDetailRequestDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.BillEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.*;
import tech.dut.safefood.repository.*;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillItemRepository billItemRepository;


    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private VoucherUserRepository voucherUserRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public BillResponseDto createBill(BillProductRequestDto billProductRequestDto) {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Optional<Bill> optionalBill = billRepository.findByUserIdAndStatus(user.getId(), Constants.BILL_CREATED);
        if (optionalBill.isPresent()) {
            Bill tmp = optionalBill.get();
            tmp.setStatus(Constants.BILL_FAIL);
        }

        Map<Long, ProductDetailRequestDto> maps = billProductRequestDto.getProductDetailRequestDtos().stream().collect(Collectors.toMap(ProductDetailRequestDto::getProductId, Function.identity()));
        List<Long> ids = billProductRequestDto.getProductDetailRequestDtos().stream().map(ProductDetailRequestDto::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.getProductInIds(ids);
        BigDecimal totalOrigin = BigDecimal.valueOf(0L);
        List<BillItem> billItems = new ArrayList<>();

        for (Product product : products) {
            if (!product.getShop().getId().equals(billProductRequestDto.getShopId())) {
                throw new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT);
            }
            ProductDetailRequestDto productDetailRequestDto = maps.get(product.getId());
            BigDecimal tmp = product.getPrice().multiply(new BigDecimal(productDetailRequestDto.getAmount()));
            totalOrigin = totalOrigin.add(tmp);
        }

        for (Product item : products) {
            BillItem billItem = new BillItem();
            billItem.setAmount(maps.get(item.getId()).getAmount());
            billItem.setProduct(item);
            billItem.setUser(user);
            billItem.setShop(item.getShop());

            billItems.add(billItem);
        }
        Optional<Voucher> optionalVoucher = voucherRepository.findByIdAndDeleteFlagIsFalseAndShopId(billProductRequestDto.getVoucherId(), billProductRequestDto.getShopId());
        Optional<Voucher> voucherOptionalAdmin = voucherRepository.findByIdAndUserType(billProductRequestDto.getVoucherId(), Constants.ROLE_ADMIN);
        Voucher voucher = null;

        if (optionalVoucher.isPresent()) {
            voucher = optionalVoucher.get();
        }

        if (voucherOptionalAdmin.isPresent()) {
            voucher = voucherOptionalAdmin.get();
        }

        if (voucher != null) {
            if (voucher.getEndedAt().isBefore(LocalDateTime.now())) {
                throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_EXPIRED);
            }
            if (voucher.getValueNeed().compareTo(totalOrigin) > 0) {
                throw new SafeFoodException(SafeFoodException.ERROR_TOTAL_ORIGIN_LESS_VALUE_NEED);
            }

            if (voucher.getQuantity() <= 0) {
                throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_FULL);
            }
        }

        if (voucher != null) {
            if (!voucherUserRepository.existsByUserIdAndVoucherId(user.getId(), billProductRequestDto.getVoucherId())) {
                VoucherUser voucherUser = new VoucherUser();
                voucherUser.setVoucher(voucher);
                voucherUser.setUser(user);
                voucherUser.setUsed(1L);
                voucherUserRepository.save(voucherUser);
                voucher.setQuantity(voucher.getQuantity() - 1L);
            } else {
                VoucherUser voucherUser = voucherUserRepository.findByUserIdAndVoucherId(user.getId(), billProductRequestDto.getVoucherId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_USER_NOT_EXISTS));
                if (voucherUser.getUsed().compareTo(voucher.getLimitPerUser()) == 0) {
                    throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_USER_IS_FULL);
                }
                voucherUser.setUsed(voucherUser.getUsed() + 1L);
                voucher.setQuantity(voucher.getQuantity() - 1L);
            }
        }


        BigDecimal totalVoucher = new BigDecimal(0);
        BigDecimal totalPayment = null;

        if (voucher != null) {
            if (!voucher.getVoucherType().equals(Constants.VOUCHER_TYPE_PERCENT)) {
                totalVoucher = totalOrigin.subtract(voucher.getValueDiscount());
            } else {
                BigDecimal bigDecimal = totalOrigin.multiply(voucher.getValueDiscount()).divide(new BigDecimal(100));
                totalVoucher = bigDecimal;
                if (totalVoucher.compareTo(voucher.getMaxDiscount()) > 0) {
                    totalVoucher = voucher.getMaxDiscount();
                }
            }
        }

        totalPayment = totalOrigin.subtract(totalVoucher);

        Shop shop = shopRepository.findById(billProductRequestDto.getShopId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setShop(shop);
        bill.setTotalOrigin(totalOrigin);
        bill.setTotalPayment(totalPayment);
        bill.setTotalVoucher(totalVoucher);
        bill.setStatus(Constants.BILL_CREATED);

        if (voucher != null) {
            bill.setVoucher(voucher);
        }


        for (BillItem billItem : billItems) {
            billItem.setBill(bill);
        }


        billRepository.save(bill);
        billItemRepository.saveAll(billItems);

        List<ProductResponseDto> productResponseDtos = productRepository.getProductResponseInIds(ids);
        BillResponseDto billResponseDto = new BillResponseDto();
        billResponseDto.setBillId(bill.getId());
        billResponseDto.setProductResponseDtos(productResponseDtos);
        billResponseDto.setTotalOrigin(bill.getTotalOrigin());
        billResponseDto.setTotalDiscount(bill.getTotalVoucher());
        billResponseDto.setTotalPayment(bill.getTotalPayment());
        return billResponseDto;
    }

    @Transactional(readOnly = true)
    public List<BillUserResponseDto> getAllListBill(BillEnum.Status status) {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Long id = user.getId();
        System.out.println(status.toString());
        return billRepository.getAllByUserIdAndStatus(id, status.toString());
    }

    @Transactional(readOnly = true)
    public BillDetailResponseDto getDetailBill(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS);
        }
        Bill bill = optionalBill.get();
        Optional<Payment> optionalPayment = paymentRepository.findByBillId(billId);
        Payment payment = null;
        if(optionalPayment.isPresent()){
            payment = optionalPayment.get();
        }
        List<BillItemResponseDto> billItemResponseDtos = billItemRepository.getAllBillItemByBillId(billId);
        BillDetailResponseDto billDetailResponseDto = new BillDetailResponseDto();
        billDetailResponseDto.setBillId(bill.getId());
        billDetailResponseDto.setBillItemResponseDtos(billItemResponseDtos);
        billDetailResponseDto.setTotalOrigin(bill.getTotalOrigin());
        billDetailResponseDto.setTotalPayment(bill.getTotalPayment());
        billDetailResponseDto.setTotalDiscount(bill.getTotalVoucher());
        billDetailResponseDto.setCode(bill.getCode());
        billDetailResponseDto.setExpiredDate(bill.getExpiredCode());
        billDetailResponseDto.setStatus(bill.getStatus());
        if(payment!=null)
        billDetailResponseDto.setPaymentType(payment.getName());
        billDetailResponseDto.setShopId(bill.getShop().getId());
        billDetailResponseDto.setShopName(bill.getShop().getName());
        billDetailResponseDto.setLogo(bill.getShop().getBanner());
        billDetailResponseDto.setAddressId(bill.getShop().getAddresses().getId());
        billDetailResponseDto.setCity(bill.getShop().getAddresses().getCity());
        billDetailResponseDto.setDistrict(bill.getShop().getAddresses().getDistrict());
        billDetailResponseDto.setTown(bill.getShop().getAddresses().getTown());
        billDetailResponseDto.setStreet(bill.getShop().getAddresses().getStreet());
        billDetailResponseDto.setX(bill.getShop().getAddresses().getX());
        billDetailResponseDto.setY(bill.getShop().getAddresses().getY());
        billDetailResponseDto.setVoucherId(bill.getVoucher().getId());
        billDetailResponseDto.setVoucherName(bill.getVoucher().getName());
        billDetailResponseDto.setVoucherCreatedAt(bill.getVoucher().getCreatedAt());
        billDetailResponseDto.setVoucherEndedAt(bill.getVoucher().getEndedAt());
        billDetailResponseDto.setUserType(bill.getVoucher().getUserType());
        billDetailResponseDto.setVoucherType(bill.getVoucher().getVoucherType());
        billDetailResponseDto.setValueNeed(bill.getVoucher().getValueNeed());
        billDetailResponseDto.setRating(bill.getRatings());
        billDetailResponseDto.setIsRating(bill.getIsRating());
        billDetailResponseDto.setVoucherDiscountImage(bill.getVoucher().getImage());
        billDetailResponseDto.setValueDiscount(bill.getVoucher().getValueDiscount());
        return billDetailResponseDto;
    }
}
