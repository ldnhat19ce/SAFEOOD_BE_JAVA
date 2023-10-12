package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.dut.safefood.dto.response.BillItemResponseDto;
import tech.dut.safefood.dto.response.TopProductResponseDto;
import tech.dut.safefood.dto.response.TurnoverProductResponseDto;
import tech.dut.safefood.model.BillItem;

import java.time.LocalDateTime;
import java.util.List;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {

    @Query(value = "SELECT new tech.dut.safefood.dto.response.TurnoverProductResponseDto(p.id, p.name, sum(bitem.amount) , (sum(bitem.amount) * p.price))" +
            "FROM BillItem as bitem inner join Product as p on bitem.product.id  = p.id inner join Shop as s on s.id = bitem.shop.id " +
            "where s.id = :shopId and ( :dateFrom IS NULL OR bitem.createdAt >= :dateFrom) and ( :dateTo  IS NULL OR bitem.createdAt <= :dateTo) " +
            "group by p.id")
    List<TurnoverProductResponseDto> getAllProductPurchase(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("shopId") Long shopId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.TopProductResponseDto(p.id, p.name, sum(bitem.amount) , p.image , p.price) FROM " +
            "BillItem as bitem inner join Shop as s on bitem.shop.id = s.id inner join Bill as b on bitem.bill.id = b.id inner join Product as p on bitem.product.id = p.id " +
            "where s.id = :shopId and (:dateFrom IS NULL OR bitem.createdAt >= :dateFrom) and (:dateTo IS NULL OR bitem.createdAt <= :dateTo) group by p.id")
    Page<TopProductResponseDto> getTopProduct(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("shopId") Long shopId, Pageable pageable);

    List<BillItem> findAllByBillId(Long billId);

    @Query(value = "select new tech.dut.safefood.dto.response.BillItemResponseDto(p.name, bi.amount, p.price, s.name, c.name, p.image) from BillItem as bi inner join Bill as b on b.id = bi.bill.id inner join" +
            " Product as p on bi.product.id = p.id inner join Category as c on c.id = p.category.id inner join Shop as s " +
            " on s.id = bi.shop.id where b.id = :billId")
    List<BillItemResponseDto> getAllBillItemByBillId(@Param("billId") Long billId);

    List<BillItem> findByBillId(Long billId);

}