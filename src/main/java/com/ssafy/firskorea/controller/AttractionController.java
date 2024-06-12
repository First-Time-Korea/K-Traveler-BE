package com.ssafy.firskorea.controller;

import java.sql.SQLException;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.attraction.dto.request.SidoPgnoDto;
import com.ssafy.firskorea.attraction.service.AttractionGptService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/attractions")
@CrossOrigin(origins = "*")
public class AttractionController {

    private final AttractionService attractionService;
    private final AttractionGptService attractionGptService;

    public AttractionController(AttractionService attractionService, AttractionGptService attractionGptService) {
        super();
        this.attractionGptService = attractionGptService;
        this.attractionService = attractionService;
    }

    @Operation(summary = "여행지 검색", description = "키워드, 카테고리, 테마, 지역을 선택하면 필터링 후 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PostMapping("/search")
    public CommonResponse<?> getAttractionsBySearch(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "관광지 필터링 조건",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchDto.class)))
            @org.springframework.web.bind.annotation.RequestBody SearchDto searchDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.getAttractionsBySearch(searchDto));
    }

    @Operation(summary = "여행지 북마크 토글", description = "회원과 관광지 아이디를 기반으로 북마크를 토글한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PutMapping("/bookmarks")
    public CommonResponse<?> toggleAttractionBookmark(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 회원과 관광지 아이디",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberContentDto.class)))
            @org.springframework.web.bind.annotation.RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.toggleAttractionBookmark(memberContentDto));
    }

    @Operation(summary = "여행지 단일 조회", description = "회원과 관광지 아이디를 기반으로 여행지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PostMapping("/details")
    public CommonResponse<?> getAttractionDetail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 회원과 관광지 아이디",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberContentDto.class)))
            @org.springframework.web.bind.annotation.RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.getAttractionDetail(memberContentDto));
    }

    @Operation(summary = "GPT를 사용, 여행지 단일 조회", description = "회원과 관광지 아이디를 기반으로 여행지를 조회 하되, GPT를 사용한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PostMapping("/details/ai")
    public CommonResponse<?> getAttractionDetailWithAI(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 회원과 관광지 아이디",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberContentDto.class)))
            @org.springframework.web.bind.annotation.RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return CommonResponse.ok(attractionGptService.getAttractionDetailWithAI(memberContentDto));
    }

    @Operation(summary = "특정 지역의 관광지 조회(페이지네이션)", description = "시도코드에 해당하는 관광지를 페이지네이션 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PostMapping("/regions/paginated")
    public CommonResponse<?> getPaginatedAttractionsBySidoCode(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 지역 코드와 페이징 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SidoPgnoDto.class)))
            @org.springframework.web.bind.annotation.RequestBody SidoPgnoDto sidoPgnoDto)
            throws SQLException {
        return CommonResponse.ok(attractionService.getPaginatedAttractionsBySidoCode(sidoPgnoDto));
    }

    @Operation(summary = "북마크 한 관광지 조회(페이지네이션)", description = "유저가 북마크한 장소를 페이지네이션 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @PostMapping("/bookmarks/paginated")
    public CommonResponse<?> getPaginatedAttractionsBookmarked(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 회원 아이디와 페이징 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberPgnoDto.class)))
            @org.springframework.web.bind.annotation.RequestBody MemberPgnoDto memberPgnoDto) throws SQLException {
        return CommonResponse.ok(attractionService.getPaginatedAttractionsBookmarked(memberPgnoDto));
    }

    @Operation(summary = "북마크 한 관광지 조회", description = "유저가 북마크한 전체 장소를 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "600", description = "로직 수행 중 실패"),
    })
    @GetMapping("/bookmarks/all")
    public CommonResponse<?> getAllAttractionsBookmarked(
            @Parameter(description = "조회할 회원 아이디", required = true)
            @RequestParam String memberId) throws SQLException {
        return CommonResponse.ok(attractionService.getAllAttractionsBookmarked(memberId));
    }

}