package tech.dut.safefood.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.FeedBackShopRequestDto;
import tech.dut.safefood.dto.response.ShopRatingsResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Bill;
import tech.dut.safefood.model.Ratings;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.repository.RatingsRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.util.AppUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserRatingShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private RatingsRepository ratingsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private BillRepository billRepository;

    @Transactional(rollbackFor = {SafeFoodException.class}, isolation = Isolation.SERIALIZABLE)
    public void feedBackStart(FeedBackShopRequestDto feedBackShopRequestDto) throws SafeFoodException {
        User useTmp = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Shop shop = shopRepository.findById(feedBackShopRequestDto.getShopId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Double start = shop.getStartRating() * shop.getRatings();
        start = (start + feedBackShopRequestDto.getStart()) / (shop.getRatings() + 1D);
        shop.setRatings(shop.getRatings() + 1D);
        shop.setStartRating(start);

        Bill bill = billRepository.findById(feedBackShopRequestDto.getBillId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS));
        bill.setIsRating(true);
        bill.setRatings(feedBackShopRequestDto.getStart());

        Ratings ratings = new Ratings();
        Long userId = useTmp.getId();
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        ratings.setRatings(feedBackShopRequestDto.getStart());
        ratings.setShop(shop);
        ratings.setUser(user);
        ratings.setContent(feedBackShopRequestDto.getContent());
        ratingsRepository.save(ratings);
    }

    @Transactional
    public List<ShopRatingsResponseDto> getAllRatingUser(Long shopId) {
        if (!shopRepository.existsById(shopId)) {
            throw new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND);
        }
        return ratingsRepository.getAllRatingShop(shopId);
    }
}
