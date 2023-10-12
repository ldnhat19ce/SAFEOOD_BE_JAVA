package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.model.ReviewImage;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findAllByReviewId(Long reviewId);

    @Query(value = "SELECT ri.imagesUrl FROM ReviewImage ri WHERE ri.review.id = :reviewId")
    List<String> findListStringByReviewId(Long reviewId);

    List<ReviewImage> findAllByReviewIdIn(List<Long> reviewIds);
}