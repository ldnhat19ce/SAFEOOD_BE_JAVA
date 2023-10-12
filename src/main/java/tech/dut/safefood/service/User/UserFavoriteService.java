package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.ShopFauvoriteResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.ShopFauvourite;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.ShopFauvouriteRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.util.AppUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {

    @Autowired
    private ShopFauvouriteRepository shopFauvouriteRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private AppUtils appUtils;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void createFavouriteShop(Long shopId) throws SafeFoodException {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
//        Optional<ShopFauvourite> optionalShopFauvourite  =  shopFauvouriteRepository.findByShopIdAndUserId(shop.getId(), user.getId());
//        if(optionalShopFauvourite.isPresent()){
//            ShopFauvourite shopFauvourite = optionalShopFauvourite.get();
//            shopFauvouriteRepository.delete(shopFauvourite);
//        }
        ShopFauvourite shopFauvourite = new ShopFauvourite();
        shopFauvourite.setShop(shop);
        shopFauvourite.setUser(user);
        shopFauvouriteRepository.save(shopFauvourite);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void deleteFavouriteShop(Long id) throws SafeFoodException {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Long userId = user.getId();
        shopFauvouriteRepository.findByIdAndUserId(id, userId).ifPresentOrElse((item) -> shopFauvouriteRepository.delete(item), () -> new SafeFoodException(SafeFoodException.ERROR_USER_FAVOURITE_SHOP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<ShopFauvoriteResponseDto> getAllFauvoriteUsers() throws SafeFoodException {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Long userId = user.getId();
        return shopFauvouriteRepository.getAllShopFauvorite(userId);
    }
}
