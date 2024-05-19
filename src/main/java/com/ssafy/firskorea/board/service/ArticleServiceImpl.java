package com.ssafy.firskorea.board.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.FileDto;
import com.ssafy.firskorea.board.dto.response.ArticleFileDto;
import com.ssafy.firskorea.board.mapper.ArticleMapper;
import com.ssafy.firskorea.util.SizeConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

	private final ArticleMapper articleMapper;

	public ArticleServiceImpl(ArticleMapper articleMapper) {
		super();
		this.articleMapper = articleMapper;
	}
	
	@Value("${articleFile.path.upload-images}")
	private String uploadImagesPath;

	@Override
	public Map<String, Object> getArticles(Map<String, String> map) throws Exception {
		log.debug(map.toString());
		
		Map<String, Object> result = new HashMap<String, Object>();

		// 여행 후기 리스트 가져오기
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("key", map.get("key"));
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
		param.put("start", start);
		param.put("listsize", SizeConstant.LIST_SIZE);

		List<ArticleDto> articles = articleMapper.getArticles(param);
		
		List<ArticleFileDto> articleFiles = new ArrayList<>();
		for (ArticleDto article : articles) {
			ArticleFileDto articleFile = new ArticleFileDto();
			articleFile.setArticleId(article.getId());
			articleFile.setMemberId(article.getMemberId());
			
			Map<String, String> img = new HashMap<>();
			StringBuilder src = new StringBuilder();
			FileDto file = article.getFile();
			src.append(file.getSaveFolder() + "/");
			src.append(file.getSaveFile());
			img.put("src", src.toString());
			img.put("fileName", file.getOriginFile());
			articleFile.setImg(img);
			
			articleFiles.add(articleFile);
		}
		
		result.put("articleFiles", articleFiles);

		// 페이지네비게이션 계산하기
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = SizeConstant.LIST_SIZE;
		int totalArticleCount = articleMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		result.put("currentPage", currentPage);
		result.put("totalPageCount", totalPageCount);

		return result;
	}

	@Override
	public byte[] getArticleFile(String src) throws Exception {
		try {
			InputStream imgStream = new FileInputStream(uploadImagesPath + "/" + src);
			byte[] img = imgStream.readAllBytes();
			imgStream.close();
			
			return img;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

}
