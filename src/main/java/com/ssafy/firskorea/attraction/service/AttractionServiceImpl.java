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

    @Autowired
    private AttractionMapper attractionMapper;

    @Override
    public List<ThemeDto> getThemeList() throws SQLException {
        return attractionMapper.getThemeList();
    }

    @Override
    public List<Category> getCategoryList(Character code) throws SQLException {
        return attractionMapper.getCategoryList(code);
    }

    @Override
    public List<Category> getSidoList() throws SQLException {
        return attractionMapper.getSidoList();
    }

    @Override
    public List<AttractionDto> getAttractionsBySearch(SearchDto searchDto) throws SQLException {
        return attractionMapper.getAttractionBySearch(searchDto);
    }

    @Override
    @Transactional
    public AttractionDto toggleAttractionBookmark(MemberContentDto memberContentDto) throws SQLException {
        AttractionDto dto = attractionMapper.getAttractionByContentId(memberContentDto);
        if (dto.getBookmarkId() > 0) {
            attractionMapper.deleteBookmark(memberContentDto);
        } else {
            attractionMapper.createBookmark(memberContentDto);
        }
        return attractionMapper.getAttractionByContentId(memberContentDto);
    }

    @Override
    public AttractionDto getAttractionDetail(MemberContentDto memberContentDto) throws SQLException {
        return attractionMapper.getAttractionByContentId(memberContentDto);
    }

    @Override
    public PaginatedAttractionsDto getPaginatedAttractionsBySidoCode(SidoPgnoDto sidoPgnoDto) throws Exception {
        int pgNo = Integer.parseInt(sidoPgnoDto.getPgno() == null ? "1" : sidoPgnoDto.getPgno());
        int start = (pgNo - 1) * SizeConstant.LIST_SIZE;

        int sidoCode = Integer.parseInt(sidoPgnoDto.getSidocode());
        List<AttractionDto> attractions = attractionMapper.getAttractionsBySidoCode(Map.of(
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

    @Override
    public PaginatedAttractionsDto getPaginatedAttractionsBookmarked(MemberPgnoDto memberPgnoDto) throws Exception {
        int pgNo = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;

        String memberId = memberPgnoDto.getMemberId();
        List<AttractionDto> attractions = attractionMapper.getBookmarkedAttractionInfos(Map.of(
                "memberId", memberId,
                "start", start,
                "listsize", SizeConstant.LIST_SIZE
        ));

        int currentPage = Integer.parseInt(memberPgnoDto.getPgno() == null ? "1" : memberPgnoDto.getPgno());
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractions = attractionMapper.getTotalBookmarkedAttractionCount(memberPgnoDto.getMemberId());
        int totalPageCount = (totalAttractions - 1) / sizePerPage + 1;
        return new PaginatedAttractionsDto(attractions, currentPage, totalPageCount);
    }

    @Override
    public List<AttractionDto> getAllAttractionsBookmarked(String memberId) throws SQLException {
        return attractionMapper.getBookmarkedAttractionList(memberId);
    }

}
