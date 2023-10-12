package tech.dut.safefood.service.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.ReviewDetailResponseDto;
import tech.dut.safefood.dto.response.ReviewResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.*;
import tech.dut.safefood.repository.*;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopCommunityService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final UserFavoriteReplyRepository userFavoriteReplyRepository;
    private final UserFavoriteReviewRepository userFavoriteReviewRepository;
    private final ReplyRepository replyRepository;

    @Transactional(rollbackFor = SafeFoodException.class)
    public void shopLikeOrDislikeReview(Integer reviewId) {
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Review review = reviewRepository.findByIdAndShopId(reviewId.longValue(), shop.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        Optional<UserFavoriteReview> oUserFavoriteReview = userFavoriteReviewRepository.findByUserIdAndReviewId(user.getId(), reviewId.longValue());
        UserFavoriteReview userFavoriteReview;
        if (oUserFavoriteReview.isPresent()) {
            userFavoriteReviewRepository.delete(oUserFavoriteReview.get());
        } else {
            userFavoriteReview = new UserFavoriteReview();
            userFavoriteReview.setReview(review);
            userFavoriteReview.setUser(user);
            userFavoriteReviewRepository.save(userFavoriteReview);
        }
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void shopLikeOrDislikeReply(Integer replyId) {
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Reply reply = replyRepository.shopGetReply(replyId.longValue(), shop.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REPLY_NOT_FOUND)
        );
        Optional<UserFavoriteReply> oUserFavoriteReply = userFavoriteReplyRepository.findByUserIdAndReplyId(user.getId(), replyId.longValue());
        UserFavoriteReply userFavoriteReply;
        if (oUserFavoriteReply.isPresent()) {
            userFavoriteReplyRepository.delete(oUserFavoriteReply.get());
        } else {
            userFavoriteReply = new UserFavoriteReply();
            userFavoriteReply.setReply(reply);
            userFavoriteReply.setUser(user);
            userFavoriteReplyRepository.save(userFavoriteReply);
        }
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void shopDeleteReview(Integer reviewId) {
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Review review = reviewRepository.findByIdAndShopId(reviewId.longValue(), shop.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );

        List<UserFavoriteReview> userFavoriteReviews = userFavoriteReviewRepository.findAllByUserIdAndReviewId(user.getId(), reviewId.longValue());
        if (!userFavoriteReviews.isEmpty()) {
            userFavoriteReviewRepository.deleteAll(userFavoriteReviews);
        }

        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId.longValue());
        if (!reviewImages.isEmpty()) {
            reviewImageRepository.deleteAll(reviewImages);
        }

        List<Reply> replies = replyRepository.findAllByReviewId(reviewId.longValue());
        if (!replies.isEmpty()) {
            List<Long> replyIds = replies.stream().map(Reply::getId).collect(Collectors.toList());
            List<UserFavoriteReply> userFavoriteReplies = userFavoriteReplyRepository.findAllByReplyIdIn(replyIds);
            if (!userFavoriteReplies.isEmpty()) {
                userFavoriteReplyRepository.deleteAll(userFavoriteReplies);
            }
            replyRepository.deleteAll(replies);
        }

        reviewRepository.delete(review);
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void shopDeleteReply(Integer replyId) {
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        Reply reply = replyRepository.shopGetReply(replyId.longValue(), shop.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REPLY_NOT_FOUND)
        );
        List<UserFavoriteReply> userFavoriteReplies = userFavoriteReplyRepository.findAllByUserIdAndReplyId(user.getId(), replyId.longValue());
        if (!userFavoriteReplies.isEmpty()) {
            userFavoriteReplyRepository.deleteAll(userFavoriteReplies);
        }
        replyRepository.delete(reply);
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

    @Transactional(rollbackFor = SafeFoodException.class)
    public ReviewDetailResponseDto shopGetReviewDetail(Long id) {
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        ReviewDetailResponseDto responseDTO = reviewRepository.shopGetReviewDetail(id, user.getId(), shop.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        responseDTO.setImages(reviewImageRepository.findListStringByReviewId(id));
        responseDTO.setReply(replyRepository.userGetAllReplyByReview(id, user.getId()));
        return responseDTO;
    }

    public PageInfo<ReviewResponseDto> shopGetAllReview(Integer limit, Integer page) {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<ReviewResponseDto> data;
        User user = getCurrentUser().get();
        Shop shop = shopRepository.findByUserId(user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        data = reviewRepository.shopGetAllReview(user.getId(), shop.getId(), pageable).map(ReviewResponseDto::new);

        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewIdIn(data.stream().map(ReviewResponseDto::getId).collect(Collectors.toList()));
        data.getContent().forEach(reviewDto -> reviewDto.setImages(reviewImages.stream().filter(b -> b.getReview().getId().equals(reviewDto.getId()))
                .map(ReviewImage::getImagesUrl).collect(Collectors.toList())));

        return AppUtils.pagingResponse(data);
    }
}
