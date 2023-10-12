package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.dto.response.UserNewsResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.repository.NewsRepository;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;
import tech.dut.safefood.vo.PageInfo;


@Service
@RequiredArgsConstructor
public class UserNewsService {
    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    public PageInfo<UserNewsResponseDto> getAllNews(Integer limit, Integer page, String sort) {
        Sort sortBy = AppUtils.buildSortCreated(sort);
        Pageable pageable = AppUtils.buildPageRequest(page, limit, sortBy);
        return AppUtils.pagingResponse(newsRepository.userGetPageNews(pageable));
    }

    @Transactional(readOnly = true)
    public UserNewsResponseDto getDetailNews(Long newsId) throws SafeFoodException {
        UserNewsResponseDto userNewsResponseDTO = newsRepository.userGetDetailNewsById(newsId).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_NEWS_NOT_FOUND)
        );
        userNewsResponseDTO.setContent(String.format(Constants.WEB_VIEW_NEWS, userNewsResponseDTO.getNewsImage(), userNewsResponseDTO.getTitle(), userNewsResponseDTO.getSubTitle(), userNewsResponseDTO.getContent()).replaceAll("[\"]", ""));
        return userNewsResponseDTO;
    }
}
