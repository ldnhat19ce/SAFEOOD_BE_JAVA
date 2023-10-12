package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.BillEnum;
import tech.dut.safefood.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT new tech.dut.safefood.dto.response.TurnoverTotalResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment)) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) group by s.id")
    TurnoverTotalResponseDto getTotalTurnover(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("shopId") Long shopId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.TurnoverTotalShopResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment), s.name, s.id, s.banner, s.description, s.ratings) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo) group by s.id")
    List<TurnoverTotalShopResponseDto> getAllTotalTurnoverShop(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);


    @Query(value = "SELECT new tech.dut.safefood.dto.response.TurnoverTotalShopResponseDto(sum(b.totalOrigin) , sum(b.totalVoucher) , sum(b.totalPayment), s.name, s.id, s.banner, s.description, s.ratings) FROM Bill as b inner join Shop as s on s.id = b.shop.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR b.createdAt >= :dateFrom) and (:dateTo IS NULL OR b.createdAt <= :dateTo)  group by s.id")
    TurnoverTotalShopResponseDto getTurnoverShop(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("shopId") Long shopId);

    Optional<Bill> findByUserIdAndStatus(Long userId, String status);

    @Query(value = "select new tech.dut.safefood.dto.response.BillUserResponseDto(b.id,b.totalOrigin,b.totalPayment,b.totalVoucher, b.createdAt, b.status, s.id, s.name, s.banner, a.id, a.city, a.district, a.town, a.street, a.x, a.y, b.isRating, b.ratings) from Bill as b inner join User as u on b.user.id = u.id inner join Shop as s on b.shop.id = s.id inner join Address as a on s.addresses.id = a.id where u.id = :userId and (:status is null or b.status = :status)")
    List<BillUserResponseDto> getAllByUserIdAndStatus(@Param("userId") Long id, @Param("status") String status);

    @Query(value = "select new tech.dut.safefood.dto.response.BillUserResponseDto(b.id,b.totalOrigin,b.totalPayment,b.totalVoucher, u.id, uf.firstName, uf.lastName, b.createdAt, b.status) from Bill as b inner join Shop as s on b.shop.id = s.id inner join User as u on u.id = b.user.id inner join UserInformation as uf on u.userInformation.id = uf.id where s.id = :shopId and (:status  is null or b.status = :status)")
    List<BillUserResponseDto> getAllByShopId(@Param("shopId") Long id, @Param("status") String status);

    Boolean existsByTotalPaymentAndId(BigDecimal amount, Long id);
    Optional<Bill> findByCode(String code);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.image, v.description, s.id, s.name, count(v.id), v.deleteFlag) from Bill as b inner join Voucher as v on b.voucher.id = v.id inner join Shop as s on s.id = v.shop.id where v.userType = 'shop' and (:query is null or v.name like concat('%',:query,'%')) and v.deleteFlag = false group by v.id")
    Page<VoucherResponseDto> getTopVoucherShop(Pageable pageable , @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.VoucherResponseDto(v.id, v.name, v.createdAt, v.endedAt, v.userType, v.valueDiscount, v.voucherType, v.valueNeed, v.limitPerUser, v.maxDiscount, v.quantity, v.image, v.description, count(v.id), v.deleteFlag) from Bill as b inner join Voucher as v on b.voucher.id = v.id inner join Shop as s on s.id = v.shop.id where v.userType = 'admin' and v.deleteFlag = false and (:query is null or v.name like concat('%',:query,'%')) group by v.id")
    Page<VoucherResponseDto> getTopVoucherAdmin(Pageable pageable , @Param("query") String query);

    Boolean existsByIdAndStatus(Long billId, String billStatus);
}