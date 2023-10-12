package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.model.VoucherUser;

import java.util.Optional;

@Repository
public interface VoucherUserRepository extends JpaRepository<VoucherUser, Long> {

    Boolean existsByUserIdAndVoucherId(Long userId, Long voucherId);

    Optional<VoucherUser> findByUserIdAndVoucherId(Long userId, Long voucherId);

}