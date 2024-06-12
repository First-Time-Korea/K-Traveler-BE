package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.AttractionIdentityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.BookmarkedAttractionInfoDto;
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
    public List<AttractionDto> getAttractionBySearch(SearchDto searchDto) throws SQLException {
        return attractionMapper.getAttractionBySearch(searchDto);
    }

    @Override
    @Transactional
    public AttractionDto toggleBookmark(AttractionIdentityDto attractionIdentityDto) throws SQLException {
        AttractionDto dto = attractionMapper.getAttractionByContentId(attractionIdentityDto);
        if (dto.getBookmarkId() > 0) {
            attractionMapper.deleteBookmark(attractionIdentityDto);
        } else {
            attractionMapper.createBookmark(attractionIdentityDto);
        }
        return attractionMapper.getAttractionByContentId(attractionIdentityDto);
    }

    @Override
    public AttractionDto getAttractionById(AttractionIdentityDto attractionIdentityDto) throws SQLException {
        return attractionMapper.getAttractionByContentId(attractionIdentityDto);
    }

    public Map<String, Object> getAttractionsBySidoCode(Map<String, String> map) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        // 여행지 정보 리스트 가져오기
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sidoCode", Integer.parseInt(map.get("sidocode")));
        int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
        param.put("start", start);
        param.put("listsize", SizeConstant.LIST_SIZE);

        List<AttractionDto> attractions = attractionMapper.getAttractionsBySidoCode(param);

        result.put("attractions", attractions);

        // 페이지네비게이션 계산하기
        int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalAttractionsBySidoCode = attractionMapper.getTotalAttractionsBySidoCodeCount(Integer.parseInt(map.get("sidocode")));
        int totalPageCount = (totalAttractionsBySidoCode - 1) / sizePerPage + 1;

        result.put("currentPage", currentPage);
        result.put("totalPageCount", totalPageCount);

        return result;
    }

    @Override
    public List<AttractionDto> getBookmarkedAttractionList(String memberId) throws SQLException {
        return attractionMapper.getBookmarkedAttractionList(memberId);
    }

    @Override
    public List<Category> getSidoList() throws SQLException {
        return attractionMapper.getSidoList();
    }

    @Override
    public Map<String, Object> getBookmarkedAttractionInfos(Map<String, String> map) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        // 여행지 정보 리스트 가져오기
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("memberId", map.get("memberId"));
        int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
        param.put("start", start);
        param.put("listsize", SizeConstant.LIST_SIZE);

        List<BookmarkedAttractionInfoDto> bookmarkedAttractionInfos = attractionMapper.getBookmarkedAttractionInfos(param);

        result.put("bookmarkedAttractionInfos", bookmarkedAttractionInfos);

        // 페이지네비게이션 계산하기
        int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int sizePerPage = SizeConstant.LIST_SIZE;
        int totalBookmarkedAttractionCount = attractionMapper.getTotalBookmarkedAttractionCount(map.get("memberId"));
        int totalPageCount = (totalBookmarkedAttractionCount - 1) / sizePerPage + 1;

        result.put("currentPage", currentPage);
        result.put("totalPageCount", totalPageCount);

        return result;
    }
}
