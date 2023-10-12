package tech.dut.safefood.service.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.ProductRequestDto;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Category;
import tech.dut.safefood.model.Product;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.model.Voucher;
import tech.dut.safefood.model.mapper.ProductMapper;
import tech.dut.safefood.repository.CategoryRepository;
import tech.dut.safefood.repository.ProductRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;

@Service
@RequiredArgsConstructor
public class ShopManagerProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AppUtils appUtils;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void createProduct(ProductRequestDto productRequestDto) throws SafeFoodException {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));

        Category category = categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_CATEGORY_IS_NOT_EXISTS));

        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setImage(productRequestDto.getImage());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setShop(shop);
        product.setCategory(category);
        product.setStatus(productRequestDto.getStatus());

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public PageInfo<ProductResponseDto> getAllProducts(Integer page, Integer limit, String status, String query) throws SafeFoodException {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        if (status == null || status.isEmpty()) {
            return AppUtils.pagingResponse(productRepository.getAllProducts(pageable, shop.getId(), query));

        }

        return AppUtils.pagingResponse(productRepository.getAllProductsAndStatus(pageable, shop.getId(), status, query));
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void deleteProduct(Long productId) throws SafeFoodException {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        productRepository.findByIdAndShopId(productId, shop.getId()).ifPresentOrElse((item) -> {
            item.setDeleteFlag(true);
            item.setStatus(Constants.PRODUCT_STATUS_DELETED);
        }, () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT));
    }



    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void updateProduct(ProductRequestDto productRequestDto) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        productRepository.findByIdAndShopIdAndDeleteFlagIsFalse(productRequestDto.getId(), shop.getId()).ifPresentOrElse((item) -> {
            Category category = categoryRepository.findById(productRequestDto.getCategoryId()).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_CATEGORY_IS_NOT_EXISTS));
            item.setName(productRequestDto.getName());
            item.setImage(productRequestDto.getImage());
            item.setStatus(productRequestDto.getStatus());
            item.setDescription(productRequestDto.getDescription());
            item.setPrice(productRequestDto.getPrice());
            item.setCategory(category);
        }, () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT));
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getDetail(Long id) throws SafeFoodException {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        ProductResponseDto productResponseDto = new ProductResponseDto();
        if (!productRepository.existsById(id)) {
            throw new SafeFoodException(SafeFoodException.ERROR_PRODUCT_NOT_EXISTS);
        }

        productRepository.findByIdAndShopIdAndDeleteFlagIsFalse(id, shop.getId()).ifPresentOrElse((item) ->
        {
            productResponseDto.setId(item.getId());
            productResponseDto.setCategoryName(item.getCategory().getName());
            productResponseDto.setName(item.getName());
            productResponseDto.setDescription(item.getDescription());
            productResponseDto.setImage(item.getImage());
            productResponseDto.setStatus(item.getStatus());
            productResponseDto.setPrice(item.getPrice());
            productResponseDto.setCountPay(item.getCountPay());
            productResponseDto.setCategoryId(item.getCategory().getId());
            productResponseDto.setCategoryName(item.getCategory().getName());
            productResponseDto.setShopId(item.getShop().getId());
            productResponseDto.setShopName(item.getShop().getName());
        }, () -> new SafeFoodException(SafeFoodException.ERROR_SHOP_DONT_HAVE_THIS_PRODUCT));
        return productResponseDto;
    }
}
