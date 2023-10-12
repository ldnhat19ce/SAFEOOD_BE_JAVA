package tech.dut.safefood.service.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.TopProductResponseDto;
import tech.dut.safefood.dto.response.TurnoverProductResponseDto;
import tech.dut.safefood.dto.response.TurnoverResponseDto;
import tech.dut.safefood.dto.response.TurnoverTotalResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.repository.BillItemRepository;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.util.constants.PageableConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopManagerTurnoverService {

    @Autowired
    private AppUtils appUtils;
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillItemRepository billItemRepository;

    @Transactional(readOnly = true)
    public TurnoverResponseDto getRevenue(Long dateFrom, Long dateTo) {

        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Long id = shop.getId();
        LocalDateTime localDateFrom = AppUtils.convertMilliToLocalDateTime(dateFrom);
        if (localDateFrom != null) {
            String localDateFromConverted = AppUtils.convertLocalDateTimeToText(Constants.DATE_FORMAT_YYYY_MM_DD, localDateFrom, Constants.DAY_AT_ZERO_O_CLOCK);
            localDateFrom = AppUtils.convertStringToLocalDateTime(localDateFromConverted, Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_2);
        }
        LocalDateTime localDateTo = AppUtils.convertMilliToLocalDateTime(dateTo);
        if (localDateTo != null) {
            String localDateToConverted = AppUtils.convertLocalDateTimeToText(Constants.DATE_FORMAT_YYYY_MM_DD, localDateTo, Constants.DAY_AFTER_AT_ZERO_O_CLOCK);
            localDateTo = AppUtils.convertStringToLocalDateTime(localDateToConverted, Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_2);
        }

        List<TurnoverProductResponseDto> listProducts = billItemRepository.getAllProductPurchase(localDateFrom, localDateTo, id);
        TurnoverTotalResponseDto turnoverTotalResponseDto = billRepository.getTotalTurnover(localDateFrom, localDateTo, id);

        TurnoverResponseDto turnoverResponseDto = new TurnoverResponseDto();

        turnoverResponseDto.setTurnoverTotalResponseDto(turnoverTotalResponseDto);
        turnoverResponseDto.setProductResponseDto(listProducts);

        return turnoverResponseDto;
    }

    @Transactional(readOnly = true)
    public List<TopProductResponseDto> getTopProduct(Long dateFrom, Long dateTo, Integer limit) {

        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));

        LocalDateTime localDateFrom = AppUtils.convertMilliToLocalDateTime(dateFrom);
        if (localDateFrom != null) {
            String localDateFromConverted = AppUtils.convertLocalDateTimeToText(Constants.DATE_FORMAT_YYYY_MM_DD, localDateFrom, Constants.DAY_AT_ZERO_O_CLOCK);
            localDateFrom = AppUtils.convertStringToLocalDateTime(localDateFromConverted, Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_2);
        }
        LocalDateTime localDateTo = AppUtils.convertMilliToLocalDateTime(dateTo);
        if (localDateTo != null) {
            String localDateToConverted = AppUtils.convertLocalDateTimeToText(Constants.DATE_FORMAT_YYYY_MM_DD, localDateTo, Constants.DAY_AFTER_AT_ZERO_O_CLOCK);
            localDateTo = AppUtils.convertStringToLocalDateTime(localDateToConverted, Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_2);
        }
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        Sort sort = Sort.by(Sort.Order.desc("p.id"));
        Pageable pageable = PageRequest.of(PageableConstants.DEFAULT_PAGE, limit, sort);
        Long shopId = shop.getId();
        List<TopProductResponseDto> responseDtoList = billItemRepository.getTopProduct(localDateFrom, localDateTo, shopId, pageable).getContent();
        responseDtoList = responseDtoList.stream().sorted((s1, s2) -> s2.getQuantity().compareTo(s1.getQuantity())).collect(Collectors.toList());
        return responseDtoList;
    }
}
