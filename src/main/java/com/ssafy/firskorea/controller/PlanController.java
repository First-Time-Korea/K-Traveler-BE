package com.ssafy.firskorea.controller;

import java.io.File;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.plan.dto.PlanFileDto;
import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.dto.request.PlanRequest;
import com.ssafy.firskorea.plan.service.PlanService;
import com.ssafy.firskorea.plan.service.PlanServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "*")
@Tag(name = "여행 계획 컨트롤러", description = "")
@Slf4j
public class PlanController {

	@Value("${planThumbFile.path}")
	private String UPLOAD_PATH;

	private PlanService planService;

	@Autowired
	public PlanController(PlanServiceImpl planServiceImpl) {
		this.planService = planServiceImpl;
	}

	@Operation(summary = "대한민국 행정 구역 전체 조회", description = "시도 코드, 이름, 이미지, 설명 전체 반환")
	@GetMapping("/regions")
	public ResponseEntity<Map<String, Object>> getRegionList() throws SQLException {
		Map<String, Object> resultMap = new HashMap<>();
		List<RegionDto> dto = planService.getRegionList();
		HttpStatus status = HttpStatus.ACCEPTED;
		resultMap.put("status", "success");
		resultMap.put("data", dto);
		return new ResponseEntity<>(resultMap, status);
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
		System.out.println(planRequest);
		planService.registerPlanner(planRequest);
		res.put("status", "success");
		res.put("message", "여행 계획 등록 성공");
		res.put("data", "null");
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	@GetMapping("/list") // 본인의 계획 목록 조회
//	private ResponseEntity<?> listPlanner(HttpSession session) throws Exception {
////        String memberId = (String) session.getAttribute("loginId");
//		Map<String, Object> res = new HashMap<>();
//		if (memberId != null) {
//			List<PlannerDto> plan = plannerService.listPlanner(memberId);
//			res.put("status", "success");
//			res.put("message", "여행 계획 목록 조회 성공");
//			res.put("data", plan);
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		res.put("status", "fail");
//		res.put("message", "로그인 안되어 있음");
//		res.put("data", "null");
//		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
//	}
//
//	@GetMapping("/info") // 계획 상세 조회
//	private ResponseEntity<?> viewPlannerInfo(HttpSession session, @RequestParam("planNo") String planNo)
//			throws Exception {
////        String memberId = (String) session.getAttribute("loginId");
//		Map<String, Object> res = new HashMap<>();
//		if (memberId != null) {
//			PlannerDto planInfo = plannerService.getCompletePlanner(planNo);
//			res.put("status", "success");
//			res.put("message", "여행 계획 상세 조회 성공");
//			res.put("data", planInfo);
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		res.put("status", "fail");
//		res.put("message", "로그인 안되어 있음");
//		res.put("data", "null");
//		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
//	}
//
//	@PutMapping("/memo")
//	public ResponseEntity<?> viewPlannerInfo(@RequestBody MemoDto memoDto, HttpSession session) throws Exception {
////        String memberId = (String) session.getAttribute("loginId");
//		Map<String, Object> res = new HashMap<>();
//		if (memberId != null) {
//			memoDto.setMemberId(memberId);
//			res.put("status", "success");
//			res.put("message", "메모 수정 성공");
//			res.put("data", "null");
//			plannerService.updateMemo(memoDto);
//			return new ResponseEntity<>(res, HttpStatus.OK);
//		}
//		res.put("status", "fail");
//		res.put("message", "로그인 안되어 있음");
//		res.put("data", "null");
//		return new ResponseEntity<>(res, HttpStatus.FAILED_DEPENDENCY);
//	}

}
