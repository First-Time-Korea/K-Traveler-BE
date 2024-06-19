package com.ssafy.firskorea.plan.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.firskorea.plan.dto.request.*;
import com.ssafy.firskorea.plan.dto.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.plan.mapper.PlanMapper;
import com.ssafy.firskorea.util.SizeConstant;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanMapper planMapper;

    @Value("${planThumbFile.path.upload-images}")
    private String uploadImagesPath;

    public PlanServiceImpl(PlanMapper planMapper) {
        this.planMapper = planMapper;
    }

    @Override
    @Transactional
    public void createPlan(PlanCreationDto dto) throws SQLException {
        Map<String, Object> plan = new HashMap<>();
        addNewPlan(dto, plan);
        String planId = String.valueOf(plan.get("id"));
        addPlanThumbnail(dto, planId);
        for (PlanDateAttractionDto apd : dto.getPlanDateAttractionDtos()) {
            LocalDateTime date = apd.getDate();
            for (String contentId : apd.getContentId()) {
                addPlanAttraction(planId, date, contentId);
            }
        }
    }

    @Override
    public PaginatedPlansDto getPaginatedPlans(PlanMemberPgnoDto planMemberPgnoDto) throws SQLException {
        List<PlanInfoDto> planInfos = fetchPaginatedPlans(planMemberPgnoDto);
        int currentPage = Integer.parseInt(planMemberPgnoDto.getPgno() == null ? "1" : planMemberPgnoDto.getPgno());
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractions = planMapper.getTotalPlanCount(planMemberPgnoDto.getMemberId());
        int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;
        return new PaginatedPlansDto(planInfos, currentPage, totalPageCount);
    }

    @Override
    @Transactional
    public PlanDetailsDto getPlanDetails(int planId) throws SQLException {
        List<PlanAndAttractionDto> paaDtos = planMapper.getPlanAndAttractions(planId);
        if (paaDtos.isEmpty()) {
            Map<String, List<PlanAttractionDetailsDto>> attractionsByDate = calculateGroupAttractionsByDate(paaDtos);
            return new PlanDetailsDto(planId, attractionsByDate);
        }
        String planTitle = paaDtos.get(0).getPlanTitle();
        Map<String, List<PlanAttractionDetailsDto>> attractionsByDate = calculateGroupAttractionsByDate(paaDtos);
        return new PlanDetailsDto(planId, planTitle, attractionsByDate);
    }

    @Override
    @Transactional
    public void updatePlanMemos(List<PlanMemoDto> memoList) throws SQLException {
        for (PlanMemoDto memoDto : memoList) {
            planMapper.updatePlanMemo(memoDto);
        }
    }

    @Override
    public void deletePlan(String planId) throws SQLException {
        planMapper.deletePlan(planId);
    }

    private void addPlanAttraction(String planId, LocalDateTime date, String contentId) throws SQLException {
        Map<String, Object> pnaMap = new HashMap<>();
        pnaMap.put("planId", planId);
        pnaMap.put("date", date);
        pnaMap.put("contentId", contentId);
        planMapper.insertPlanAndAttraction(pnaMap);
    }

    private void addPlanThumbnail(PlanCreationDto dto, String planId) throws SQLException {
        PlanThumbnailDto fileDto = dto.getPlanThumbnailDto();
        if (fileDto != null) {
            fileDto.setPlanId(planId);
            planMapper.insertPlanThumbnail(fileDto);
        }
    }

    private void addNewPlan(PlanCreationDto dto, Map<String, Object> plan) throws SQLException {
        plan.put("memberId", dto.getMemberId());
        plan.put("title", dto.getTitle());
        planMapper.insertPlan(plan);
    }

    private List<PlanInfoDto> fetchPaginatedPlans(PlanMemberPgnoDto planMemberPgnoDto) throws SQLException {
        int pgNo = Integer.parseInt(planMemberPgnoDto.getPgno() == null ? "1" : planMemberPgnoDto.getPgno());
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;

        String memberId = planMemberPgnoDto.getMemberId();
        return planMapper.retrievePaginatedPlans(Map.of(
                "memberId", memberId,
                "start", start,
                "listsize", SizeConstant.LIST_SIZE
        ));
    }

    private Map<String, List<PlanAttractionDetailsDto>> calculateGroupAttractionsByDate(List<PlanAndAttractionDto> paaDtos) throws SQLException {
        List<PlanAttractionDetailsDto> attractions = fetchAttractionDetailsForPlan(paaDtos);
        return groupAttractionsByDate(attractions);
    }

    private Map<String, List<PlanAttractionDetailsDto>> groupAttractionsByDate(List<PlanAttractionDetailsDto> attractions) {
        Map<String, List<PlanAttractionDetailsDto>> attractionsByDate = new HashMap<>();
        for (PlanAttractionDetailsDto afp : attractions) {
            String dateKey = afp.getDate().toString();
            attractionsByDate.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(afp);
        }
        return attractionsByDate;
    }

    private List<PlanAttractionDetailsDto> fetchAttractionDetailsForPlan(List<PlanAndAttractionDto> paaDtos) throws SQLException {
        List<PlanAttractionDetailsDto> attractions = new ArrayList<>();
        for (PlanAndAttractionDto dto : paaDtos) {
            attractions.add(planMapper.planAttractionDetails(dto));
        }
        return attractions;
    }
}
