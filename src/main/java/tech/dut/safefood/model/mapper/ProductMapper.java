package tech.dut.safefood.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tech.dut.safefood.dto.request.ProductRequestDto;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.model.Product;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequestDto productDto);

    @Mapping(source = "product.category.name", target = "categoryName")
    ProductResponseDto toDtoResponse(Product product);

    @Mapping(source = "product.category.id", target = "categoryId")
    ProductRequestDto toDtoRequest(Product product);

    List<ProductResponseDto> toListResponseDto(List<Product> products);
}
