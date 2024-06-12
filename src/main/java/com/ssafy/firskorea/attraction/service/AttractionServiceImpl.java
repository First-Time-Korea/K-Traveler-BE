package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.attraction.dto.request.SidoPgnoDto;
import com.ssafy.firskorea.attraction.dto.response.PaginatedAttractionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;
import com.ssafy.firskorea.attraction.mapper.AttractionMapper;
import com.ssafy.firskorea.util.SizeConstant;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;

    @Autowired
    public AttractionServiceImpl(AttractionMapper attractionMapper) {
        this.attractionMapper = attractionMapper;
    }

    /**
     * 사용 가능한 모든 여행 테마 목록을 검색한다.
     *
     * @return {@link ThemeDto} 객체의 리스트를 반환한다.
     */
    @Override
    public List<ThemeDto> getThemeList() throws SQLException {
        return attractionMapper.getThemeList();
    }

    /**
     * 특정 테마 코드에 관련된 카테고리를 가져온다.
     *
     * @param code 테마를 식별하는 문자 코드다.
     * @return 주어진 테마에 해당하는 {@link Category} 객체의 리스트를 반환한다.
     */
    @Override
    public List<Category> getCategoryList(Character code) throws SQLException {
        return attractionMapper.getCategoryList(code);
    }

    /**
     * 대한민국의 행정 지역 목록을 검색한다.
     *
     * @return {@link Category} 객체의 리스트로 행정 지역을 반환한다.
     */
    @Override
    public List<Category> getSidoList() throws SQLException {
        return attractionMapper.getSidoList();
    }

    /**
     * {@link SearchDto}에 포함된 다양한 검색 기준에 따라 검색을 수행한다.
     *
     * @param searchDto 검색 매개변수를 포함하는 데이터 전송 객체다.
     * @return 검색 조건에 맞는 {@link AttractionDto} 리스트를 반환한다.
     */
    @Override
    public List<AttractionDto> getAttractionsBySearch(SearchDto searchDto) throws SQLException {
        return attractionMapper.getAttractionBySearch(searchDto);
    }

    /**
     * 회원 ID와 컨텐츠 ID를 기준으로 관광지의 북마크 상태를 토글한다.
     *
     * @param memberContentDto 회원 ID와 컨텐츠 ID를 포함하는 전송 객체다.
     * @return 새로운 북마크 상태로 업데이트된 {@link AttractionDto}를 반환한다.
     */
    @Override
    @Transactional
    public AttractionDto toggleAttractionBookmark(MemberContentDto memberContentDto) throws SQLException {
        AttractionDto dto = attractionMapper.getAttractionByContentId(memberContentDto);
        if (dto.getBookmarkId() > 0) {
            attractionMapper.deleteAttractionBookmark(memberContentDto);
        } else {
            attractionMapper.createAttractionBookmark(memberContentDto);
        }
        return attractionMapper.getAttractionByContentId(memberContentDto);
    }

    /**
     * 회원 ID와 컨텐츠 ID를 기준으로 특정 관광지의 상세 정보를 가져온다.
     *
     * @param memberContentDto 회원 ID와 컨텐츠 ID를 포함하는 전송 객체다.
     * @return 상세한 {@link AttractionDto} 정보를 반환한다.
     */
    @Override
    public AttractionDto getAttractionDetail(MemberContentDto memberContentDto) throws SQLException {
        return attractionMapper.getAttractionByContentId(memberContentDto);
    }

    /**
     * 제공된 지역 코드에 따라 페이징 처리된 관광지 목록을 가져온다.
     *
     * @param sidoPgnoDto 페이지 번호와 시도 코드를 포함하는 전송 객체다.
     * @return 페이징 처리된 {@link PaginatedAttractionsDto} 객체를 반환한다.
     */
    @Override
    public PaginatedAttractionsDto getPaginatedAttractionsBySidoCode(SidoPgnoDto sidoPgnoDto) throws Exception {
        int pgNo = Integer.parseInt(sidoPgnoDto.getPgno() == null ? "1" : sidoPgnoDto.getPgno());
        int start = (pgNo - 1) * SizeConstant.LIST_SIZE;

        int sidoCode = Integer.parseInt(sidoPgnoDto.getSidocode());
        List<AttractionDto> attractions = attractionMapper.getPaginatedAttractionsBySidoCode(Map.of(
                "sidoCode", sidoCode,
                "start", start,
                "listsize", SizeConstant.LIST_SIZE
        ));

        int currentPage = Integer.parseInt(sidoPgnoDto.getPgno() == null ? "1" : sidoPgnoDto.getPgno());
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractions = attractionMapper.getTotalAttractionsBySidoCodeCount(Integer.parseInt(sidoPgnoDto.getSidocode()));
        int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;

        return new PaginatedAttractionsDto(attractions, currentPage, totalPageCount);
    }

    /**
     * 회원 ID의 북마크된 관광지 목록을 페이징 처리하여 가져온다.
     *
     * @param memberPgnoDto 회원 ID와 페이지 번호를 포함하는 전송 객체다.
     * @return 페이징 처리된 {@link PaginatedAttractionsDto} 객체를 반환한다.
     */
    @Override
    public PaginatedAttractionsDto getPaginatedAttractionsBookmarked(MemberPgnoDto memberPgnoDto) throws Exception {
        int pgNo = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;

        String memberId = memberPgnoDto.getMemberId();
        List<AttractionDto> attractions = attractionMapper.getPaginatedAttractionsBookmarked(Map.of(
                "memberId", memberId,
                "start", start,
                "listsize", SizeConstant.LIST_SIZE
        ));

        int currentPage = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractions = attractionMapper.getTotalAttractionsBookmarkedCount(memberPgnoDto.getMemberId());
        int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;
        return new PaginatedAttractionsDto(attractions, currentPage, totalPageCount);
    }

    /**
     * 회원 ID의 북마크된 모든 관광지 목록을 가져온다.
     *
     * @param memberId 회원의 식별자다.
     * @return {@link AttractionDto} 객체의 리스트를 반환한다.
     */
    @Override
    public List<AttractionDto> getAllAttractionsBookmarked(String memberId) throws SQLException {
        return attractionMapper.getAllAttractionsBookmarked(memberId);
    }

}
