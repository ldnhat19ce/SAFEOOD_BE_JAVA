package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}