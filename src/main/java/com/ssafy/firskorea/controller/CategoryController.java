package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.attraction.service.AttractionService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

    private final AttractionService attractionService;

    public CategoryController(AttractionService attractionService) {
        super();
        this.attractionService = attractionService;
    }

    @Operation(summary = "여행 테마", description = "여행 테마를 비동기로 불러올 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @GetMapping("/themes")
    public CommonResponse<?> getThemeList() throws SQLException {
        return CommonResponse.ok(attractionService.getThemeList());
    }

    @Operation(summary = "여행 카테고리", description = "선택한 여행 테마에 해당하는 카테고리를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @GetMapping("/themes/{themeCode}/categories")
    public CommonResponse<?> getCategoryList(
            @Parameter(description = "카테고리를 조회할 테마 코드", required = true)
            @PathVariable Character themeCode) throws SQLException {
        return CommonResponse.ok(attractionService.getCategoryList(themeCode));
    }

    @Operation(summary = "여행 지역 정보", description = "대한민국 행정 지역에 속한 시도를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @GetMapping("/regions")
    public CommonResponse<?> getSidoList() throws SQLException {
        return CommonResponse.ok(attractionService.getSidoList());
    }
}