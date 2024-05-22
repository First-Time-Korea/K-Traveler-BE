package com.ssafy.firskorea.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.service.AttractionGptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;
import com.ssafy.firskorea.attraction.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * 검색 및 비동기 처리 전용 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/attraction")
@CrossOrigin(origins = "*")
public class AttractionController {

    private AttractionService attractionService;
    private AttractionGptService attractionGptService;

    public AttractionController(AttractionService attractionService, AttractionGptService attractionGptService) {
        super();
        this.attractionGptService = attractionGptService;
        this.attractionService = attractionService;
    }

    @Operation(summary = "여행 테마", description = "여행지 탭에 들어가면, 기본적으로 여행 테마를 불러온다.")
    @GetMapping("/theme")
    public ResponseEntity<Map<String, Object>> getThemeList() throws SQLException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
        List<ThemeDto> list = attractionService.getThemeList();
        resultMap.put("status", "success");
        resultMap.put("data", list);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "여행 카테고리", description = "여행 테마에 맞는 여행 카테고리를 조회한다.")
    @GetMapping("/theme/{themaCode}/category")
    public ResponseEntity<Map<String, Object>> getCategoryList(@PathVariable Character themaCode) throws SQLException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
        List<Category> list = attractionService.getCategoryList(themaCode);
        resultMap.put("status", "success");
        resultMap.put("data", list);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "여행 지역 정보", description = "대한민국 행정 지역에 속한 시도를 조회한다.")
    @GetMapping("/region")
    public ResponseEntity<Map<String, Object>> getSidoList() throws SQLException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
        List<Category> list = attractionService.getSidoList();
        resultMap.put("status", "success");
        resultMap.put("data", list);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "여행지 검색", description = "검색한 키워드를 선택한 테마, 카테고리 내에서 찾아 반환한다.")
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> getAttractionListByThema(@RequestBody SearchDto searchDto)
            throws SQLException {
        System.out.println(searchDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
        List<AttractionDto> list = attractionService.getAttractionBySearch(searchDto);
        resultMap.put("status", "success");
        resultMap.put("data", list);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "북마크 토글", description = "회원 to 여행지 북마크")
    @PostMapping("/bookmark")
    public ResponseEntity<Map<String, Object>> toggleBookmark(@RequestBody Map<String, String> map)
            throws SQLException {
        System.out.println("북마크 요청" + map);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        AttractionDto dto = attractionService.toggleBookmark(map);
        resultMap.put("status", "success");
        resultMap.put("data", dto);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "여행지 단일 조회", description = "회원 아이디 및 콘텐트 아이디로 여행지 단일 조회")
    @PostMapping()
    public ResponseEntity<Map<String, Object>> getAttraction(@RequestBody Map<String, String> map)
            throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        AttractionDto dto = attractionService.getAttractionById(map);
        resultMap.put("status", "success");
        resultMap.put("data", dto);
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "GPT를 사용, 여행지 단일 조회", description = "GPT를 사용해 한글을 영문으로 번역 후 반환")
    @PostMapping("/ai")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody Map<String, String> map) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        AttractionDto dto = attractionGptService.prompt(map);
        HttpStatus status = HttpStatus.ACCEPTED;
        resultMap.put("status", "success");
        resultMap.put("data", dto);
        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "특정 지역의 관광지 전체 조회", description = "유저가 검색한 장소에 해당하는 시도 코드를 가지고 해당 지역에 존재하는 관광지 반환")
    @GetMapping("/sido/{sidoCode}")
    public ResponseEntity<Map<String, Object>> getAttractionListBySidoCode(@PathVariable String sidoCode) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        List<AttractionDto> dto = attractionService.getAttractionListBySidoCode(sidoCode);
        HttpStatus status = HttpStatus.ACCEPTED;
        resultMap.put("status", "success");
        resultMap.put("data", dto);
        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "북마크 한 위치 조회", description = "해당 유저가 북마크한 모든 장소를 반환한다.")
    @GetMapping("/bookmark")
    public ResponseEntity<Map<String, Object>> getBookmarkedAttractionList(@RequestParam String memberId) throws SQLException {
        Map<String, Object> resultMap = new HashMap<>();
        List<AttractionDto> dto = attractionService.getBookmarkedAttractionList(memberId);
        HttpStatus status = HttpStatus.ACCEPTED;
        resultMap.put("status", "success");
        resultMap.put("data", dto);
        return new ResponseEntity<>(resultMap, status);
    }
    
    // 나의 여행지 리스트 조회하기
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getBookmarkedAttractionInfos(@RequestParam Map<String, String> map) throws Exception {
		Map<String, Object> result = attractionService.getBookmarkedAttractionInfos(map);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 계획 리스트 조회 성공");
		response.put("bookmarkedAttractionInfos", result.get("bookmarkedAttractionInfos"));
		response.put("currentPage", result.get("currentPage"));
		response.put("totalPageCount", result.get("totalPageCount"));

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

}