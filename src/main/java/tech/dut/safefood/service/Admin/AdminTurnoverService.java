package tech.dut.safefood.service.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.TurnoverResponseDto;
import tech.dut.safefood.dto.response.TurnoverTotalResponseDto;
import tech.dut.safefood.dto.response.TurnoverTotalShopResponseDto;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTurnoverService {

    @Autowired
    private BillRepository billRepository;

    @Transactional(readOnly = true)
    public List<TurnoverTotalShopResponseDto> getRevenue(Long dateFrom, Long dateTo) {
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
        return billRepository.getAllTotalTurnoverShop(localDateFrom, localDateTo);
    }

    @Transactional(readOnly = true)
    public TurnoverTotalShopResponseDto getRevenueShop(Long dateFrom, Long dateTo, Long shopId) {
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
        return billRepository.getTurnoverShop(localDateFrom, localDateTo, shopId);
    }

    public List<TurnoverTotalShopResponseDto> getTop10Revenue(Long dateFrom, Long dateTo){
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
        List<TurnoverTotalShopResponseDto> responseDtoList = billRepository.getAllTotalTurnoverShop(localDateFrom, localDateTo);

        Collections.sort(responseDtoList, new Comparator<TurnoverTotalShopResponseDto>() {
            @Override
            public int compare(TurnoverTotalShopResponseDto o1, TurnoverTotalShopResponseDto o2) {
                return o1.getTotalPrice().compareTo(o2.getTotalPrice());
            }
        });

        responseDtoList = responseDtoList.stream().limit(10).collect(Collectors.toList());
        return responseDtoList;
    }

}
