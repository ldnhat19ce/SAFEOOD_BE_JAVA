package tech.dut.safefood.service.Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.BillDetailResponseDto;
import tech.dut.safefood.dto.response.BillItemResponseDto;
import tech.dut.safefood.dto.response.BillUserResponseDto;
import tech.dut.safefood.enums.BillEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Bill;
import tech.dut.safefood.model.Payment;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.repository.*;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import java.util.List;
import java.util.Optional;

@Service
public class ShopBillService {
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

    @Transactional(readOnly = true)
    public List<BillUserResponseDto> getAllListBill(BillEnum.Status status) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Long shopId = shop.getId();
        return billRepository.getAllByShopId(shopId, status.toString());
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
        if(payment!=null){
            billDetailResponseDto.setPaymentType(payment.getName());
        }
        billDetailResponseDto.setUserId(bill.getUser().getId());
        billDetailResponseDto.setUserFirstName(bill.getUser().getUserInformation().getFirstName());
        billDetailResponseDto.setUserLastName(bill.getUser().getUserInformation().getLastName());
        billDetailResponseDto.setCreatedAt(bill.getCreatedAt());
        billDetailResponseDto.setVoucherId(bill.getVoucher().getId());
        billDetailResponseDto.setVoucherName(bill.getVoucher().getName());
        billDetailResponseDto.setVoucherCreatedAt(bill.getVoucher().getCreatedAt());
        billDetailResponseDto.setVoucherEndedAt(bill.getVoucher().getEndedAt());
        billDetailResponseDto.setUserType(bill.getVoucher().getUserType());
        billDetailResponseDto.setVoucherType(bill.getVoucher().getVoucherType());
        billDetailResponseDto.setValueNeed(bill.getVoucher().getValueNeed());
        billDetailResponseDto.setVoucherDiscountImage(bill.getVoucher().getImage());
        billDetailResponseDto.setValueDiscount(bill.getVoucher().getValueDiscount());
        return billDetailResponseDto;
    }

    @Transactional(readOnly = true)
    public BillDetailResponseDto getBillByCode(String code) {
        Optional<Bill> optionalBill = billRepository.findByCode(code);
        if (optionalBill.isEmpty()) {
            throw new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS);
        }
        Bill bill = optionalBill.get();
        Optional<Payment> optionalPayment = paymentRepository.findByBillId(bill.getId());
        Payment payment = null;
        if(optionalPayment.isPresent()){
            payment = optionalPayment.get();
        }
        List<BillItemResponseDto> billItemResponseDtos = billItemRepository.getAllBillItemByBillId(bill.getId());
        BillDetailResponseDto billDetailResponseDto = new BillDetailResponseDto();
        billDetailResponseDto.setBillId(bill.getId());
        billDetailResponseDto.setBillItemResponseDtos(billItemResponseDtos);
        billDetailResponseDto.setTotalOrigin(bill.getTotalOrigin());
        billDetailResponseDto.setTotalPayment(bill.getTotalPayment());
        billDetailResponseDto.setTotalDiscount(bill.getTotalVoucher());
        billDetailResponseDto.setCode(bill.getCode());
        billDetailResponseDto.setExpiredDate(bill.getExpiredCode());
        billDetailResponseDto.setStatus(bill.getStatus());
        if(payment!=null){
            billDetailResponseDto.setPaymentType(payment.getName());
        }
        billDetailResponseDto.setUserId(bill.getUser().getId());
        billDetailResponseDto.setUserFirstName(bill.getUser().getUserInformation().getFirstName());
        billDetailResponseDto.setUserLastName(bill.getUser().getUserInformation().getLastName());
        billDetailResponseDto.setCreatedAt(bill.getCreatedAt());
        billDetailResponseDto.setVoucherId(bill.getVoucher().getId());
        billDetailResponseDto.setVoucherName(bill.getVoucher().getName());
        billDetailResponseDto.setVoucherCreatedAt(bill.getVoucher().getCreatedAt());
        billDetailResponseDto.setVoucherEndedAt(bill.getVoucher().getEndedAt());
        billDetailResponseDto.setUserType(bill.getVoucher().getUserType());
        billDetailResponseDto.setVoucherType(bill.getVoucher().getVoucherType());
        billDetailResponseDto.setVoucherDiscountImage(bill.getVoucher().getImage());
        billDetailResponseDto.setValueNeed(bill.getVoucher().getValueNeed());
        billDetailResponseDto.setValueDiscount(bill.getVoucher().getValueDiscount());
        return billDetailResponseDto;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void doneBill(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS);
        }

        optionalBill.get().setStatus(Constants.BILL_DONE);

    }
}
