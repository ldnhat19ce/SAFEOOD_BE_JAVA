package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.UpdateSeenFlagRequestDto;
import tech.dut.safefood.dto.response.NotificationResponseDto;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Notification;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.NotificationRepository;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public PageInfo<NotificationResponseDto> getAllNotification(Integer page, Integer limit, List<CommonEnum.NotificationType> types) {
        Long currentUserId = getCurrentUser().get().getId();

        Pageable pageable = AppUtils.buildPageable(page, limit);
        Page<NotificationResponseDto> responseDTOS = notificationRepository.getAllNotificationByCurrentUserId(currentUserId, types, pageable);
        return AppUtils.pagingResponse(responseDTOS);
    }

    public void updateSeenFlagNotification(UpdateSeenFlagRequestDto requestDto) {
        Long currentUserId = getCurrentUser().get().getId();
        List<Notification> notifications = notificationRepository.findALLByIdIn(requestDto.getNotificationIds(), currentUserId);
        notifications.forEach(n -> n.setSeenFlag(true));
        notificationRepository.saveAll(notifications);
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
}
