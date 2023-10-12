package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.AdminReviewDetailResponseDto;
import tech.dut.safefood.dto.response.IReviewResponseDto;
import tech.dut.safefood.dto.response.ReviewDetailResponseDto;
import tech.dut.safefood.dto.response.TopReviewResponseDto;
import tech.dut.safefood.model.Review;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndUserId(Long id, Long userId);

    Optional<Review> findByIdAndShopId(Long id, Long shopId);

    Optional<Review> findByIdAndUserIdAndShopId(Long id, Long userId, Long shopId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ReviewDetailResponseDto("
            + "r.id, "
            + "r.content, "
            + "r.title, "
            + "r.createdAt, "
            + "(select count (ufr)from UserFavoriteReview ufr where r.id = ufr.review.id), "
            + "(select count (rp)from Reply rp where r.id = rp.review.id), "
            + "(select (count (ufr) > 0)from UserFavoriteReview ufr where ufr.review.id = r.id and ufr.user.id = :userId), "
            + "ui.user.id, "
            + "CASE WHEN (ui.lastName IS NOT NULL) THEN CONCAT(ui.firstName, ' ', ui.lastName) ELSE ui.firstName END, "
            + "ui.userImage, "
            + "r.shop.id, "
            + "r.shop.name) "
            + "FROM Review r "
            + "LEFT JOIN UserInformation ui ON ui.user.id = r.user.id "
            + "WHERE r.id = :reviewId")
    Optional<ReviewDetailResponseDto> userGetReviewDetail(Long reviewId, Long userId);

    @Query(value = "select new tech.dut.safefood.dto.response.TopReviewResponseDto("
            + "rv.id, "
            + "rv.shop.id, "
            + "rv.title, "
            + "rv.content) "
            + "FROM Review rv "
            + "LEFT JOIN UserFavoriteReview ufr ON rv.id = ufr.review.id "
            + "LEFT JOIN Reply rl ON rv.id = rl.review.id "
            + "WHERE (:shopId IS NULL OR rv.shop.id = :shopId) "
            + "GROUP BY rv.id "
            + "ORDER BY COUNT(ufr.review.id) DESC, COUNT(rl.review.id) DESC")
    Page<TopReviewResponseDto> getAllTopReview(Long shopId, Pageable pageable);

    @Query(value = "SELECT "
            + "rv.id AS id, "
            + "rv.content AS content, "
            + "rv.title AS title, "
            + "rv.created_at AS createdAt, "
            + "(SELECT DISTINCT COUNT(ufr.id) FROM user_favorite_review ufr WHERE ufr.review_id = rv.id) AS totalFavorite, "
            + "(SELECT (COUNT(r.id)) FROM reply r WHERE r.review_id = rv.id) AS totalReply, "
            + "(SELECT (COUNT(ufr1.id) > 0) FROM user_favorite_review ufr1 WHERE ufr1.review_id = rv.id AND ufr1.user_id = :userId) AS liked, "
            + "ui.user_id AS userId, "
            + "CASE WHEN (ui.last_name IS NOT NULL) THEN CONCAT(ui.first_name, ' ', ui.last_name) ELSE ui.first_name END AS username, "
            + "ui.user_image as userImage "
            + "FROM review rv "
            + "LEFT JOIN user_information ui ON rv.user_id = ui.user_id "
            + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
            + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId) "
            + "AND :filter IS NOT NULL "
            + "AND :userId IS NOT NULL "
            + "GROUP BY rv.id, ui.id "
            + "ORDER BY "
            + "     CASE WHEN (:filter = 'MOST_LIKED') THEN (COUNT(ufr.id)) END DESC",
            countQuery = "SELECT COUNT(rv.id) "
                    + "FROM review rv INNER JOIN user_information ui ON ui.user_id = rv.user_id "
                    + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
                    + "AND :filter IS NOT NULL "
                    + "AND :userId IS NOT NULL "
                    + "GROUP BY rv.id, ui.id "
                    + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId)",
            nativeQuery = true)
    Page<IReviewResponseDto> userGetAllReview(Long userId, Long shopId, String filter, Pageable pageable);

    @Query(value = "SELECT "
            + "rv.id AS id, "
            + "rv.content AS content, "
            + "rv.title AS title, "
            + "rv.created_at AS createdAt, "
            + "(SELECT DISTINCT COUNT(ufr.id) FROM user_favorite_review ufr WHERE ufr.review_id = rv.id) AS totalFavorite, "
            + "(SELECT (COUNT(r.id)) FROM reply r WHERE r.review_id = rv.id) AS totalReply, "
            + "(SELECT (COUNT(ufr1.id) > 0) FROM user_favorite_review ufr1 WHERE ufr1.review_id = rv.id AND ufr1.user_id = :userId) AS liked, "
            + "ui.user_id AS userId, "
            + "CASE WHEN (ui.last_name IS NOT NULL) THEN CONCAT(ui.first_name, ' ', ui.last_name) ELSE ui.first_name END AS username, "
            + "ui.user_image as userImage "
            + "FROM review rv "
            + "LEFT JOIN user_information ui ON rv.user_id = ui.user_id "
            + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
            + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId) "
            + "AND :userId IS NOT NULL "
            + "AND rv.shop_id = :shopId "
            + "GROUP BY rv.id, ui.id ",
            countQuery = "SELECT COUNT(rv.id) "
                    + "FROM review rv INNER JOIN user_information ui ON ui.user_id = rv.user_id "
                    + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
                    + "AND :userId IS NOT NULL "
                    + "GROUP BY rv.id, ui.id "
                    + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId)",
            nativeQuery = true)
    Page<IReviewResponseDto> shopGetAllReview(Long userId, Long shopId, Pageable pageable);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ReviewDetailResponseDto("
            + "r.id, "
            + "r.content, "
            + "r.title, "
            + "r.createdAt, "
            + "(select count (ufr)from UserFavoriteReview ufr where r.id = ufr.review.id), "
            + "(select count (rp)from Reply rp where r.id = rp.review.id), "
            + "(select (count (ufr) > 0)from UserFavoriteReview ufr where ufr.review.id = r.id and ufr.user.id = :userId), "
            + "ui.user.id, "
            + "CASE WHEN (ui.lastName IS NOT NULL) THEN CONCAT(ui.firstName, ' ', ui.lastName) ELSE ui.firstName END, "
            + "ui.userImage, "
            + "r.shop.id, "
            + "r.shop.name) "
            + "FROM Review r "
            + "LEFT JOIN UserInformation ui ON ui.user.id = r.user.id "
            + "WHERE r.id = :reviewId "
            + "AND r.shop.id = :shopId")
    Optional<ReviewDetailResponseDto> shopGetReviewDetail(Long reviewId, Long userId, Long shopId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.AdminReviewDetailResponseDto("
            + "r.id, "
            + "r.content, "
            + "r.title, "
            + "r.createdAt, "
            + "(select count (ufr)from UserFavoriteReview ufr where r.id = ufr.review.id), "
            + "(select count (rp)from Reply rp where r.id = rp.review.id), "
            + "(select (count (ufr) > 0)from UserFavoriteReview ufr where ufr.review.id = r.id), "
            + "ui.user.id, "
            + "CASE WHEN (ui.lastName IS NOT NULL) THEN CONCAT(ui.firstName, ' ', ui.lastName) ELSE ui.firstName END, "
            + "ui.userImage, "
            + "r.shop.id, "
            + "r.shop.name) "
            + "FROM Review r "
            + "LEFT JOIN UserInformation ui ON ui.user.id = r.user.id "
            + "WHERE r.id = :reviewId")
    Optional<AdminReviewDetailResponseDto> adminGetReviewDetail(Long reviewId);

    @Query(value = "SELECT "
            + "rv.id AS id, "
            + "rv.content AS content, "
            + "rv.title AS title, "
            + "rv.created_at AS createdAt, "
            + "(SELECT DISTINCT COUNT(ufr.id) FROM user_favorite_review ufr WHERE ufr.review_id = rv.id) AS totalFavorite, "
            + "(SELECT (COUNT(r.id)) FROM reply r WHERE r.review_id = rv.id) AS totalReply, "
            + "(SELECT (COUNT(ufr1.id) > 0) FROM user_favorite_review ufr1 WHERE ufr1.review_id = rv.id) AS liked, "
            + "ui.user_id AS userId, "
            + "CASE WHEN (ui.last_name IS NOT NULL) THEN CONCAT(ui.first_name, ' ', ui.last_name) ELSE ui.first_name END AS username, "
            + "ui.user_image as userImage "
            + "FROM review rv "
            + "LEFT JOIN user_information ui ON rv.user_id = ui.user_id "
            + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
            + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId) "
            + "GROUP BY rv.id, ui.id ",
            countQuery = "SELECT COUNT(rv.id) "
                    + "FROM review rv INNER JOIN user_information ui ON ui.user_id = rv.user_id "
                    + "LEFT JOIN user_favorite_review ufr ON rv.id = ufr.review_id "
                    + "GROUP BY rv.id, ui.id "
                    + "WHERE (:shopId IS NULL OR rv.shop_id = :shopId)",
            nativeQuery = true)
    Page<IReviewResponseDto> adminGetAllReview(Long shopId, Pageable pageable);

}