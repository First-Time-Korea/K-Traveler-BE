package com.ssafy.firskorea.controller;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.attraction.dto.request.SidoPgnoDto;
import com.ssafy.firskorea.attraction.service.AttractionGptService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.*;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/attraction")
@CrossOrigin(origins = "*")
public class AttractionController {

    private final AttractionService attractionService;
    private final AttractionGptService attractionGptService;

    public AttractionController(AttractionService attractionService, AttractionGptService attractionGptService) {
        super();
        this.attractionGptService = attractionGptService;
        this.attractionService = attractionService;
    }

    @Operation(summary = "여행 테마", description = "여행 테마를 비동기로 불러올 때 사용")
    @GetMapping("/theme")
    public CommonResponse<?> getThemeList() throws SQLException {
        return CommonResponse.ok(attractionService.getThemeList());
    }

    @Operation(summary = "여행 카테고리", description = "선택한 여행 테마에 해당하는 카테고리를 불러온다.")
    @GetMapping("/theme/{themeCode}/category")
    public CommonResponse<?> getCategoryList(@PathVariable Character themeCode) throws SQLException {
        return CommonResponse.ok(attractionService.getCategoryList(themeCode));
    }

    @Operation(summary = "여행 지역 정보", description = "대한민국 행정 지역에 속한 시도를 조회한다.")
    @GetMapping("/region")
    public CommonResponse<?> getSidoList() throws SQLException {
        return CommonResponse.ok(attractionService.getSidoList());
    }

    @Operation(summary = "여행지 검색", description = "키워드, 카테고리, 테마, 지역을 선택하면 필터링 후 반환한다.")
    @PostMapping("/search")
    public CommonResponse<?> getAttractionsBySearch(@RequestBody SearchDto searchDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.getAttractionsBySearch(searchDto));
    }

    @Operation(summary = "북마크 토글", description = "회원과 관광지 아이디를 기반으로 북마크를 토글한다.")
    @PostMapping("/bookmark")
    public CommonResponse<?> toggleAttractionBookmark(@RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.toggleAttractionBookmark(memberContentDto));
    }

    @Operation(summary = "여행지 단일 조회", description = "회원과 관광지 아이디를 기반으로 여행지를 조회한다.")
    @PostMapping()
    public CommonResponse<?> getAttractionDetail(@RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.getAttractionDetail(memberContentDto));
    }

    @Operation(summary = "GPT를 사용, 여행지 단일 조회", description = "회원과 관광지 아이디를 기반으로 여행지를 조회 하되, GPT를 사용한다.")
    @PostMapping("/ai")
    public CommonResponse<?> getAttractionDetailWithAI(@RequestBody MemberContentDto memberContentDto) throws Exception {
        return CommonResponse.ok(attractionGptService.getAttractionDetailWithAI(memberContentDto));
    }

    @Operation(summary = "특정 지역의 관광지 전체 조회(페이지네이션)", description = "시도코드에 해당하는 관광지를 페이지네이션 한다.")
    @PostMapping("/sido")
    public CommonResponse<?> getPaginatedAttractionsBySidoCode(@RequestBody SidoPgnoDto sidoPgnoDto) throws Exception {
        return CommonResponse.ok(attractionService.getPaginatedAttractionsBySidoCode(sidoPgnoDto));
    }

    @Operation(summary = "북마크 한 위치 조회(페이지네이션)", description = "유저가 북마크한 장소를 페이지네이션 한다.")
    @PostMapping("/list" )
    public CommonResponse<?> getPaginatedAttractionsBookmarked(@RequestBody MemberPgnoDto memberPgnoDto) throws Exception {
        return CommonResponse.ok(attractionService.getPaginatedAttractionsBookmarked(memberPgnoDto));
    }

    @Operation(summary = "북마크 한 위치 조회", description = "유저가 북마크한 전체 장소를 반환한다.")
    @GetMapping("/bookmark")
    public CommonResponse<?> getAllAttractionsBookmarked(@RequestParam String memberId) throws SQLException {
        return CommonResponse.ok(attractionService.getAllAttractionsBookmarked(memberId));
    }


}