package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.CategoryDto;
import tech.dut.safefood.dto.response.UserResponseDto;
import tech.dut.safefood.model.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);


    @Query(value = "select new tech.dut.safefood.dto.CategoryDto(c.id, c.name ,c.deleteFlag) " +
            "from Category as c where c.deleteFlag = false and (:query IS NULL OR c.name like concat('%',:query,'%'))")
    Page<CategoryDto> getAllCategories(Pageable pageable, @Param("query") String query);

    @Query(value = "select new tech.dut.safefood.dto.CategoryDto(c.id, c.name ,c.deleteFlag) " +
            "from Category as c where c.deleteFlag = false")
    Page<CategoryDto> getAllCategories(Pageable pageable);

    Optional<Category> findByIdAndDeleteFlagIsFalse(Long aLong);
}