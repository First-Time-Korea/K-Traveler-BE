package com.ssafy.firskorea.controller;

import java.sql.SQLException;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.attraction.dto.request.SidoPgnoDto;
import com.ssafy.firskorea.attraction.service.AttractionGptService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @Hidden
    @GetMapping("/search/v1")
    public ResponseEntity<CommonResponse<?>> getAttractionsBySearch(
            @Valid @ModelAttribute SearchDto searchDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(attractionService.getAttractionsBySearch(searchDto)));
    }

    @Operation(summary = "여행지 검색(페이지네이션)", description = "키워드, 카테고리, 테마, 지역을 선택하면 필터링 후 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/search/v2")
    public ResponseEntity<CommonResponse<?>> getPaginatedAttractionsBySearch(
             @Valid @ModelAttribute SearchDto searchDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(attractionService.getPaginatedAttractionsBySearch(searchDto)));
    }


    @Operation(summary = "여행지 북마크 토글", description = "회원과 관광지 아이디를 기반으로 북마크를 토글한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로직 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @PutMapping("/bookmarks")
    public ResponseEntity<CommonResponse<?>> toggleAttractionBookmark(
            @Valid @RequestBody MemberContentDto memberContentDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.ok(attractionService.toggleAttractionBookmark(memberContentDto)));
    }

    @Operation(summary = "여행지 단일 조회", description = "회원과 관광지 아이디를 기반으로 여행지를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/details")
    public ResponseEntity<CommonResponse<?>> getAttractionDetail(
            @Valid @ModelAttribute MemberContentDto memberContentDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(attractionService.getAttractionDetail(memberContentDto)));
    }

    @Operation(summary = "GPT를 사용, 여행지 단일 조회", description = "K-Culture DB화 시키기 전에 사용")
    @Hidden
    @PostMapping("/details/ai/v1")
    public ResponseEntity<CommonResponse<?>> getAttractionDetailWithAIV1(
            @Valid @RequestBody MemberContentDto memberContentDto)
            throws SQLException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.ok(attractionGptService.getAttractionDetailWithGptApi(memberContentDto)));
    }

    @Operation(summary = "특정 지역의 관광지 조회(페이지네이션)", description = "시도코드에 해당하는 관광지를 페이지네이션 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/regions/paginated")
    public ResponseEntity<CommonResponse<?>> getPaginatedAttractionsBySidoCode(
            @Valid @ModelAttribute SidoPgnoDto sidoPgnoDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(attractionService.getPaginatedAttractionsBySidoCode(sidoPgnoDto)));

    }

    @Operation(summary = "북마크 한 관광지 조회(페이지네이션)", description = "유저가 북마크한 장소를 페이지네이션 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/bookmarks/paginated")
    public ResponseEntity<CommonResponse<?>> getPaginatedAttractionsBookmarked(
            @Valid @ModelAttribute MemberPgnoDto memberPgnoDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(attractionService.getPaginatedAttractionsBookmarked(memberPgnoDto)));
    }

}