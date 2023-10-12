package tech.dut.safefood.configurator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.model.Bill;
import tech.dut.safefood.model.Voucher;
import tech.dut.safefood.repository.BillRepository;
import tech.dut.safefood.repository.VoucherRepository;
import tech.dut.safefood.util.constants.Constants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SafeFoodSchedule {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private BillRepository billRepository;

    @Scheduled(cron = "0 0 0,12 * * *", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void scheduleVoucher(){
        List<Voucher> vouchers = voucherRepository.findAll();
        for(Voucher voucher : vouchers){
            if(voucher.getEndedAt().isBefore(LocalDateTime.now())){
                voucher.setDeleteFlag(true);
            }
        }
    }

    @Scheduled(cron = "0 0 0,12 * * *", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void scheduleBill(){
        List<Bill> bills = billRepository.findAll();
        for(Bill b : bills){
           if(b.getExpiredCode().isBefore(LocalDateTime.now())){
               b.setStatus(Constants.BILL_FAIL);
           }
        }
    }

}
