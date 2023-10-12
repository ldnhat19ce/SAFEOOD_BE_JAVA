package tech.dut.safefood.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.VoucherRequestDto;
import tech.dut.safefood.dto.request.VoucherUpdateRequestDto;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.enums.VoucherEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.Voucher;
import tech.dut.safefood.repository.VoucherRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminManagerVoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AppUtils appUtils;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void createVoucher(VoucherRequestDto voucherRequestDto) throws SafeFoodException {
        if (!voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.PERCENT.toString()) && !voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.AMOUNT.toString())) {
            throw new SafeFoodException(SafeFoodException.ERROR_TYPE_VOUCHER_IS_NOT_CORRECT);

        }
        if(voucherRequestDto.getCreatedAt() > voucherRequestDto.getEndedAt()){
            throw new SafeFoodException(SafeFoodException.ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT);
        }
        Voucher voucher = new Voucher();
        voucher.setName(voucherRequestDto.getName());
        voucher.setCreatedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getCreatedAt()));
        voucher.setEndedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt()));
        voucher.setValueDiscount(voucherRequestDto.getValueDiscount());
        voucher.setValueNeed(voucherRequestDto.getValueNeed());
        voucher.setLimitPerUser(voucherRequestDto.getLimitPerUser());
        voucher.setMaxDiscount(voucherRequestDto.getMaxDiscount());
        voucher.setUserType(Constants.ROLE_ADMIN);
        voucher.setQuantity(voucherRequestDto.getQuantity());
        voucher.setImage(voucherRequestDto.getImage());
        voucher.setDescription(voucherRequestDto.getDescription());
        voucher.setVoucherType(voucherRequestDto.getVoucherType().toString());
        voucherRepository.save(voucher);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void deleteVoucher(Long voucherId) throws SafeFoodException {
        voucherRepository.findByIdAndUserType(voucherId, Constants.ROLE_ADMIN).ifPresentOrElse((item) -> {
            item.setDeleteFlag(true);
        }, () -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS));
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void updateVoucher(VoucherUpdateRequestDto voucherRequestDto, Long id) throws SafeFoodException {
        if (!voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.PERCENT.toString()) && !voucherRequestDto.getVoucherType().equals(VoucherEnum.Type.AMOUNT.toString())) {
            throw new SafeFoodException(SafeFoodException.ERROR_TYPE_VOUCHER_IS_NOT_CORRECT);

        }
        Voucher voucher = voucherRepository.findByIdAndUserTypeAndDeleteFlagIsFalse(id, Constants.ROLE_ADMIN).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS));
        LocalDateTime tmpEnd = AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt());
        if(voucher.getCreatedAt().isAfter(tmpEnd)){
            throw new SafeFoodException(SafeFoodException.ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT);
        }
        voucher.setName(voucherRequestDto.getName());
        voucher.setEndedAt(AppUtils.convertMilliToLocalDateTime(voucherRequestDto.getEndedAt()));
        voucher.setValueDiscount(voucherRequestDto.getValueDiscount());
        voucher.setValueNeed(voucherRequestDto.getValueNeed());
        voucher.setLimitPerUser(voucherRequestDto.getLimitPerUser());
        voucher.setMaxDiscount(voucherRequestDto.getMaxDiscount());
        voucher.setQuantity(voucherRequestDto.getQuantity());
        voucher.setImage(voucherRequestDto.getImage());
        voucher.setDescription(voucherRequestDto.getDescription());
        voucher.setVoucherType(voucherRequestDto.getVoucherType().toString());
        voucherRepository.save(voucher);
    }

    @Transactional(readOnly = true)
    public List<VoucherResponseDto> getAllVoucher(Integer page, Integer limit, String query, String status) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        Page<VoucherResponseDto> voucherResponseDtos = voucherRepository.getAllVoucherByAdmin(pageable, Constants.ROLE_ADMIN, query);
        List<VoucherResponseDto> responseDtoList = voucherResponseDtos.getContent();
        for (VoucherResponseDto voucherResponseDto : responseDtoList) {
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

        if (status != null) {
            responseDtoList = responseDtoList.stream().filter(v -> v.getStatus().equals(status)).collect(Collectors.toList());
            Collections.sort(responseDtoList, new Comparator<VoucherResponseDto>() {
                @Override
                public int compare(VoucherResponseDto o1, VoucherResponseDto o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
        }
        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public VoucherResponseDto getDetailVoucher(Long id) throws SafeFoodException {
        if (!voucherRepository.existsById(id)) {
            throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS);
        }
        VoucherResponseDto voucherResponseDto = voucherRepository.getDetailVoucherByAdmin(Constants.ROLE_ADMIN, id);
        if (!voucherResponseDto.getDeleteFlag()) {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_ACTIVED);
        } else {
            voucherResponseDto.setStatus(Constants.VOUCHER_STATUS_INACTIVE);
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
    public VoucherResponseDto getDetailVoucherShop(Long id) throws SafeFoodException {
        if (!voucherRepository.existsById(id)) {
            throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_IS_NOT_EXISTS);
        }
        VoucherResponseDto voucherResponseDto = voucherRepository.getDetailVoucherByAdmin(id);
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
    public List<VoucherResponseDto> getAllVoucherShop(Integer page, Integer limit, Long id, String query, String status) throws SafeFoodException {
        if (status != null && status != VoucherEnum.Status.ACTIVED.toString() && status != VoucherEnum.Status.INACTIVE.toString() && status != VoucherEnum.Status.DELETED.toString()) {
            throw new SafeFoodException(SafeFoodException.ERROR_VOUCHER_STATUS_IS_NOT_CORRECT);
        }

        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        Page<VoucherResponseDto> dtoPage = voucherRepository.getAllVouchers(pageable, id, query);
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
}
