package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.response.ShopRatingsResponseDto;
import tech.dut.safefood.model.Ratings;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, Long> {

    @Query(value = "select new tech.dut.safefood.dto.response.ShopRatingsResponseDto(u.id,uf.firstName, uf.lastName, uf.userImage, r.content, r.ratings) from Ratings as r inner join User as u on r.user.id = u.id inner join Shop as s on s.id = r.shop.id inner join UserInformation as uf on u.id = uf.user.id where s.id = :shopId order by r.id desc ")
    public List<ShopRatingsResponseDto> getAllRatingShop(@Param("shopId") Long shopId);
    
}
