package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.ProductDetailRequestDto;
import tech.dut.safefood.dto.request.VoucherProductRequestDto;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.dto.response.VoucherUserResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Product;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.repository.ProductRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.repository.VoucherRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserVoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private BillRepository billRepository;

    @Transactional(readOnly = true)
    public List<VoucherUserResponseDto> getAllVoucherForBill(VoucherProductRequestDto voucherProductRequestDto) {
        Map<Long, ProductDetailRequestDto> maps = voucherProductRequestDto.getOrderDetails().stream().collect(Collectors.toMap(ProductDetailRequestDto::getProductId, Function.identity()));
        List<Long> ids = voucherProductRequestDto.getOrderDetails().stream().map(ProductDetailRequestDto::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.getProductInIds(ids);
        BigDecimal totalOrigin = BigDecimal.valueOf(0L);
        for (Product product : products) {
            if (!product.getShop().getId().equals(voucherProductRequestDto.getShopId())) {
                throw new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT);
            }
            ProductDetailRequestDto productDetailRequestDto = maps.get(product.getId());
            BigDecimal tmp = product.getPrice().multiply(BigDecimal.valueOf(productDetailRequestDto.getAmount()));
            totalOrigin = totalOrigin.add(tmp);
        }

        return voucherRepository.getAllVoucherUser(voucherProductRequestDto.getShopId(), totalOrigin, Constants.ROLE_ADMIN);
    }

    @Transactional(readOnly = true)
    public VoucherResponseDto getDetailVoucher(Long voucherId) throws SafeFoodException {
        if (!voucherRepository.existsById(voucherId)) {
            throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS);
        }
        VoucherResponseDto voucherResponseDto = voucherRepository.getDetailVoucherById(voucherId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS));
        if (!voucherResponseDto.getDeleteFlag()) {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
        } else {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
        }
        if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
        }
        if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
        }
        return voucherResponseDto;
    }

    @Transactional(readOnly = true)
    public PageInfo<VoucherResponseDto> getShopVoucher(Integer page, Integer limit, Long shopId, String query) throws SafeFoodException {
        if (!shopRepository.existsById(shopId)) {
            throw new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND);
        }

        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<VoucherResponseDto> dtoPage = voucherRepository.getVoucherByShopId(pageable, shopId, query);

        List<VoucherResponseDto> voucherResponseDtos = dtoPage.getContent();

        if(voucherResponseDtos.size() >0){
            for(VoucherResponseDto voucherResponseDto : voucherResponseDtos){
                if (!voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
                } else {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
                }
                if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
                if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
            }
        }

        return AppUtils.pagingResponseCustom(dtoPage, voucherResponseDtos);
    }

    @Transactional(readOnly = true)
    public PageInfo<VoucherUserResponseDto> getAllVoucher(Integer page, Integer limit, String userType, String query) throws SafeFoodException {
        if (!Constants.ROLE_SHOP.equals(userType) && !Constants.ROLE_ADMIN.equals(userType) && userType != null) {
            throw new SafeFoodException(SafeFoodException.ERROR_USERTYPE_NOT_CORRECT);
        }
        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<VoucherUserResponseDto> dtoPage  =  voucherRepository.getAllVoucher(pageable, userType, query);
        List<VoucherUserResponseDto> voucherResponseDtos = dtoPage.getContent();
        if(voucherResponseDtos.size() >0){
            for(VoucherUserResponseDto voucherResponseDto : voucherResponseDtos){
                if (!voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
                } else {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
                }
                if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
                if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
            }
        }
        return AppUtils.pagingResponseCustom(dtoPage, voucherResponseDtos);
    }

    @Transactional(readOnly = true)
    public PageInfo<VoucherResponseDto> getTopVoucherAllShop(Integer page, Integer limit, String query) {

        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<VoucherResponseDto> dtoPage = billRepository.getTopVoucherShop(pageable, query);
        List<VoucherResponseDto> voucherResponseDtos = dtoPage.getContent();

        if(voucherResponseDtos.size() >0){
            for(VoucherResponseDto voucherResponseDto : voucherResponseDtos){
                if (!voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
                } else {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
                }
                if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
                if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
            }
        }

        List<VoucherResponseDto> tmpDto = new ArrayList<>(voucherResponseDtos);

        if (tmpDto.size() >= 2) {

            Comparator<VoucherResponseDto> voucherResponseDtoComparator = new Comparator<VoucherResponseDto>() {
                @Override
                public int compare(VoucherResponseDto o1, VoucherResponseDto o2) {
                    return o2.getCountUserVoucher().compareTo(o1.getCountUserVoucher());
                }
            };
            Collections.sort(tmpDto, voucherResponseDtoComparator);
        }

        return AppUtils.pagingResponseCustom(dtoPage, tmpDto);
    }

    @Transactional(readOnly = true)
    public PageInfo<VoucherResponseDto> getTopVoucherAdmin(Integer page, Integer limit, String query) {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<VoucherResponseDto> dtoPage = billRepository.getTopVoucherAdmin(pageable, query);
        List<VoucherResponseDto> voucherResponseDtos = dtoPage.getContent();

        if(voucherResponseDtos.size() >0){
            for(VoucherResponseDto voucherResponseDto : voucherResponseDtos){
                if (!voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
                } else {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
                }
                if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
                if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                    voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
                }
            }
        }

        List<VoucherResponseDto> tmpDto = new ArrayList<>(voucherResponseDtos);

        if (tmpDto.size() > 2) {
            Collections.sort(tmpDto, new Comparator<VoucherResponseDto>() {
                @Override
                public int compare(VoucherResponseDto o1, VoucherResponseDto o2) {
                    return o2.getCountUserVoucher().compareTo(o1.getCountUserVoucher());
                }
            });
        }

        return AppUtils.pagingResponseCustom(dtoPage, tmpDto);
    }
}
