package tech.dut.safefood.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tech.dut.safefood.dto.request.AdminNewsDTO;
import tech.dut.safefood.model.News;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    News toEntity(AdminNewsDTO adminNewsDTO);

    AdminNewsDTO toDto(News news);

    List<AdminNewsDTO> toListDto(List<News> news);
}
