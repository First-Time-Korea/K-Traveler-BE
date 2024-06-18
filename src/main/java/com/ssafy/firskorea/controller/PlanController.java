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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.plan.dto.PlanThumbnailDto;
import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.PlanCreationDto;
import com.ssafy.firskorea.plan.service.PlanService;
import com.ssafy.firskorea.plan.service.PlanServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
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

	@PostMapping() // 여행 계획 등록
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<?> createPlan(@RequestPart("planRequest") PlanCreationDto planCreationDto,
										 @RequestPart("file") MultipartFile file) throws SQLException, IOException {
		if (!file.isEmpty()) {
			initializePlanRequest(planCreationDto, file);
		}
		planService.createPlan(planCreationDto);
		return CommonResponse.okCreation();
	}

	// 나의 여행 계획 리스트 조회하기
	@GetMapping("/paginated")
	public CommonResponse<?> getPaginatedPlans(@ModelAttribute PlanMemberPgnoDto planMemberPgnoDto) throws SQLException {
		return CommonResponse.ok(planService.getPaginatedPlans(planMemberPgnoDto));
	}

	@GetMapping("/{planId}") // 계획 상세 조회
	public CommonResponse<?> getPlanDetails(@PathVariable String planId) throws SQLException {
		return CommonResponse.ok(planService.getPlanDetails(Integer.parseInt(planId)));
	}

	@PutMapping("/{planId}/memos")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<?> updatePlanMemos(
			@PathVariable String planId,
			@RequestBody Map<String, List<PlanMemoDto>> memoMap) throws SQLException {
		planService.updatePlanMemos(memoMap.get("memos"));
		return CommonResponse.okCreation();
	}

	@DeleteMapping("/{planId}")
	public CommonResponse<?> deletePlan(@PathVariable String planId) throws SQLException {
		planService.deletePlan(planId);
		return CommonResponse.ok();
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
