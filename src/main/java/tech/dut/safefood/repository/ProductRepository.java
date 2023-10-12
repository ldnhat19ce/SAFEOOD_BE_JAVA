package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.ShopResponseDto;
import tech.dut.safefood.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByShopId(Long shopId);

    Optional<Product> findByIdAndShopId(Long id, Long shopId);

    Optional<Product> findByIdAndDeleteFlagIsFalse(Long id);

    Optional<Product> findByIdAndShopIdAndDeleteFlagIsFalse(Long id, Long shopId);

    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id , p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.status = :status and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<ProductResponseDto> getAllProductsAndStatus(Pageable pageable, @Param("shopId") Long shopId, @Param("status") String status, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id , p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.status = :status")
    Page<ProductResponseDto> getAllProductsAndStatus(Pageable pageable, @Param("shopId") Long shopId, @Param("status") String status);


    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name,c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%'))")
    Page<ProductResponseDto> getAllProducts(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name,c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.deleteFlag = false")
    Page<ProductResponseDto> getAllProducts(Pageable pageable, @Param("shopId") Long shopId);

    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name, c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id=p.category.id inner join Shop as s on p.shop.id = s.id where s.id = :shopId and p.deleteFlag = false and (:query IS NULL OR p.name like concat('%',:query,'%')) order by p.countPay desc")
    Page<ProductResponseDto> getTopProductShop(Pageable pageable, @Param("shopId") Long shopId, @Param("query") String query);

    @Query(value = "select p from Product as  p where p.id in :ids")
    List<Product> getProductInIds(@Param("ids") List<Long> ids);

    @Query(value = "select new tech.dut.safefood.dto.response.ProductResponseDto(p.id, p.name,p.image,p.description,p.price, p.status, c.name,c.id, p.countPay, s.id, s.name) " + "from Product as p inner join Category as c on c.id = p.category.id inner join Shop as s on p.shop.id = s.id where p.id in :ids and p.deleteFlag = false")
    List<ProductResponseDto> getProductResponseInIds(@Param("ids") List<Long> ids);
}