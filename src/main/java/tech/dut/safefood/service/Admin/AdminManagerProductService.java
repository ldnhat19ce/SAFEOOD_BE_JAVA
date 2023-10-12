package tech.dut.safefood.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.CategoryDto;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.ShopDetailResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Category;
import tech.dut.safefood.model.Shop;
import tech.dut.safefood.repository.CategoryRepository;
import tech.dut.safefood.repository.ProductRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminManagerProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void createCategory(CategoryDto categoryDto) throws SafeFoodException {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new SafeFoodException(SafeFoodException.ERROR_CATEGORY_IS_EXISTS);
        }
        Category category = new Category();
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void deleteCategory(Long categoryId) throws SafeFoodException {
        categoryRepository.findById(categoryId).ifPresentOrElse((item) -> {
            item.setDeleteFlag(true);
            item.getProducts().stream().forEach(p -> p.setDeleteFlag(true));
        }, () -> new SafeFoodException(SafeFoodException.ERROR_CATEGORY_IS_NOT_EXISTS));
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void updateCategory(CategoryDto categoryDto) throws SafeFoodException {
        categoryRepository.findByIdAndDeleteFlagIsFalse(categoryDto.getId()).ifPresentOrElse((item) ->
                        item.setName(categoryDto.getName()),
                () -> new SafeFoodException(SafeFoodException.ERROR_CATEGORY_IS_NOT_EXISTS));
    }

    @Transactional(readOnly = true)
    public PageInfo<CategoryDto> getAllCategory(Integer page, Integer limit, String query) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        return AppUtils.pagingResponse(categoryRepository.getAllCategories(pageable, query));
    }

    @Transactional(readOnly = true)
    public CategoryDto getDetailCategory(Long id) throws SafeFoodException {
        CategoryDto categoryDto = new CategoryDto();
        categoryRepository.findByIdAndDeleteFlagIsFalse(id).ifPresentOrElse((item) -> {
            categoryDto.setId(id);
            categoryDto.setName(item.getName());
            categoryDto.setDeleteFlag(item.getDeleteFlag());
        }, () -> new SafeFoodException((SafeFoodException.ERROR_CATEGORY_IS_NOT_EXISTS)));
        return categoryDto;
    }


    @Transactional(readOnly = true)
    public PageInfo<ProductResponseDto> getAllProducts(Integer page, Integer limit, Long shopId, String query) throws SafeFoodException {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        return AppUtils.pagingResponse(productRepository.getAllProducts(pageable, shopId, query));
    }
}
