package tech.dut.safefood.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.PricePaymentCreateRequestDto;
import tech.dut.safefood.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.safefood.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Bill;
import tech.dut.safefood.model.Payment;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.repository.PaymentRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UserPricePaymentService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @Value("${email.safefood.time.plusMillis}")
    private Long TIME_PLUS;

    @Transactional(rollbackFor = SafeFoodException.class)
    public void createPaymentPrice(PricePaymentCreateRequestDto requestDTO){
        Bill bill = billRepository.findById(Long.valueOf(requestDTO.getBillId())).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS));
        bill.setStatus(Constants.BILL_PENDING);

        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime expireDate = LocalDateTime.from(AppUtils.instantToLocalDateTime(Instant.now().plusMillis(TIME_PLUS), zone));
        bill.setExpiredCode(expireDate);
        bill.setCode(AppUtils.generateDigitCode());

        Payment payment = new Payment();
        payment.setName(Constants.PRICE);
        payment.getBill().add(bill);
        payment.setAmount(bill.getTotalPayment());
        payment.setStatus(Constants.PAYMENT_PENDING);
        paymentRepository.save(payment);
    }
}
