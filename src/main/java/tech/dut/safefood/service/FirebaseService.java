package tech.dut.safefood.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.dut.safefood.dto.MetaDataDto;
import tech.dut.safefood.dto.request.NotificationDto;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.model.Reply;
import tech.dut.safefood.model.Review;
import tech.dut.safefood.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FirebaseService {
    private boolean checkUserIsLoginAndDeviceTokenIsExist(User user) {
        return user.getIsLogin() && user.getDeviceToken() != null;
    }

    public void sendUserLikeReviewNotification(Review review, NotificationDto notificationDTO) {
        if (checkUserIsLoginAndDeviceTokenIsExist(review.getUser())) {
            MetaDataDto metaDataDTO = new MetaDataDto();
            metaDataDTO.setTitle(notificationDTO.getTitle());
            metaDataDTO.setContent(notificationDTO.getContent());
            metaDataDTO.setType(CommonEnum.NotificationType.REVIEW.toString());
            metaDataDTO.setTypeId(review.getId().toString());
            metaDataDTO.setTypeName(notificationDTO.getTypeName());
            sendToSingleToken(metaDataDTO, notificationDTO, review.getUser().getDeviceToken());
        }
    }

    public void sendUserCommentReviewNotification(Review review, NotificationDto notificationDTO) {
        if (checkUserIsLoginAndDeviceTokenIsExist(review.getUser())) {
            MetaDataDto metaDataDTO = new MetaDataDto();
            metaDataDTO.setType(CommonEnum.NotificationType.REVIEW.toString());
            metaDataDTO.setTypeId(review.getId().toString());
            metaDataDTO.setTypeName(notificationDTO.getTypeName());
            sendToSingleToken(metaDataDTO, notificationDTO, review.getUser().getDeviceToken());
        }
    }

    public void sendUserLikeReplyNotification(Reply reply, NotificationDto notificationDTO) {
        if (checkUserIsLoginAndDeviceTokenIsExist(reply.getUser())) {
            MetaDataDto metaDataDTO = new MetaDataDto();
            metaDataDTO.setTypeId(reply.getId().toString());
            metaDataDTO.setType(CommonEnum.NotificationType.REVIEW.toString());
            metaDataDTO.setTypeName(notificationDTO.getTypeName());
            sendToSingleToken(metaDataDTO, notificationDTO, reply.getUser().getDeviceToken());
        }
    }

    /**
     * get Instance of generic object and put data
     *
     * @param t              {@link T}
     * @param notificationId {@link String}
     */
    private <T> Map<String, String> getData(T t, Integer notificationId) {
        Map<String, String> data = new HashMap<>();

        MetaDataDto metaDataDTO = new MetaDataDto();
        if (t instanceof MetaDataDto) {
            metaDataDTO = (MetaDataDto) t;
            data.put("metadata", new Gson().toJson(metaDataDTO));
        }
        if (null != notificationId) {
            data.put("notificationId", notificationId.toString());
        }
        return data;
    }

    public <T> void sendToSingleToken(T t, NotificationDto notificationDTO, String deviceToken) {
        Message message = Message.builder()
                .setNotification(new Notification(notificationDTO.getTitle(), notificationDTO.getContent()))
                .putAllData(getData(t, notificationDTO.getId()))
                .setToken(deviceToken)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

}
