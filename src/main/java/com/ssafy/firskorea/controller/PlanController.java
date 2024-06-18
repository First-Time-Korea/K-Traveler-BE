package com.ssafy.firskorea.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.plan.dto.request.MemberPgnoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
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

	private PlanService planService;

	@Autowired
	public PlanController(PlanServiceImpl planServiceImpl) {
		this.planService = planServiceImpl;
	}

	@PostMapping() // 여행 계획 등록
	@ResponseStatus(HttpStatus.CREATED)
	private CommonResponse<?> insertPlanner(@RequestPart("planRequest") PlanRequest planRequest,
											@RequestPart("file") MultipartFile file) throws Exception {
		if (!file.isEmpty()) {
			initializePlanRequest(planRequest, file);
		}
		planService.registerPlanner(planRequest);
		return CommonResponse.okCreation();
	}

	// 나의 여행 계획 리스트 조회하기
	@GetMapping("/list")
	public CommonResponse<?> getPlanInfos(@ModelAttribute MemberPgnoDto memberPgnoDto) throws Exception {
		return CommonResponse.ok(planService.getPlanInfos(memberPgnoDto));
	}

	@GetMapping("/info") // 계획 상세 조회
	private CommonResponse<?> viewPlannerInfo(@RequestParam("planId") String planId) throws Exception {
		return CommonResponse.ok(planService.getCompletePlanner(Integer.parseInt(planId)));
	}

	@PutMapping("/memo")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<?> viewPlannerInfo(@RequestBody Map<String, List<PlanMemoDto>> memoMap) throws Exception {
		planService.updateMemo(memoMap.get("memos"));
		return CommonResponse.okCreation();
	}

	@DeleteMapping()
	public CommonResponse<?> deletePlan(@RequestParam String planId) throws Exception {
		planService.deletePlan(planId);
		return CommonResponse.ok();
	}

	private void initializePlanRequest(PlanRequest planRequest, MultipartFile file) throws IOException {
		String today = new SimpleDateFormat("yyMMdd").format(new Date());
		String saveFolder = UPLOAD_PATH + File.separator + today;
		File folder = new File(saveFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String originalFileName = file.getOriginalFilename();
		String saveFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자
		PlanFileDto fileInfoDto = new PlanFileDto(today, originalFileName, saveFileName);
		file.transferTo(new File(folder, saveFileName)); //실제 파일 저장
		planRequest.setPlanFileDto(fileInfoDto);
	}

}
