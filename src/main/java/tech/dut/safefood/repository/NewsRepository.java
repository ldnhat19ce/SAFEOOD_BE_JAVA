package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.dut.safefood.dto.request.AdminNewsDTO;
import tech.dut.safefood.dto.response.UserNewsResponseDto;
import tech.dut.safefood.model.News;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(value = "SELECT new tech.dut.safefood.dto.response.UserNewsResponseDto(" +
            "n.id, " +
            "n.title," +
            "n.location," +
            "n.address," +
            "n.content," +
            "n.createdAt," +
            "n.image," +
            "n.subTitle) " +
            "FROM News n")
    Page<UserNewsResponseDto> userGetPageNews(Pageable pageable);

    @Query(value = "SELECT new tech.dut.safefood.dto.response.UserNewsResponseDto(" +
            "n.id, " +
            "n.title," +
            "n.location," +
            "n.address," +
            "n.content," +
            "n.createdAt," +
            "n.image," +
            "n.subTitle) " +
            "FROM News n where n.id = :id ")
    Optional<UserNewsResponseDto> userGetDetailNewsById(@Param("id") Long id);

    @Query(value = "SELECT new tech.dut.safefood.dto.request.AdminNewsDTO(" +
            "n.id, " +
            "n.title," +
            "n.location," +
            "n.address," +
            "n.content," +
            "n.image," +
            "n.subTitle) " +
            "FROM News n " +
            "WHERE (:query IS NULL OR n.title like concat('%',:query,'%')) ")
    Page<AdminNewsDTO> adminGetPageNews(Pageable pageable, @Param("query") String query);

    @Modifying
    void deleteById(Long id);
}
