package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.dto.response.VoucherUserResponseDto;
import tech.dut.safefood.model.Voucher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.deleteFlag, v.image, v.description, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on v.shop.id = s.id where (:query IS NULL OR v.name like concat('%',:query,'%')) and s.id = :shopId")
    Page<VoucherResponseDto> getAllVouchers(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.deleteFlag, v.image, v.description, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on v.shop.id = s.id where s.id = :shopId and v.deleteFlag =false")
    Page<VoucherResponseDto> getAllVouchers(Pageable pageable, @Param("shopId") Long shopId);

    Optional<Voucher> findByIdAndShopId(Long id, Long shopId);

    Optional<Voucher> findByIdAndShopIdAndDeleteFlagIsFalse(Long id, Long shopId);

    Optional<Voucher> findByIdAndDeleteFlagIsFalseAndShopId(Long id, Long shopId);


    Optional<Voucher> findByIdAndUserType(Long voucherId, String userType);

    Optional<Voucher> findByIdAndUserTypeAndDeleteFlagIsFalse(Long voucherId, String userType);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.deleteFlag, v.image, v.description) " +
            "from Voucher as v inner join Shop as s on s.id = v.shop.id where s.id = :id and v.deleteFlag = false and (:query is null or v.name like concat('%',:query,'%'))")
    Page<VoucherResponseDto> getVoucherByShopId(Pageable pageable,@Param("id") Long id, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.deleteFlag, v.image, v.description) " +
            "from Voucher as v where (:query is null or v.name like concat('%',:query,'%')) and v.userType = :userType")
    Page<VoucherResponseDto> getAllVoucherByAdmin(Pageable pageable, @Param("userType") String userType, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.deleteFlag, v.image, v.description) " +
            "from Voucher as v where v.userType = :userType and v.id =:id")
    VoucherResponseDto getDetailVoucherByAdmin(@Param("userType") String userType, @Param("id") Long id);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity,v.deleteFlag, v.image, v.description, s.id, s.name) " +
            "from Voucher as v inner join Shop as s on s.id = v.shop.id where v.id =:id")
    VoucherResponseDto getDetailVoucherByAdmin(@Param("id") Long id);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.image, v.description, v.deleteFlag) " +
            "from Voucher as v where v.id =:id and v.deleteFlag = false")
    Optional<VoucherResponseDto> getDetailVoucherById(@Param("id") Long id);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherUserResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.image, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, s.id, s.name, v.description, v.deleteFlag)  from Voucher as v left join Shop as s on v.shop.id = s.id where (s.id = :shopId OR v.userType = :userType) and  v.valueNeed <= :totalOrigin and v.deleteFlag = false order by v.id desc")
    List<VoucherUserResponseDto> getAllVoucherUser(@Param("shopId") Long shopId, @Param("totalOrigin") BigDecimal totalOrigin, @Param("userType") String userType);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherUserResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.image, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, s.id, s.name, v.description, v.deleteFlag)  from Voucher as v left join Shop as s on v.shop.id = s.id where (:userType is null or v.userType LIKE concat('%',:userType,'%')) and v.deleteFlag = false and (:query is null or v.name like concat('%',:query,'%'))")
    Page<VoucherUserResponseDto> getAllVoucher(Pageable pageable,@Param("userType") String userType, @Param("query") String query);

}