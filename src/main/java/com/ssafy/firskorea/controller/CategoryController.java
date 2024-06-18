package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.attraction.service.AttractionService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Slf4j
@RestController
@Validated
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
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/themes")
    public CommonResponse<?> getThemeList() throws SQLException {
        return CommonResponse.ok(attractionService.getThemeList());
    }

    @Operation(summary = "여행 카테고리", description = "선택한 여행 테마에 해당하는 카테고리를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/themes/{themeCode}/categories")
    public CommonResponse<?> getCategoryList(
            @Parameter(description = "카테고리를 조회할 테마 코드", required = true)
            @Pattern(regexp = "^[a-zA-Z]$", message = "테마 코드 형식이 올바르지 않습니다.")
            @PathVariable String themeCode) throws SQLException {
        return CommonResponse.ok(attractionService.getCategoryList(themeCode.charAt(0)));
    }

    @Operation(summary = "대한민국 행정 구역 조회", description = "대한민국 행정 지역에 속한 시도를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/regions")
    public CommonResponse<?> getSidoList() throws SQLException {
        return CommonResponse.ok(attractionService.getSidoList());
    }

    @Operation(summary = "대한민국 행정 구역 상세 정보", description = "시도 코드, 이름, 이미지, 설명 전체 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/regions/details")
    public CommonResponse<?> getSidoInfoList() throws SQLException {
        return CommonResponse.ok(attractionService.getSidoInfoList());
    }
}