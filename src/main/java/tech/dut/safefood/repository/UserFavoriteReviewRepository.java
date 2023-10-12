package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.request.UserDto;
import tech.dut.safefood.model.UserFavoriteReview;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteReviewRepository extends JpaRepository<UserFavoriteReview, Long> {
    Optional<UserFavoriteReview> findByUserIdAndReviewId(Long userId, Long reviewId);

    List<UserFavoriteReview> findAllByUserIdAndReviewId(Long userId, Long reviewId);

    List<UserFavoriteReview> findAllByReviewId(Long reviewId);

    @Query(value = "SELECT new tech.dut.safefood.dto.request.UserDto(" +
            "uf.user.id, " +
            "CASE WHEN (uf.user.deviceToken IS NOT NULL) THEN uf.user.deviceToken ELSE '' END, " +
            "CASE WHEN (uf.lastName IS NOT NULL) THEN CONCAT(uf.firstName, ' ', uf.lastName) ELSE uf.firstName END) " +
            "FROM UserFavoriteReview ufr LEFT JOIN UserInformation uf ON ufr.user.id = uf.user.id " +
            "WHERE ufr.review.id = :reviewId ORDER BY ufr.createdAt DESC")
    List<UserDto> findUserDtoByReviewId(Long reviewId);
}
