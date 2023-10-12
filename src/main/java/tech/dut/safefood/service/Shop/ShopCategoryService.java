package tech.dut.safefood.service.Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.CategoryDto;
import tech.dut.safefood.repository.CategoryRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;

@Service
public class ShopCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public PageInfo<CategoryDto> getAllCategory(Integer page, Integer limit) {
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);
        return AppUtils.pagingResponse(categoryRepository.getAllCategories(pageable));
    }
}
