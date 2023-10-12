package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.model.Payment;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Boolean existsByTxnrefAndBillId(String txnref, Long billId);

    Boolean existsByAmountAndBillId(BigDecimal amount, Long billId);

    Boolean existsByBillIdAndBillStatus(Long billId, String billStatus);

    Optional<Payment> findByBillId(Long aLong);
}