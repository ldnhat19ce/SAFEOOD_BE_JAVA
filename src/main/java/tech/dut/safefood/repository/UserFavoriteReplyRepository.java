package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.request.UserDto;
import tech.dut.safefood.model.UserFavoriteReply;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteReplyRepository extends JpaRepository<UserFavoriteReply, Long> {
    Optional<UserFavoriteReply> findByUserIdAndReplyId(Long userId, Long replyId);

    List<UserFavoriteReply> findAllByUserIdAndReplyId(Long userId, Long replyId);

    List<UserFavoriteReply> findAllByReplyIdIn(List<Long> replyIds);

    List<UserFavoriteReply> findAllByReplyId(Long replyId);

    @Query(value = "SELECT new tech.dut.safefood.dto.request.UserDto(" +
            "uf.user.id, " +
            "CASE WHEN (uf.user.deviceToken IS NOT NULL) THEN " +
            "uf.user.deviceToken ELSE '' END, " +
            "CASE WHEN (uf.lastName IS NOT NULL) THEN " +
            "CONCAT(uf.firstName, ' ', uf.lastName) ELSE uf.firstName END) " +
            "FROM UserFavoriteReply ufr " +
            "LEFT JOIN UserInformation uf " +
            "ON ufr.user.id = uf.user.id " +
            "WHERE ufr.reply.id = :replyId " +
            "ORDER BY ufr.createdAt DESC")
    List<UserDto> findUserDtoByReplyId(Long replyId);
}
