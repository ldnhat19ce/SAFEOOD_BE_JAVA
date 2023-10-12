package tech.dut.safefood.service.Admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.request.AdminNewsDTO;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.News;
import tech.dut.safefood.model.mapper.NewsMapper;
import tech.dut.safefood.repository.NewsRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.vo.PageInfo;

@Service
@RequiredArgsConstructor
public class AdminManagerNewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public AdminNewsDTO adminCreateNews(AdminNewsDTO adminNewsDTO) {
        News news = NewsMapper.INSTANCE.toEntity(adminNewsDTO);
        news.setLocation(adminNewsDTO.getLatitude());
        newsRepository.save(news);
        return adminNewsDTO;
    }

    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public AdminNewsDTO adminUpdateNews(AdminNewsDTO adminNewsDTO) throws SafeFoodException {
        News news = newsRepository.findById(adminNewsDTO.getId())
                .orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_NEWS_NOT_FOUND));
        news.setTitle(adminNewsDTO.getTitle());
        news.setAddress(adminNewsDTO.getAddress());
        news.setContent(adminNewsDTO.getContent());
        news.setImage(adminNewsDTO.getImage());
        news.setSubTitle(adminNewsDTO.getSubTitle());
        news.setLocation(adminNewsDTO.getLatitude());
        return adminNewsDTO;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void adminDeleteNews(Long id) throws SafeFoodException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_NEWS_NOT_FOUND));
        newsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PageInfo<AdminNewsDTO> adminGetPageNews(Integer page, Integer limit, String query) {
        if(page == null)
            page = 0;
        Pageable pageable = AppUtils.buildPageableCreated(page, limit);

        Page<AdminNewsDTO> data = newsRepository.adminGetPageNews(pageable, query);

        return AppUtils.pagingResponse(data);
    }

    @Transactional(readOnly = true)
    public AdminNewsDTO adminGetDetailNewsById(Long id) throws SafeFoodException {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_NEWS_NOT_FOUND));
        return NewsMapper.INSTANCE.toDto(news);
    }
}
