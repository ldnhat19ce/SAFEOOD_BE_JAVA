package tech.dut.safefood.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.UserResponseDto;
import tech.dut.safefood.enums.AuthProvider;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);

    Optional<User> findByResetToken(String code);

    Optional<User> findFirstByEmailAndStatus(String email, UserEnum.Status userEnum);

    Boolean existsByResetToken(String code);

    Boolean existsByEmailIgnoreCaseAndProvider(String email, AuthProvider authProvider);

    Boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    Optional<User> findFirstByOauthIdAndProvider(String oauthId, AuthProvider authProvider);

    Optional<User> findByShopId(Long shopId);

    @Query(value = "select new tech.dut.safefood.dto.response.UserResponseDto(u.id, u.email, u.provider, u.phoneNumber, u.status, u.createdAt, u.modifiedAt, r.name) " +
            "from User as u inner join Role as r on u.role.id=r.id inner join UserInformation as uf on u.id = uf.user.id where (:query IS NULL OR uf.firstName like concat('%',:query,'%') OR uf.lastName like concat('%',:query,'%')) " +
            " and (:status is null OR u.status = :status)")
    Page<UserResponseDto> getAllUsers(Pageable pageable, @Param("query") String query, @Param("status") UserEnum.Status status);

}
