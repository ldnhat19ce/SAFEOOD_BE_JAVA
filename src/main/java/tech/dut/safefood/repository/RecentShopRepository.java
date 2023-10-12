package tech.dut.safefood.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.ShopResponseDto;
import tech.dut.safefood.model.RecentShop;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentShopRepository extends JpaRepository<RecentShop, Long> {

    @Query(value = "select new tech.dut.safefood.dto.response.ShopResponseDto(s.id,s.name,s.description, s.phone,s.banner,s.ratings) from RecentShop as rs inner join User as u on rs.user.id = u.id inner join Shop as s on rs.shop.id = s.id where u.id = :userId order by rs.createdAt desc")
    List<ShopResponseDto> getAllRecentShop(@Param("userId") Long userId);


    Optional<RecentShop> findByUserIdAndShopId(Long aLong, Long bLong);
}