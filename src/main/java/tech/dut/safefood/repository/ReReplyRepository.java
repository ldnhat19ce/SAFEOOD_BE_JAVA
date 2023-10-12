package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.model.ReReply;

@Repository
public interface ReReplyRepository extends JpaRepository<ReReply, Long> {
}