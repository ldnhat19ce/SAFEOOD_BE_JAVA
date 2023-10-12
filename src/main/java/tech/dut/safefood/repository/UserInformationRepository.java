package tech.dut.safefood.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.dut.safefood.dto.UserProfileDto;
import tech.dut.safefood.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {

    @Query(value = "select new tech.dut.safefood.dto.UserProfileDto(uf.id, uf.firstName, uf.lastName, uf.gender, uf.birthday, uf.userImage ) from User as u inner join" +
            " UserInformation as uf on u.id = uf.user.id where u.id = :userId")
    UserProfileDto getProfileUser(@Param("userId") Long userId);
}
