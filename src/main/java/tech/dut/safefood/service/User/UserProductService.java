package tech.dut.safefood.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.repository.ProductRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

@Service
public class UserProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public PageInfo<ProductResponseDto> getTopProductShop(Integer page, Integer limit, Long shopId, String query) throws SafeFoodException {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        return AppUtils.pagingResponse(productRepository.getTopProductShop(pageable, shopId, query));
    }

    @Transactional(readOnly = true)
    public PageInfo<ProductResponseDto> getProductShop(Integer page, Integer limit, Long shopId, String query) throws SafeFoodException {
        Pageable pageable = AppUtils.buildPageable(page, limit);
        return AppUtils.pagingResponse(productRepository.getAllProducts(pageable, shopId, query));
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getDetail(Long id) throws SafeFoodException {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        if (!productRepository.existsById(id)) {
            throw new SafeFoodException(SafeFoodException.ERROR_PRODUCT_NOT_EXISTS);
        }

        productRepository.findByIdAndDeleteFlagIsFalse(id).ifPresentOrElse((item) ->
        {
            productResponseDto.setId(item.getId());
            productResponseDto.setCategoryName(item.getCategory().getName());
            productResponseDto.setName(item.getName());
            productResponseDto.setDescription(item.getDescription());
            productResponseDto.setImage(item.getImage());
            productResponseDto.setStatus(item.getStatus());
            productResponseDto.setPrice(item.getPrice());
            productResponseDto.setCountPay(item.getCountPay());
        }, () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT));
        return productResponseDto;
    }

}
