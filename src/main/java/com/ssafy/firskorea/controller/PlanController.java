package com.ssafy.firskorea.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.PlanMemoDto;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.dto.response.PlanResponse;
import com.ssafy.firskorea.plan.service.PlanService;
import com.ssafy.firskorea.plan.service.PlanServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/plans")
@CrossOrigin(origins = "*")
@Tag(name = "여행 계획 컨트롤러", description = "")
@Slf4j
public class PlanController {

	@Value("${planThumbFile.path.upload-images}")
	private String UPLOAD_PATH;

	private PlanService planService;

	@Autowired
	public PlanController(PlanServiceImpl planServiceImpl) {
		this.planService = planServiceImpl;
	}

	@PostMapping() // 여행 계획 등록
	private ResponseEntity<?> insertPlanner(@RequestPart("planRequest") PlanRequest planRequest,
			@RequestPart("file") MultipartFile file) throws Exception {
		Map<String, Object> res = new HashMap<>();
		if (!file.isEmpty()) {
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = UPLOAD_PATH + File.separator + today;
			File folder = new File(saveFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			String originalFileName = file.getOriginalFilename();
			String saveFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.')); // 확장자
																														// 제거
			PlanFileDto fileInfoDto = new PlanFileDto(today, originalFileName, saveFileName);
			file.transferTo(new File(folder, saveFileName)); // 실제 파일을 저장한다.
			planRequest.setPlanFileDto(fileInfoDto);
		}
		planService.registerPlanner(planRequest);
		res.put("status", "success");
		res.put("message", "여행 계획 등록 성공");
		res.put("data", "null");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 나의 여행 계획 리스트 조회하기
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getPlanInfos(@RequestParam Map<String, String> map) throws Exception {
		Map<String, Object> result = planService.getPlanInfos(map);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 계획 리스트 조회 성공");
		response.put("planInfos", result.get("planInfos"));
		response.put("currentPage", result.get("currentPage"));
		response.put("totalPageCount", result.get("totalPageCount"));

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

	@GetMapping("/info") // 계획 상세 조회
	private ResponseEntity<?> viewPlannerInfo(@RequestParam("planId") String planId) throws Exception {
		Map<String, Object> res = new HashMap<>();
		PlanResponse planInfo = planService.getCompletePlanner(Integer.parseInt(planId));
		if (planInfo != null) {
			res.put("status", "success");
			res.put("message", "여행 계획 상세 조회 성공");
			res.put("data", planInfo);
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		res.put("status", "fail");
		res.put("message", "로그인 안되어 있음");
		res.put("data", "null");
		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
	}

	@PutMapping("/memo")
	public ResponseEntity<?> viewPlannerInfo(@RequestBody Map<String, List<PlanMemoDto>> memoMap) throws Exception {
		Map<String, Object> res = new HashMap<>();
		res.put("status", "success");
		res.put("message", "메모 수정 성공");
		res.put("data", "null");
		System.out.println(memoMap);
		planService.updateMemo((List<PlanMemoDto>) memoMap.get("memos"));
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@DeleteMapping()
	public ResponseEntity<?> deletePlan(@RequestParam String planId) throws Exception {
		Map<String, Object> res = new HashMap<>();
		res.put("status", "success");
		res.put("message", "여행 삭제 성공");
		res.put("data", "null");
		planService.deletePlan(planId);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
