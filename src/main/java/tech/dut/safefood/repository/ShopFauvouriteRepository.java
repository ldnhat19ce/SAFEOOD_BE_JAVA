package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.ShopFauvoriteResponseDto;
import tech.dut.safefood.dto.response.ShopResponseDto;
import tech.dut.safefood.model.ShopFauvourite;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopFauvouriteRepository extends JpaRepository<ShopFauvourite, Long> {

    Optional<ShopFauvourite> findByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ShopFauvoriteResponseDto(s.id, s.name, s.banner, s.description, s.scheduleActive, s.phone) FROM ShopFauvourite as f inner join User as u on f.user.id = u.id inner join Shop  as s on f.shop.id = s.id where u.id = :userId order by s.id ")
    List<ShopFauvoriteResponseDto> getAllShopFauvorite(@Param("userId") Long userId);

    Optional<ShopFauvourite> findByShopIdAndUserId(Long shopId, Long userId);

    Boolean existsByUserIdAndShopId(Long userId, Long shopId);
}