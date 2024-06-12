package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.request.MemberPgnoDto;
import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.request.SidoPgnoDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.PaginatedAttractionsDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

public interface AttractionService {

    List<ThemeDto> getThemeList() throws SQLException;

    List<Category> getCategoryList(Character code) throws SQLException;

    List<Category> getSidoList() throws SQLException;

    List<AttractionDto> getAttractionsBySearch(SearchDto searchDto) throws SQLException;

    AttractionDto toggleAttractionBookmark(MemberContentDto memberContentDto) throws SQLException;

    AttractionDto getAttractionDetail(MemberContentDto memberContentDto) throws SQLException;

    PaginatedAttractionsDto getPaginatedAttractionsBySidoCode(SidoPgnoDto sidoPgnoDto) throws SQLException;

    PaginatedAttractionsDto getPaginatedAttractionsBookmarked(MemberPgnoDto memberPgnoDto) throws SQLException;

    List<AttractionDto> getAllAttractionsBookmarked(String memberId) throws SQLException;
}
