package tech.dut.safefood.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.AdminReviewDetailResponseDto;
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
public class AdminCommunityService {
    private final ShopRepository shopRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final UserRepository userRepository;
    private final UserFavoriteReplyRepository userFavoriteReplyRepository;
    private final UserFavoriteReviewRepository userFavoriteReviewRepository;
    private final ReplyRepository replyRepository;

    @Transactional(rollbackFor = SafeFoodException.class)
    public void adminDeleteReview(Integer reviewId) {
        User user = getCurrentUser().get();
        Review review = reviewRepository.findById(reviewId.longValue()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );

        List<UserFavoriteReview> userFavoriteReviews = userFavoriteReviewRepository.findAllByReviewId(reviewId.longValue());
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
    public void adminDeleteReply(Integer replyId) {
        User user = getCurrentUser().get();

        Reply reply = replyRepository.findById(replyId.longValue()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REPLY_NOT_FOUND)
        );
        List<UserFavoriteReply> userFavoriteReplies = userFavoriteReplyRepository.findAllByReplyId(replyId.longValue());
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
    public AdminReviewDetailResponseDto adminGetReviewDetail(Long id) {
        User user = getCurrentUser().get();
        AdminReviewDetailResponseDto responseDTO = reviewRepository.adminGetReviewDetail(id).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_REVIEW_NOT_FOUND)
        );
        responseDTO.setImages(reviewImageRepository.findListStringByReviewId(id));
        responseDTO.setReply(replyRepository.userGetAllReplyByReview(id, user.getId()));
        return responseDTO;
    }

    public PageInfo<ReviewResponseDto> adminGetAllReview(Integer limit, Integer page, Long shopId) {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<ReviewResponseDto> data;
        User user = getCurrentUser().get();

        data = reviewRepository.adminGetAllReview(shopId, pageable).map(ReviewResponseDto::new);

        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewIdIn(data.stream().map(ReviewResponseDto::getId).collect(Collectors.toList()));
        data.getContent().forEach(reviewDto -> reviewDto.setImages(reviewImages.stream().filter(b -> b.getReview().getId().equals(reviewDto.getId()))
                .map(ReviewImage::getImagesUrl).collect(Collectors.toList())));

        return AppUtils.pagingResponse(data);
    }
}
