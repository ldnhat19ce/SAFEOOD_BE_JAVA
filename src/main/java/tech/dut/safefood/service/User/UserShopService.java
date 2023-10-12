package tech.dut.safefood.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.*;
import tech.dut.safefood.repository.*;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private RecentShopRepository recentShopRepository;

    @Autowired
    private ShopFauvouriteRepository shopFauvouriteRepository;

    @Transactional(readOnly = true)
    public UserShopResponseDto getDetailShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        User user = getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        ShopProfileResponseDto shopProfileResponseDto = new ShopProfileResponseDto();
        shopProfileResponseDto.setId(shop.getId());
        shopProfileResponseDto.setName(shop.getName());
        shopProfileResponseDto.setLogo(shop.getBanner());
        shopProfileResponseDto.setDescription(shop.getDescription());
        shopProfileResponseDto.setPhone(shop.getPhone());
        if(shopFauvouriteRepository.existsByUserIdAndShopId(user.getId(), shopId)){
            shopProfileResponseDto.setIsFauvorite(true);
        }
        else {
            shopProfileResponseDto.setIsFauvorite(false);
        }
        List<String> imagesShop = shop.getShopImages().stream().map(ShopImage::getImage).collect(Collectors.toList());
        shopProfileResponseDto.setImageShops(imagesShop);
        shopProfileResponseDto.setRantings(shop.getRatings());
        shopProfileResponseDto.setCity(shop.getAddresses().getCity());
        shopProfileResponseDto.setDistrict(shop.getAddresses().getDistrict());
        shopProfileResponseDto.setTown(shop.getAddresses().getTown());
        shopProfileResponseDto.setStreet(shop.getAddresses().getStreet());
        shopProfileResponseDto.setX(shop.getAddresses().getX());
        shopProfileResponseDto.setY(shop.getAddresses().getY());

        Pageable pageable = AppUtils.buildPageableCreated(null, null);
        List<ProductResponseDto> productResponseDtos = productRepository.getAllProductsAndStatus(pageable, shopId, Constants.PRODUCT_STATUS_RECOMMEND).getContent();

        Pageable pageableVoucher = AppUtils.buildPageableCreated(null, 3);
        List<VoucherResponseDto> voucherResponseDtos = voucherRepository.getAllVouchers(pageableVoucher, shopId).getContent();

        UserShopResponseDto userShopResponseDto = new UserShopResponseDto();
        userShopResponseDto.setShopProfile(shopProfileResponseDto);
        userShopResponseDto.setListProduct(productResponseDtos);
        userShopResponseDto.setListVoucher(voucherResponseDtos);

        return userShopResponseDto;
    }

    @Transactional(readOnly = true)
    public List<TurnoverTotalShopResponseDto> getTopRevenue(Long dateFrom, Long dateTo, Integer top) {
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

        if (top == null) {
            top = 3;
        }

        Collections.sort(responseDtoList, new Comparator<TurnoverTotalShopResponseDto>() {
            @Override
            public int compare(TurnoverTotalShopResponseDto o1, TurnoverTotalShopResponseDto o2) {
                return o2.getTotalPrice().compareTo(o1.getTotalPrice());
            }
        });

        responseDtoList = responseDtoList.stream().limit(top).collect(Collectors.toList());
        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public PageInfo<ShopLocationResponseDTO> getAllLocationByCurrentLocation(Double x, Double y, Double radius, Integer limit, Integer page) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        Page<ShopLocationResponseDTO> userLocations = shopRepository.findAllShopLocationByName(radius, x, y, pageable);
        for (ShopLocationResponseDTO userLocation : userLocations) {
            userLocation.setDistance(AppUtils.getDistanceFromLatLonInKm(userLocation.getX(), userLocation.getY(), x, y));
        }
        return AppUtils.pagingResponse(userLocations);
    }

    @Transactional(readOnly = true)
    public PageInfo<ShopLocationResponseDTO> getAllShopLocationByCurrentLocation(Long shopId, Double radius, Integer limit, Integer page) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        Page<ShopLocationResponseDTO> userLocations = shopRepository.findAllShopLocationByName(radius, shopId, pageable);

        for (ShopLocationResponseDTO userLocation : userLocations) {
            userLocation.setDistance(AppUtils.getDistanceFromLatLonInKm(userLocation.getX(), userLocation.getY(), shop.getAddresses().getX(), shop.getAddresses().getY()));
        }
        return AppUtils.pagingResponse(userLocations);
    }

    @Transactional(readOnly = true)
    public PageInfo<ShopResponseDto> getAllShops(Integer page, Integer limit, UserEnum.Status status, String query) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        if (status != null) {
            return AppUtils.pagingResponse(shopRepository.getAllShop(pageable, status, query));
        }
        return AppUtils.pagingResponse(shopRepository.getAllShop(pageable, query));
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void createRecentShop(Long shopId) throws SafeFoodException {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Optional<RecentShop> optional = recentShopRepository.findByUserIdAndShopId(user.getId(), shop.getId());
        if (optional.isPresent()) {
            RecentShop recentShop = optional.get();
            recentShopRepository.delete(recentShop);
        }
        RecentShop recentShop = new RecentShop();
        recentShop.setShop(shop);
        recentShop.setUser(user);
        recentShopRepository.save(recentShop);
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public List<ShopResponseDto> getRecentShop() throws SafeFoodException {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        return recentShopRepository.getAllRecentShop(user.getId());
    }
    @Transactional(rollbackFor = SafeFoodException.class)
    public Optional<User> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            User user = null;
            if (authentication.getPrincipal() instanceof UserDetails) {
                SafeFoodUserPrincipal userPrincipal = (SafeFoodUserPrincipal) authentication.getPrincipal();
                user = userRepository.findById(userPrincipal.getUserId()).get();
                return user;
            }
            return null;
        });
    }

}
