package com.ssafy.firskorea.controller;

import com.ssafy.firskorea.plan.dto.RegionDto;
import com.ssafy.firskorea.plan.service.PlanService;
import com.ssafy.firskorea.plan.service.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@CrossOrigin(origins = "*")
@Tag(name = "여행 계획 컨트롤러", description = "")
@Slf4j
public class PlanController {

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


}
