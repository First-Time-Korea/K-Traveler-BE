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

    /**
     * 새 여행 계획을 생성하고, 해당 계획에 대한 섬네일과 관광지 정보를 추가한다.
     *
     * @param dto 여행 계획 생성 요청 데이터 전송 객체
     */
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

    /**
     * 주어진 회원 ID에 따라 페이징 처리된 여행 계획 목록을 검색한다.
     *
     * @param planMemberPgnoDto 페이징 정보와 회원 ID를 포함하는 DTO
     * @return 페이징 처리된 여행 계획 목록
     */
    @Override
    public PaginatedPlansDto getPaginatedPlans(PlanMemberPgnoDto planMemberPgnoDto) throws SQLException {
        List<PlanInfoDto> planInfos = fetchPaginatedPlans(planMemberPgnoDto);
        int currentPage = Integer.parseInt(planMemberPgnoDto.getPgno() == null ? "1" : planMemberPgnoDto.getPgno());
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractions = planMapper.getTotalPlanCount(planMemberPgnoDto.getMemberId());
        int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;
        return new PaginatedPlansDto(planInfos, currentPage, totalPageCount);
    }

    /**
     * 주어진 계획 ID에 따라 해당 계획의 상세 정보를 조회한다.
     *
     * @param planId 조회할 여행 계획의 ID
     * @return 계획의 상세 정보를 포함하는 DTO
     */
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

    /**
     * 주어진 여행 계획 메모 목록을 업데이트한다.
     *
     * @param memoList 업데이트할 여행 계획 메모 목록
     */
    @Override
    @Transactional
    public void updatePlanMemos(List<PlanMemoDto> memoList) throws SQLException {
        for (PlanMemoDto memoDto : memoList) {
            planMapper.updatePlanMemo(memoDto);
        }
    }

    /**
     * 주어진 ID의 여행 계획을 삭제한다.
     *
     * @param planId 삭제할 여행 계획의 ID
     */
    @Override
    public void deletePlan(String planId) throws SQLException {
        planMapper.deletePlan(planId);
    }

    /**
     * 여행 계획에 관련된 관광지 정보를 데이터베이스에 추가한다.
     *
     * @param planId    여행 계획 ID
     * @param date      방문 날짜
     * @param contentId 관광지 ID
     */
    private void addPlanAttraction(String planId, LocalDateTime date, String contentId) throws SQLException {
        Map<String, Object> pnaMap = new HashMap<>();
        pnaMap.put("planId", planId);
        pnaMap.put("date", date);
        pnaMap.put("contentId", contentId);
        planMapper.insertPlanAndAttraction(pnaMap);
    }

    /**
     * 여행 계획 생성 시 제출된 섬네일 정보를 데이터베이스에 추가한다.
     *
     * @param dto    여행 계획 생성 정보가 담긴 DTO
     * @param planId 여행 계획의 ID
     */
    private void addPlanThumbnail(PlanCreationDto dto, String planId) throws SQLException {
        PlanThumbnailDto fileDto = dto.getPlanThumbnailDto();
        if (fileDto != null) {
            fileDto.setPlanId(planId);
            planMapper.insertPlanThumbnail(fileDto);
        }
    }

    /**
     * 새로운 여행 계획을 데이터베이스에 추가한다.
     *
     * @param dto  여행 계획 생성 정보가 담긴 DTO
     * @param plan 계획 정보를 저장할 맵
     */
    private void addNewPlan(PlanCreationDto dto, Map<String, Object> plan) throws SQLException {
        plan.put("memberId", dto.getMemberId());
        plan.put("title", dto.getTitle());
        planMapper.insertPlan(plan);
    }

    /**
     * 회원 ID에 따라 페이징 처리된 여행 계획 목록을 조회한다.
     *
     * @param planMemberPgnoDto 페이징 정보와 회원 ID를 포함하는 DTO
     * @return 페이징 처리된 여행 계획 목록
     */
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

    /**
     * 주어진 여행 계획 관련 데이터를 날짜별로 그룹화하여 반환한다.
     *
     * @param paaDtos 여행 계획과 관광지 정보가 담긴 DTO 리스트
     * @return 날짜별로 그룹화된 관광지 정보
     */
    private Map<String, List<PlanAttractionDetailsDto>> calculateGroupAttractionsByDate(List<PlanAndAttractionDto> paaDtos) throws SQLException {
        List<PlanAttractionDetailsDto> attractions = fetchAttractionDetailsForPlan(paaDtos);
        return groupAttractionsByDate(attractions);
    }

    /**
     * 여행 계획에 포함된 관광지 상세 정보를 날짜별로 그룹화한다.
     *
     * @param attractions 관광지 상세 정보 리스트
     * @return 날짜별로 그룹화된 관광지 정보 Map
     */
    private Map<String, List<PlanAttractionDetailsDto>> groupAttractionsByDate(List<PlanAttractionDetailsDto> attractions) {
        Map<String, List<PlanAttractionDetailsDto>> attractionsByDate = new HashMap<>();
        for (PlanAttractionDetailsDto afp : attractions) {
            String dateKey = afp.getDate().toString();
            attractionsByDate.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(afp);
        }
        return attractionsByDate;
    }

    /**
     * 여행 계획에 포함된 각 관광지의 상세 정보를 조회한다.
     *
     * @param paaDtos 여행 계획과 관광지 정보가 담긴 DTO 리스트
     * @return 관광지 상세 정보가 담긴 리스트
     */
    private List<PlanAttractionDetailsDto> fetchAttractionDetailsForPlan(List<PlanAndAttractionDto> paaDtos) throws SQLException {
        List<PlanAttractionDetailsDto> attractions = new ArrayList<>();
        for (PlanAndAttractionDto dto : paaDtos) {
            attractions.add(planMapper.planAttractionDetails(dto));
        }
        return attractions;
    }
}
