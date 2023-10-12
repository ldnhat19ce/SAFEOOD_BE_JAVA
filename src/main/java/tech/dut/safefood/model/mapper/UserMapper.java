package tech.dut.safefood.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tech.dut.safefood.dto.response.UserResponseDto;
import tech.dut.safefood.model.User;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "user.role.name", target = "role")
    UserResponseDto toDto(User user);

    List<UserResponseDto> toListDto(List<User> users);
}
