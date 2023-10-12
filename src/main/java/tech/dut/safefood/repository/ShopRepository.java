package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.ShopLocationResponseDTO;
import tech.dut.safefood.dto.response.ShopResponseDto;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.model.Shop;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByPhoneIgnoreCase(String phoneNumber);

    @Query(value = "select new tech.dut.safefood.dto.response.ShopResponseDto(s.id, s.name,u.status,s.description,s.phone, s.banner, s.ratings) " +
            "from User as u inner join Shop as s on u.id=s.user.id and (:query IS NULL OR s.name like concat('%',:query,'%')) ")
    Page<ShopResponseDto> getAllShop(Pageable pageable, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.ShopResponseDto(s.id, s.name,u.status,s.description,s.phone,s.banner, s.ratings) " +
            "from User as u inner join Shop as s on u.id=s.user.id where u.status = :status and (:query IS NULL OR s.name like concat('%',:query,'%'))")
    Page<ShopResponseDto> getAllShop(Pageable pageable, @Param("status") UserEnum.Status status, @Param("query") String query);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.ShopLocationResponseDTO(" +
            "s.id," +
            "s.name," +
            "s.description," +
            "s.banner," +
            "a.x, a.y," +
            "a.street, s.ratings) FROM Shop as s inner join Address as a on s.addresses.id = a.id inner join User as u on u.id = s.user.id" +
            " WHERE u.status = 'ACTIVED' and distance_shop(a.id, :x, :y) <= :radius")
    Page<ShopLocationResponseDTO> findAllShopLocationByName(@Param("radius") Double radius,@Param("x") Double x, @Param("y") Double y, Pageable pageable);


    @Query(value = "SELECT new tech.dut.safefood.dto.response.ShopLocationResponseDTO(" +
            "s.id," +
            "s.name," +
            "s.description," +
            "s.banner," +
            "a.x, a.y," +
            "a.street, s.ratings) FROM Shop as s inner join Address as a on s.addresses.id = a.id inner join User as u on s.user.id = u.id " +
            "WHERE u.status = 'ACTIVED' and s.id <> :shopId and distance_shop(a.id,a.x, a.y) <= :radius ")
    Page<ShopLocationResponseDTO> findAllShopLocationByName(@Param("radius") Double radius,@Param("shopId") Long shopId,  Pageable pageable);

    Optional<Shop> findByUserId(Long userId);
}