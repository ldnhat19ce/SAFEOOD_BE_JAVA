package tech.dut.safefood.service.User;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.safefood.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.safefood.dto.response.VNPayReturnUrlResponseDto;
import tech.dut.safefood.enums.VNPayEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Bill;
import tech.dut.safefood.model.BillItem;
import tech.dut.safefood.model.Payment;
import tech.dut.safefood.repository.BillItemRepository;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.repository.PaymentRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.APIConstants;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.util.constants.VNPayUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserVNPAYService {

    @Value("${app.vn-pay.version}")
    private String vnpVersion;

    @Value("${app.vn-pay.command}")
    private String vnpCommand;

    @Value("${app.vn-pay.tmn-code}")
    private String vnpTmnCode;

    @Value("${app.vn-pay.curr-code}")
    private String vnpCurrCode;

    @Value("${app.vn-pay.pay-url}")
    private String vnpPaymentUrl;

    @Value("${app.vn-pay.hash-secret}")
    private String vnpHashSecret;

    @Value("${email.safefood.time.plusMillis}")
    private Long TIME_PLUS;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillItemRepository billItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(rollbackFor = SafeFoodException.class)
    public VNPayCreateUrlResponseDto createPaymentUrl(VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) throws Exception {
        Bill bill = billRepository.findById(Long.valueOf(requestDTO.getBillId())).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS));
        String ipAdd = VNPayUtil.getIpAddress(request);

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

        String orderInfo = "Thanh toan cho don hang " + requestDTO.getBillId();
        String vnpCreateDate = formatter.format(zonedDateTime);

        String vnpTxnRef = requestDTO.getBillId() + "-" + vnpCreateDate + "-ORDER";

        zonedDateTime = zonedDateTime.plusMinutes(15);
        String vnpExpireDate = formatter.format(zonedDateTime);

        bill.setStatus(Constants.BILL_PENDING);


        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(bill.getTotalPayment().multiply(BigDecimal.valueOf(100L))));
        vnpParams.put("vnp_CurrCode", vnpCurrCode);
        vnpParams.put("vnp_BankCode", requestDTO.getBankCode());
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", requestDTO.getVnpOrderType());
        vnpParams.put("vnp_Locale", requestDTO.getVnpLocale());
        vnpParams.put("vnp_ReturnUrl", requestDTO.getVnpReturnUrl());
        vnpParams.put("vnp_IpAddr", ipAdd);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        ArrayList fieldNames = new ArrayList(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnpPaymentUrl + "?" + queryUrl;
        VNPayCreateUrlResponseDto responseDTO = new VNPayCreateUrlResponseDto();
        responseDTO.setUrl(paymentUrl);
        responseDTO.setBillId(Integer.parseInt(requestDTO.getBillId()));
        return responseDTO;
    }

    public VNPayReturnUrlResponseDto getReturnPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLDecoder.decode(params.nextElement(), StandardCharsets.UTF_8.toString());
            String fieldValue = URLDecoder.decode(request.getParameter(fieldName), StandardCharsets.UTF_8.toString());
            if(fieldName.equals("vnp_OrderInfo")){
                fieldValue = fieldValue.replace(" ","+");
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayUtil.hashAllFields(vnpHashSecret, fields);
        VNPayReturnUrlResponseDto vnPayReturnUrlResponseDTO = new VNPayReturnUrlResponseDto();
        if (signValue.equals(vnpSecureHash)) {
            if (VNPayEnum.PaymentStatus.SUCCESS.getCode().equals(request.getParameter("vnp_ResponseCode"))) {
                vnPayReturnUrlResponseDTO.setResponseCode(VNPayEnum.PaymentStatus.SUCCESS.getCode());
                vnPayReturnUrlResponseDTO.setMessage(VNPayEnum.PaymentStatus.SUCCESS.getMessage());
                vnPayReturnUrlResponseDTO.setSignData(signValue);
            }
        } else {
            vnPayReturnUrlResponseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getCode());
            vnPayReturnUrlResponseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getMessage());
            vnPayReturnUrlResponseDTO.setSignData(signValue);
        }
        return vnPayReturnUrlResponseDTO;
    }

    public VNPayReturnUrlResponseDto getIPNUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                if (fieldName.equals("vnp_OrderInfo")) {
                    fields.put(fieldName, VNPayUtil.getVnpOrderInfoForApp(fieldValue));
                } else {
                    fields.put(fieldName, fieldValue);
                }
            }
        }
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        BigDecimal bigDecimal = BigDecimal.valueOf(Long.parseLong(fields.get("vnp_Amount").toString()) / 100);
        System.out.println(bigDecimal);
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayUtil.hashAllFields(vnpHashSecret, fields);
        VNPayReturnUrlResponseDto responseDTO = new VNPayReturnUrlResponseDto();
        try {
            if (signValue.equals(vnpSecureHash)) {
                //Verify signature OK
                Long billId = Long.valueOf(fields.get("vnp_TxnRef").toString().split("-")[0]);
                System.out.println(billId);
                //Verify order is exist
                boolean checkOrderId = billRepository.existsById(billId);
                //Verify amount
                boolean checkAmount = billRepository.existsByTotalPaymentAndId(BigDecimal.valueOf(Long.parseLong(fields.get("vnp_Amount").toString()) / 100), billId);
                //Verify order status in database
                boolean checkOrderStatus = billRepository.existsByIdAndStatus(billId, Constants.BILL_PENDING);
                System.out.println(checkOrderStatus);
                System.out.println(request.getParameter("vnp_ResponseCode"));
                System.out.println(VNPayEnum.PaymentStatus.SUCCESS.getCode().equals(request.getParameter("vnp_ResponseCode")));
                if (checkOrderId) {
                    if (checkAmount) {
                        if (checkOrderStatus) {
                            if (VNPayEnum.PaymentStatus.SUCCESS.getCode().equals(request.getParameter("vnp_ResponseCode"))) {
                                Bill bill = billRepository.findById(Long.valueOf(billId)).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS));
                                bill.setStatus(Constants.BILL_PAYMENTED);

                                ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

                                bill.setExpiredCode(LocalDateTime.ofInstant(Instant.now().plusMillis(TIME_PLUS), zone));
                                bill.setCode(AppUtils.generateDigitCode());

                                Payment payment = new Payment();
                                payment.setName(Constants.VNPAY);
                                payment.setAmount(bill.getTotalPayment());
                                payment.setTxnref(fields.get("vnp_TxnRef").toString());
                                payment.setResponseCode(request.getParameter("vnp_ResponseCode"));
                                payment.setPaymentInfo(request.getRequestURI().toString());
                                payment.setStatus(Constants.PAYMENT_SUCCESS);
                                paymentRepository.save(payment);
                                bill.setPayment(payment);

                                List<BillItem> billItems = billItemRepository.findAllByBillId(billId);

                                for (BillItem item : billItems) {
                                    item.getProduct().setCountPay(item.getProduct().getCountPay() + item.getAmount());
                                }

                                responseDTO.setResponseCode(request.getParameter("vnp_ResponseCode"));
                                responseDTO.setMessage(APIConstants.VNPAY_PAYMENT_SUCCESSFULLY);
                            } else {
                                Bill bill = billRepository.findById(Long.valueOf(billId)).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_BILL_NOT_EXISTS));
                                bill.setStatus(Constants.BILL_PAYMENTED);

                                Payment payment = new Payment();
                                payment.setName(Constants.VNPAY);
                                payment.getBill().add(bill);
                                payment.setAmount(bill.getTotalPayment());
                                payment.setTxnref(fields.get("vnp_TxnRef").toString());
                                payment.setResponseCode(request.getParameter("vnp_ResponseCode"));
                                payment.setPaymentInfo(request.getRequestURI().toString());
                                payment.setStatus(Constants.PAYMENT_FAIL);
                                paymentRepository.save(payment);

                                responseDTO.setResponseCode(request.getParameter("vnp_ResponseCode"));
                                responseDTO.setMessage(APIConstants.VNPAY_PAYMENT_FAILED);
                            }
                        } else {
                            responseDTO.setResponseCode(VNPayEnum.PaymentStatus.ALREADY_CONFIRM.getCode());
                            responseDTO.setMessage(VNPayEnum.PaymentStatus.ALREADY_CONFIRM.getMessage());
                        }
                    } else {
                        responseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_AMOUNT.getCode());
                        responseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_AMOUNT.getMessage());
                    }
                } else {
                    responseDTO.setResponseCode(VNPayEnum.PaymentStatus.ORDER_NOT_FOUND.getCode());
                    responseDTO.setMessage(VNPayEnum.PaymentStatus.ORDER_NOT_FOUND.getMessage());
                }
            } else {
                responseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getCode());
                responseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getMessage());
            }
        } catch (Exception e) {
            responseDTO.setResponseCode(VNPayEnum.PaymentStatus.UNKNOW_ERR.getCode());
            responseDTO.setMessage(VNPayEnum.PaymentStatus.UNKNOW_ERR.getMessage());
        }
        return responseDTO;
    }
}
