package tech.dut.safefood.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.util.constants.PageableConstants;
import tech.dut.safefood.vo.PageInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppUtils {


    @Autowired
    private UserRepository userRepository;

    public static String generateDigitCode() {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        return randomStringGenerator.generate(Constants.DIGIT_CODE_LENGTH);
    }

    public Optional<Shop> getShopCurrent() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                SafeFoodUserPrincipal userPrincipal = (SafeFoodUserPrincipal) authentication.getPrincipal();
                Optional<User> userOptional = userRepository.findById(userPrincipal.getUserId());
                if (userOptional.isPresent()) {
                    return userOptional.get().getShop();
                }
                return null;
            }
            return null;
        });
    }

    public static Pageable buildPageableCreated(Integer page, Integer limit) {
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        Sort sort = Sort.by(Sort.Order.desc(PageableConstants.DEFAULT_FIELD_SORT));
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        return PageRequest.of(page, limit, sort);
    }

    public static Pageable buildPageable(Integer page, Integer limit) {
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        return PageRequest.of(page, limit);
    }

    public static <T> PageInfo<T> pagingResponse(Page<T> page) {
        PageInfo<T> pageInfo = new PageInfo<T>();
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setLimit(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        pageInfo.setPage(page.getNumber() + 1);
        pageInfo.setResult(page.getContent());
        return pageInfo;
    }

    public static <T> PageInfo<T> pagingResponseCustom(Page<T> page, List<T> listCustom) {
        PageInfo<T> pageInfo = new PageInfo<T>();
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setLimit(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        pageInfo.setPage(page.getNumber() + 1);
        pageInfo.setResult(listCustom);
        return pageInfo;
    }

    public static LocalDateTime convertMilliToLocalDateTime(Long millis) {
        if (null == millis) {
            return null;
        }
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String convertLocalDateTimeToText(String format, LocalDateTime time, String timeAppend) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String outputString = StringUtils.EMPTY;
        if (null != time) {
            outputString = formatter.format(time).concat(" " + timeAppend);
        }
        return outputString;
    }

    public static LocalDateTime convertStringToLocalDateTime(String date, String format) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public Optional<User> getCurrentUser() throws SafeFoodException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            User user = null;
            if (authentication.getPrincipal() instanceof UserDetails) {
                SafeFoodUserPrincipal userPrincipal = (SafeFoodUserPrincipal) authentication.getPrincipal();
                user = userRepository.findById(userPrincipal.getUserId()).get();
                if (!user.getRole().getName().equals(Constants.ROLE_USER)) {
                    throw new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND);
                }
                return user;
            }
            return null;
        });
    }

    public static LocalDate instantToLocalDateTime(Instant instant, ZoneId zone){
        return LocalDate.ofInstant(instant, zone);
    }

    public static LocalDateTime instantToLocalDateTime(Instant instant){
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Long convertLocalDateTimeToMilli(LocalDateTime dateTime) {
        if (null != dateTime) {
            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return 0L;
    }

    public static Sort buildSortCreated(String sortStr) {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        if (StringUtils.isNotEmpty(sortStr)) {
            switch (sortStr) {
                case Constants.SORT_OLDEST_TO_NEWEST:
                    sort = Sort.by(Sort.Order.asc("createdAt"));
                    break;
                case Constants.SORT_NEWEST_TO_OLDEST:
                    sort = Sort.by(Sort.Order.desc("createdAt"));
                    break;
            }
        }
        return sort;
    }

    public static PageRequest buildPageRequest(Integer page, Integer limit, Sort sort) {
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        return PageRequest.of(page, limit, sort);
    }

    public static Double getDistanceFromLatLonInKm(Double x1, Double y1, Double x2, Double y2) {
        Double dLat = deg2rad(x2 - x1);
        Double dLon = deg2rad(y2 - y1);
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(x1)) * Math.cos(deg2rad(x2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return new BigDecimal(Constants.EARTH_RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)))).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}