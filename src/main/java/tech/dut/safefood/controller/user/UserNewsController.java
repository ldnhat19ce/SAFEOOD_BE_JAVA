package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.UserNewsResponseDto;
import tech.dut.safefood.service.User.UserNewsService;
import tech.dut.safefood.vo.PageInfo;

@RestController
@RequestMapping("/user/news")
public class UserNewsController {

    @Autowired
    private UserNewsService newsService;

    @ApiOperation("Get all news")
    @GetMapping
    public APIResponse<PageInfo<UserNewsResponseDto>> getAllNews(
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "Sort by", allowableValues = "SORT_OLDEST_TO_NEWEST,SORT_NEWEST_TO_OLDEST")
            @RequestParam(required = false) String sort) {
        return APIResponse.okStatus(newsService.getAllNews(limit, page, sort));
    }

    @ApiOperation("Get detail news")
    @GetMapping("/{id}")
    public APIResponse<UserNewsResponseDto> getDetailNews(@PathVariable("id") Long id) {
        return APIResponse.okStatus(newsService.getDetailNews(id));
    }
}
