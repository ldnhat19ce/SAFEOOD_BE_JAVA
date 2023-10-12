package tech.dut.safefood.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.ShopDetailResponseDto;
import tech.dut.safefood.dto.response.ShopResponseDto;
import tech.dut.safefood.dto.response.UserResponseDto;
import tech.dut.safefood.enums.ShopEnum;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Role;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.User;
import tech.dut.safefood.model.mapper.UserMapper;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.service.EmailSenderService;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminManagerService {
    @Autowired
    private UserRepository userRepository;

    private final EmailSenderService emailSenderService;

    private final TemplateEngine templateEngine;

    @Autowired
    private ShopRepository shopRepository;

    @Value("${email.safefood.from}")
    private String SEND_MAIL_FROM;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void blockUser(Long userId) throws SafeFoodException {
        User user = userRepository.findById(userId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        if (!user.getStatus().equals(UserEnum.Status.ACTIVED)) {
            throw new SafeFoodException(SafeFoodException.USER_IS_INACTIVED);
        }
        user.setStatus(UserEnum.Status.BLOCKED);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void rollBackBlockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        if (!user.getStatus().equals(UserEnum.Status.BLOCKED)) {
            throw new SafeFoodException(SafeFoodException.USER_IS_NOT_BLOCK);
        }
        user.setStatus(UserEnum.Status.ACTIVED);
        user.setExpiredTime(null);
        user.setResetToken(null);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void blockShop(Long shopId) throws SafeFoodException {
        User user = userRepository.findByShopId(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        if (!user.getStatus().equals(UserEnum.Status.ACTIVED)) {
            throw new SafeFoodException(SafeFoodException.USER_IS_INACTIVED);
        }
        user.setStatus(UserEnum.Status.BLOCKED);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void activeShop(Long shopId) throws SafeFoodException {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_FOUND));
        User user = userRepository.findByShopId(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        String name = user.getShop().getName();
        sendNameToMail(user.getEmail().toLowerCase(), name, Constants.ACTIVE_SHOP, Constants.TEMPLATE_ACTIVE_SHOP);
        user.setStatus(UserEnum.Status.ACTIVED);
        shop.setScheduleActive(Constants.SHOP_SCHEDULE_CLOSE);
    }

    private void sendNameToMail(String recipientEmail, String name, String subject, String template) throws SafeFoodException {
        try {
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("name", name);
            context.setVariables(model);
            String content = templateEngine.process(template, context);
            emailSenderService.sendEmail(subject, new InternetAddress(SEND_MAIL_FROM, Constants.EMAIL_SEND_FROM_DA_PASS_NAME), recipientEmail, content);
        } catch (Exception e) {
            throw new SafeFoodException(SafeFoodException.ERROR_CAN_NOT_SEND_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public PageInfo<ShopResponseDto> getAllShops(Integer page, Integer limit, UserEnum.Status status, String query) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        if(status != null){
            return AppUtils.pagingResponse(shopRepository.getAllShop(pageable, status, query));
        }
        return AppUtils.pagingResponse(shopRepository.getAllShop(pageable, query));
    }

    @Transactional(readOnly = true)
    public PageInfo<UserResponseDto> getAllListUser(Integer page, Integer limit, String query, UserEnum.Status status) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        return AppUtils.pagingResponse(userRepository.getAllUsers(pageable, query,status));
    }

    @Transactional(readOnly = true)
    public ShopDetailResponseDto getDetailShop(Long id) throws SafeFoodException {
        ShopDetailResponseDto shopDetailResponseDto = new ShopDetailResponseDto();
        shopRepository.findById(id).ifPresentOrElse((item) -> {
            shopDetailResponseDto.setId(id);
            shopDetailResponseDto.setName(item.getName());
            shopDetailResponseDto.setBanner(item.getBanner());
            shopDetailResponseDto.setDescription(item.getDescription());
        }, () -> new SafeFoodException((SafeFoodException.ERROR_SHOP_NOT_FOUND)));
        return shopDetailResponseDto;
    }


}
