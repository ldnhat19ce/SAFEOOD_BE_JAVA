package tech.dut.safefood.service.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.ProductRequestDto;
import tech.dut.safefood.dto.request.VoucherRequestDto;
import tech.dut.safefood.dto.request.VoucherUpdateRequestDto;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.TurnoverTotalShopResponseDto;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.enums.VoucherEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Category;
import tech.dut.safefood.model.Product;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.Voucher;
import tech.dut.safefood.model.mapper.ProductMapper;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.repository.VoucherRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopManagerVoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private AppUtils appUtils;

    @Transactional(readOnly = true)
    public List<VoucherResponseDto> getAllVoucher(Integer page, Integer limit, String query, String status) throws SafeFoodException {
        if (status != null && status.equals(VoucherEnum.Status.ACTIVED.toString()) && status.equals(VoucherEnum.Status.INACTIVE.toString()) && status.equals(VoucherEnum.Status.DELETED.toString())) {
            throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_STATUS_IS_NOT_CORRECT);
        }

        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        Page<VoucherResponseDto> dtoPage = voucherRepository.getAllVouchers(pageable, shop.getId(), query);
        List<VoucherResponseDto> voucherResponseDtos = dtoPage.getContent();
        for (VoucherResponseDto voucherResponseDto : voucherResponseDtos) {
            if (!voucherResponseDto.getDeleteFlag()) {
                voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
            }
            if (voucherResponseDto.getEndedAt().isBefore(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
            }
            if (voucherResponseDto.getDeleteFlag()) {
                voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_DELETED);
            }
            if (voucherResponseDto.getCreatedAt().isAfter(LocalDateTime.now()) && !voucherResponseDto.getDeleteFlag()) {
                voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
            }

        }

        if (status != null) {
            voucherResponseDtos = voucherResponseDtos.stream().filter(v -> v.getStatus().equals(status)).collect(Collectors.toList());
            Collections.sort(voucherResponseDtos, new Comparator<VoucherResponseDto>() {
                @Override
                public int compare(VoucherResponseDto o1, VoucherResponseDto o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
        }
        return voucherResponseDtos;
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void createVoucher(VoucherRequestDto voucherRequestDto) throws SafeFoodException {
        if (!voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.PERCENT.toString()) && !voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.AMOUNT.toString())) {
            throw new SafeFoodException(SafeFoodException.ERROR_TYPE_VOUCHER_IS_NOT_CORRECT);

        }

        if(voucherRequestDto.getCreatedAt() > voucherRequestDto.getEndedAt()){
            throw new SafeFoodException(SafeFoodException.ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT);
        }

        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Shop tmp = shopRepository.findById(shop.getId()).get();
        Voucher voucher = new Voucher();
        voucher.setName(voucherRequestDto.getName());
        voucher.setCreatedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getCreatedAt()));
        voucher.setEndedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt()));
        voucher.setValueDiscount(voucherRequestDto.getValueDiscount());
        voucher.setValueNeed(voucherRequestDto.getValueNeed());
        voucher.setLimitPerUser(voucherRequestDto.getLimitPerUser());
        voucher.setMaxDiscount(voucherRequestDto.getMaxDiscount());
        voucher.setUserType(Constants.ROLE_SHOP);
        voucher.setShop(shop);
        voucher.setQuantity(voucherRequestDto.getQuantity());
        voucher.setImage(voucherRequestDto.getImage());
        voucher.setDescription(voucherRequestDto.getDescription());
        voucher.setVoucherType(voucherRequestDto.getVoucherType().toString());
        voucherRepository.save(voucher);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void deleteVoucher(Long voucherId) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        voucherRepository.findByIdAndShopId(voucherId, shop.getId()).ifPresentOrElse((item) -> {
            item.setDeleteFlag(true);
        }, () -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS));
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void updateVoucher(VoucherUpdateRequestDto voucherRequestDto) {
        if (!voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.PERCENT.toString()) && !voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.AMOUNT.toString())) {
            throw new SafeFoodException(SafeFoodException.ERROR_TYPE_VOUCHER_IS_NOT_CORRECT);

        }


        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        voucherRepository.findByIdAndShopIdAndDeleteFlagIsFalse(voucherRequestDto.getId(), shop.getId()).ifPresentOrElse((item) -> {
            LocalDateTime tmpEnd = AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt());
            if(item.getCreatedAt().isAfter(tmpEnd)){
                throw new SafeFoodException(SafeFoodException.ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT);
            }
            item.setName(voucherRequestDto.getName());
            item.setEndedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt()));
            item.setValueDiscount(voucherRequestDto.getValueDiscount());
            item.setValueNeed(voucherRequestDto.getValueNeed());
            item.setLimitPerUser(voucherRequestDto.getLimitPerUser());
            item.setMaxDiscount(voucherRequestDto.getMaxDiscount());
            item.setVoucherType(voucherRequestDto.getVoucherType().toString());
            item.setQuantity(voucherRequestDto.getQuantity());
            item.setImage(voucherRequestDto.getImage());
            item.setDescription(voucherRequestDto.getDescription());
        }, () -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS));
    }

    @Transactional(readOnly = true)
    public VoucherResponseDto getDetail(Long id) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        VoucherResponseDto voucherResponseDto = new VoucherResponseDto();
        voucherRepository.findByIdAndShopId(id, shop.getId()).ifPresentOrElse((item) ->
        {
            BeanUtils.copyProperties(item, voucherResponseDto);
        }, () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT));
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
}
