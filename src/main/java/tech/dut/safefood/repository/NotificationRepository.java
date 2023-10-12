package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.NotificationResponseDto;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "SELECT new tech.dut.safefood.dto.response.NotificationResponseDto"
            + " (n.title, n.content, n.seenFlag, n.createdAt, n.typeId, n.type)"
            + " FROM Notification n "
            + " WHERE n.toUser.id = :currentUserId "
            + " AND (COALESCE(:types) IS NULL OR n.type IN (:types)) "
            + " ORDER BY n.createdAt DESC ")
    Page<NotificationResponseDto> getAllNotificationByCurrentUserId(Long currentUserId, List<CommonEnum.NotificationType> types, Pageable pageable);

    @Query(value = "SELECT n FROM Notification n"
            + " WHERE n.id IN (:notificationIds)"
            + " AND n.toUser.id = :currentUserId "
            + " AND n.seenFlag = FALSE")
    List<Notification> findALLByIdIn(List<Long> notificationIds, Long currentUserId);
}
