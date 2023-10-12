package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.NotificationDto;
import tech.dut.safefood.dto.request.ReplyRequestDto;
import tech.dut.safefood.dto.request.ReviewRequestDto;
import tech.dut.safefood.dto.request.UserDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.*;
import tech.dut.safefood.repository.*;
import tech.dut.safefood.service.FirebaseService;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCommunityService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final UserFavoriteReplyRepository userFavoriteReplyRepository;
    private final UserFavoriteReviewRepository userFavoriteReviewRepository;
    private final ReplyRepository replyRepository;
    private final NotificationRepository notificationRepository;

    private final FirebaseService firebaseService;

    @Transactional(rollbackFor = SafeFoodException.class)
    public ReviewCreateResponseDto createReview(ReviewRequestDto reviewRequestDTO) {
        ReviewCreateResponseDto responseDTO = new ReviewCreateResponseDto();
        User user = getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        Review review = new Review();

        Shop shop = shopRepository.findById(reviewRequestDTO.getShopId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        review.setShop(shop);
        review.setUser(user);

        BeanUtils.copyProperties(reviewRequestDTO, review);
        reviewRepository.save(review);
        updateReviewImage(reviewRequestDTO.getReviewImages(), review);

        BeanUtils.copyProperties(review, responseDTO);
        responseDTO.setImages(reviewRequestDTO.getReviewImages());
        responseDTO.setShopId(shop.getId());
        return responseDTO;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void updateReviewImage(List<String> images, Review review) {
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (String image : images) {
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.setReview(review);
            reviewImage.setImagesUrl(image);
            reviewImages.add(reviewImage);
        }
        reviewImageRepository.saveAll(reviewImages);
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public ReplyCreateResponseDto createReply(ReplyRequestDto replyRequestDto, Integer reviewId) {
        Review review = reviewRepository.findById(reviewId.longValue()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        User user = getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));

        Reply reply = new Reply();
        reply.setReview(review);
        reply.setUser(user);
        reply.setContent(replyRequestDto.getContent());
        replyRepository.save(reply);

        if (!user.equals(review.getUser())) {
            List<UserDto> replyDTOs = replyRepository.findReplyByReviewId(reply.getReview().getId()).stream().distinct().collect(Collectors.toList());
            handleUserReplyReviewNotification(replyDTOs, review);
        }
        return new ReplyCreateResponseDto(reply.getId(), reviewId.longValue(), reply.getContent());
    }

    private void handleUserReplyReviewNotification(List<UserDto> replyDTOs, Review review) {
        Notification notification = new Notification();
        if (replyDTOs.size() == 1) {
            notification = saveNotification(replyDTOs.get(0).getUsername(), Constants.NOTIFICATION_COMMUNITY_USER_COMMENT_REVIEW_CONTENT, review, review.getTitle());
        } else if (replyDTOs.size() == 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_TWO_USER, replyDTOs.get(0).getUsername(), replyDTOs.get(1).getUsername()),
                    Constants.NOTIFICATION_COMMUNITY_USER_COMMENT_REVIEW_CONTENT, review, review.getTitle());
        } else if (replyDTOs.size() > 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_MULTIPLE_USER, replyDTOs.get(0).getUsername(), replyDTOs.size() - 1),
                    Constants.NOTIFICATION_COMMUNITY_USER_COMMENT_REVIEW_CONTENT, review, review.getTitle());
        }
        if (!replyDTOs.isEmpty()) {
            NotificationDto notificationDTO = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDTO);
            firebaseService.sendUserCommentReviewNotification(review, notificationDTO);
        }
    }

    public void userLikeOrDislikeReview(Integer reviewId) {
        User user = getCurrentUser().get();
        Review review = reviewRepository.findById(reviewId.longValue()).orElseThrow(
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
            userFavoriteReview = userFavoriteReviewRepository.save(userFavoriteReview);

            if (!user.equals(review.getUser())) {
                List<UserDto> userFavoriteReviews = userFavoriteReviewRepository.findUserDtoByReviewId(userFavoriteReview.getReview().getId());
                handleUserLikeReviewNotification(userFavoriteReviews, review);
            }
        }
    }

    private void handleUserLikeReviewNotification(List<UserDto> userFavouriteReviews, Review review) {
        Notification notification = new Notification();
        if (userFavouriteReviews.size() == 1) {
            notification = saveNotification(userFavouriteReviews.get(0).getUsername(), Constants.NOTIFICATION_COMMUNITY_USER_REACTED_REVIEW_CONTENT, review, review.getTitle());
        } else if (userFavouriteReviews.size() == 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_TWO_USER, userFavouriteReviews.get(0).getUsername(), userFavouriteReviews.get(1).getUsername()),
                    Constants.NOTIFICATION_COMMUNITY_USER_REACTED_REVIEW_CONTENT, review, review.getTitle());
        } else if (userFavouriteReviews.size() > 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_MULTIPLE_USER, userFavouriteReviews.get(0).getUsername(), userFavouriteReviews.size() - 1),
                    Constants.NOTIFICATION_COMMUNITY_USER_REACTED_REVIEW_CONTENT, review, review.getTitle());
        }
        if (!userFavouriteReviews.isEmpty()) {
            NotificationDto notificationDTO = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDTO);
            firebaseService.sendUserLikeReviewNotification(review, notificationDTO);
        }
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void userLikeOrDislikeReply(Integer replyId) {
        User user = getCurrentUser().get();
        Reply reply = replyRepository.findById(replyId.longValue()).orElseThrow(
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

            if (!user.equals(reply.getUser())) {
                List<UserDto> userFavouriteReplies = userFavoriteReplyRepository.findUserDtoByReplyId(userFavoriteReply.getReply().getId());
                handleUserLikeReplyNotification(userFavouriteReplies, reply);
            }
        }
    }

    private void handleUserLikeReplyNotification(List<UserDto> userFavouriteReplies, Reply reply) {
        Notification notification = new Notification();
        if (userFavouriteReplies.size() == 1) {
            notification = saveNotification(userFavouriteReplies.get(0).getUsername(), Constants.NOTIFICATION_COMMUNITY_USER_REACTED_COMMENT_CONTENT, reply, reply.getContent());
        } else if (userFavouriteReplies.size() == 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_TWO_USER, userFavouriteReplies.get(0).getUsername(), userFavouriteReplies.get(1).getUsername()),
                    Constants.NOTIFICATION_COMMUNITY_USER_REACTED_COMMENT_CONTENT, reply, reply.getContent());
        } else if (userFavouriteReplies.size() > 2) {
            notification = saveNotification(String.format(Constants.NOTIFICATION_COMMUNITY_MULTIPLE_USER, userFavouriteReplies.get(0).getUsername(), userFavouriteReplies.size() - 1),
                    Constants.NOTIFICATION_COMMUNITY_USER_REACTED_COMMENT_CONTENT, reply, reply.getContent());
        }
        if (!userFavouriteReplies.isEmpty()) {
            NotificationDto notificationDTO = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDTO);
            firebaseService.sendUserLikeReplyNotification(reply, notificationDTO);
        }
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void userDeleteReview(Integer reviewId) {
        User user = getCurrentUser().get();
        Review review = reviewRepository.findByIdAndUserId(reviewId.longValue(), user.getId()).orElseThrow(
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
    public void userDeleteReply(Integer replyId) {
        User user = getCurrentUser().get();
        Reply reply = replyRepository.findById(replyId.longValue()).orElseThrow(
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
    public ReviewCreateResponseDto editReview(ReviewRequestDto reviewRequestDTO, Long reviewId) {
        Review review = reviewRepository.findById(reviewId.longValue()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        review.setTitle(reviewRequestDTO.getTitle());
        review.setContent(reviewRequestDTO.getContent());
        reviewRepository.save(review);

        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId);
        if (!reviewImages.isEmpty()) {
            reviewImageRepository.deleteAll(reviewImages);
        }
        if (!reviewRequestDTO.getReviewImages().isEmpty()) {
            updateReviewImage(reviewRequestDTO.getReviewImages(), review);
        }

        ReviewCreateResponseDto responseDTO = new ReviewCreateResponseDto();
        responseDTO.setTitle(review.getTitle());
        responseDTO.setContent(review.getContent());
        responseDTO.setImages(reviewRequestDTO.getReviewImages());
        responseDTO.setShopId(review.getShop().getId());
        return responseDTO;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public ReplyCreateResponseDto editReply(String content, Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REPLY_NOT_FOUND)
        );
        reply.setContent(content);
        replyRepository.save(reply);
        return new ReplyCreateResponseDto(reply.getId(), replyId, reply.getContent());
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public ReviewDetailResponseDto userGetReviewDetail(Long id) {
        User user = getCurrentUser().get();
        ReviewDetailResponseDto responseDTO = reviewRepository.userGetReviewDetail(id, user.getId()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        responseDTO.setImages(reviewImageRepository.findListStringByReviewId(id));
        responseDTO.setReply(replyRepository.userGetAllReplyByReview(id, user.getId()));
        return responseDTO;
    }

    public PageInfo<TopReviewResponseDto> userGetTopReview(Long shopId, Integer limit, Integer page) {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        return AppUtils.pagingResponse(reviewRepository.getAllTopReview(shopId, pageable));
    }

    public PageInfo<ReviewResponseDto> userGetAllReview(Long shopId, Integer limit, Integer page, String filter) {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<ReviewResponseDto> data;
        Long currentUserId = getCurrentUser().get().getId();

        data = reviewRepository.userGetAllReview(currentUserId, shopId, filter, pageable).map(ReviewResponseDto::new);

        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewIdIn(data.stream().map(ReviewResponseDto::getId).collect(Collectors.toList()));
        data.getContent().forEach(reviewDto -> reviewDto.setImages(reviewImages.stream().filter(b -> b.getReview().getId().equals(reviewDto.getId()))
                .map(ReviewImage::getImagesUrl).collect(Collectors.toList())));

        return AppUtils.pagingResponse(data);
    }

    public <T> Notification saveNotification(String title, String content, T t, String typeName) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        User fromUser = getCurrentUser().get();
        if (t instanceof Review) {
            Review review = (Review) t;
            notification.setType(CommonEnum.NotificationType.REVIEW);
            notification.setTypeId(review.getId().intValue());
            notification.setFromUser(fromUser);
            notification.setToUser(review.getUser());
        } else if (t instanceof Reply) {
            Reply reply = (Reply) t;
            notification.setType(CommonEnum.NotificationType.REPLY);
            notification.setTypeId(reply.getId().intValue());
            notification.setFromUser(fromUser);
            notification.setToUser(reply.getUser());
        }
        notification.setTypeName(typeName);
        return notificationRepository.save(notification);
    }
}
