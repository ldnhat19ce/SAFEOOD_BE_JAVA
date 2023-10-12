package tech.dut.safefood.service.Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.ShopRatingsResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.repository.RatingsRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.util.AppUtils;

import java.util.List;

@Service
public class ShopRatingsService {
    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private AppUtils appUtils;

    @Transactional
    public List<ShopRatingsResponseDto> getAllRatingUser(){
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Long shopId = shop.getId();
        if(!shopRepository.existsById(shopId)){
            throw new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND);
        }
        return ratingsRepository.getAllRatingShop(shopId);
    }
}
