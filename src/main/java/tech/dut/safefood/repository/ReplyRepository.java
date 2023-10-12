package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.request.UserDto;
import tech.dut.safefood.dto.response.ReplyResponseDto;
import tech.dut.safefood.model.Reply;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByReviewId(Long reviewId);

    Optional<Reply> findByIdAndReviewId(Long id, Long reviewId);

    @Query(value = "SELECT rl FROM Reply rl " +
            "INNER JOIN Review rv ON rl.review.id = rv.id " +
            "INNER JOIN Shop s ON rv.shop.id = s.id " +
            "WHERE rl.id = :id " +
            "AND rv.shop.id = :shopId")
    Optional<Reply> shopGetReply(Long id, Long shopId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ReplyResponseDto("
            + "rl.id, "
            + "rl.createdAt, "
            + "rl.modifiedAt, "
            + "rl.user.id, "
            + "CASE WHEN (ui.lastName IS NOT NULL) THEN CONCAT(ui.firstName, ' ', ui.lastName) ELSE ui.firstName END, "
            + "ui.userImage, "
            + "rl.content, "
            + "(select count (ufr)from UserFavoriteReply ufr where rl.id = ufr.reply.id), "
            + "(select (count (ufr) > 0)from UserFavoriteReply ufr where ufr.reply.id = rl.id and ufr.user.id = :userId)) "
            + "FROM Reply rl "
            + "LEFT JOIN UserInformation ui ON ui.user.id = rl.user.id "
            + "WHERE rl.review.id = :reviewId")
    List<ReplyResponseDto> userGetAllReplyByReview(Long reviewId, Long userId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ReplyResponseDto (" +
            "r.id," +
            "r.createdAt," +
            "r.modifiedAt," +
            "r.user.id," +
            "CASE WHEN (ui.lastName IS NOT NULL) THEN CONCAT(ui.firstName, ' ', ui.lastName) ELSE ui.firstName END, " +
            "ui.userImage," +
            "r.content," +
            "(select (count(ufr.id))from UserFavoriteReply ufr where ufr.reply.id = r.id)," +
            "(select (count(ufr.id) > 0)from UserFavoriteReply ufr where ufr.reply.id = r.id and ufr.user.id = :userId), " +
            "r.review.id) " +
            "from Reply r " +
            "left join User u on u.id = r.user.id " +
            "left join UserInformation ui on ui.user.id = u.id " +
            "where r.review.id in :reviewIds " +
            "AND r.user.id = :userId " +
            "order by r.createdAt ")
    List<ReplyResponseDto> findReplyByCurrentUser(Long userId, List<Long> reviewIds);

    @Query(value = "SELECT new tech.dut.safefood.dto.request.UserDto("
            + "uf.user.id, "
            + "CASE WHEN (uf.user.deviceToken IS NOT NULL) THEN uf.user.deviceToken ELSE '' END, "
            + "CASE WHEN (uf.lastName IS NOT NULL) THEN CONCAT(uf.firstName, ' ', uf.lastName) ELSE uf.firstName END) "
            + "FROM Reply rl "
            + "LEFT JOIN UserInformation uf ON rl.user.id = uf.user.id "
            + "WHERE rl.review.id = :reviewId "
            + "GROUP BY rl.user.id, uf.firstName, uf.lastName, rl.createdAt "
            + "ORDER BY rl.createdAt DESC")
    List<UserDto> findReplyByReviewId(Long reviewId);

}