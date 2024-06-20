package com.ssafy.firskorea.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.plan.dto.request.PlanMemberPgnoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.plan.dto.request.PlanThumbnailDto;
import com.ssafy.firskorea.plan.dto.request.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.PlanCreationDto;
import com.ssafy.firskorea.plan.service.PlanService;
import com.ssafy.firskorea.plan.service.PlanServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/plans")
@CrossOrigin(origins = "*")
public class PlanController {

    @Value("${planThumbFile.path.upload-images}")
    private String UPLOAD_PATH;

    private final PlanService planService;

    @Autowired
    public PlanController(PlanServiceImpl planServiceImpl) {
        this.planService = planServiceImpl;
    }

    @Operation(summary = "여행 계획 등록", description = "여행 계획을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "여행 계획 등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @PostMapping() // 여행 계획 등록
    public ResponseEntity<CommonResponse<?>> createPlan(
            @Valid @RequestPart("planRequest") PlanCreationDto planCreationDto,
            @RequestPart("file") MultipartFile file) throws SQLException, IOException {
        if (!file.isEmpty()) {
            initializePlanRequest(planCreationDto, file);
        }
        planService.createPlan(planCreationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.okCreation());
    }

    @Operation(summary = "나의 여행 계획 목록을 조회한다.", description = "나의 여행 계획을 페이징 하여 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 계획 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/paginated")
    public ResponseEntity<CommonResponse<?>> getPaginatedPlans(
            @Valid @ModelAttribute PlanMemberPgnoDto planMemberPgnoDto) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(planService.getPaginatedPlans(planMemberPgnoDto)));
    }

    @Operation(summary = "여행 계획 상세 조회", description = "여행 계획 번호로 계획을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 계획 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @GetMapping("/{planId}") // 계획 상세 조회
    public ResponseEntity<CommonResponse<?>> getPlanDetails(
            @Parameter(description = "여행 계획 번호", required = true)
            @Pattern(regexp = "^\\d+$")
            @PathVariable String planId) throws SQLException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok(planService.getPlanDetails(Integer.parseInt(planId))));
    }

    @Operation(summary = "여행 계획 메모", description = "여행 계획 내부 특정 날짜의 특정 관광지에 대한 메모를 등록한다.(또는 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "메모 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @PutMapping("/{planId}/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommonResponse<?>> updatePlanMemos(
            @Parameter(description = "여행 계획 번호", required = true)
            @Pattern(regexp = "^\\d+$")
            @PathVariable String planId,
            @Valid @RequestBody Map<String, List<PlanMemoDto>> memoMap) throws SQLException {

        planService.updatePlanMemos(memoMap.get("memos"));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.okCreation());
    }

    @Operation(summary = "여행 계획 삭제", description = "여행 계획 번호로 여행 계획을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 계획 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "500", description = "로직 처리 실패"),
    })
    @DeleteMapping("/{planId}")
    public ResponseEntity<CommonResponse<?>> deletePlan(
            @Parameter(description = "여행 계획 번호", required = true)
            @Pattern(regexp = "^\\d+$")
            @PathVariable String planId) throws SQLException {
        planService.deletePlan(planId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.ok());
    }

    private void initializePlanRequest(PlanCreationDto planCreationDto, MultipartFile file) throws IOException, NullPointerException {
        String today = new SimpleDateFormat("yyMMdd").format(new Date());
        String saveFolder = UPLOAD_PATH + File.separator + today;
        File folder = new File(saveFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String originalFileName = file.getOriginalFilename();
        String saveFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자
        PlanThumbnailDto fileInfoDto = new PlanThumbnailDto(today, originalFileName, saveFileName);
        file.transferTo(new File(folder, saveFileName)); //실제 파일 저장
        planCreationDto.setPlanThumbnailDto(fileInfoDto);
    }

}
