package com.ssafy.firskorea.board.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.FileDto;
import com.ssafy.firskorea.board.dto.TagDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;
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
	@Transactional
	public void writeArticle(Map<String, Object> map) throws Exception {
		// 여행 후기 태그 확인 및 생성하기
		List<String> tags = (List<String>) map.get("tags");

		List<Integer> tagIds = new ArrayList<>();
		for (String tag : tags) {
			// 여행 후기 태그 확인
			Integer id = articleMapper.getArticleTagId(tag);

			if (id != null) {
				tagIds.add(id);
			} else {
				TagDto t = new TagDto();
				t.setName(tag);
				articleMapper.writeArticleTag(t);

				tagIds.add(t.getId());
			}
		}

		// 여행 후기 생성하기
		ArticleDto article = new ArticleDto();
		article.setMemberId((String) map.get("userId"));
		article.setContent((String) map.get("content"));

		articleMapper.writeArticle(article);
		int articleId = article.getId();

		// 여행 후기랑 태그 관계 형성하기
		Map<String, Object> m = new HashMap<>();
		m.put("articleId", articleId);
		m.put("articleTagIds", tagIds);
		articleMapper.connectArticleAndTag(m);

		// 여행 후기 사진 서버 저장 후 DB에 정보 저장하기
		String today = new SimpleDateFormat("yyMMdd").format(new Date());
		String saveFolder = uploadImagesPath + File.separator + today;

		File folder = new File(saveFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		MultipartFile mfile = (MultipartFile) map.get("file");
		FileDto file = new FileDto();
		String originalFileName = mfile.getOriginalFilename();
		if (!originalFileName.isEmpty()) {
			String saveFileName = UUID.randomUUID().toString()
					+ originalFileName.substring(originalFileName.lastIndexOf('.'));
			file.setArticleId(articleId);
			file.setSaveFolder(today);
			file.setOriginFile(originalFileName);
			file.setSaveFile(saveFileName);
			mfile.transferTo(new File(folder, saveFileName));
		}

		articleMapper.writeArticleFile(file);
	}

	@Override
	@Transactional
	public Map<String, Object> getArticles(Map<String, String> map) throws Exception {
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

	@Override
	@Transactional
	public ArticleDto getArticleForModification(int articleId) throws Exception {
		// 여행 후기 조회하기
		ArticleDto article = articleMapper.getArticleForModification(articleId);

		// 여행 후기의 태그 조회하기
		List<TagDto> tags = articleMapper.getTagsOfArticle(articleId);
		article.setTags(tags);

		return article;
	}

	@Override
	@Transactional
	public void modifyArticle(Map<String, Object> map) throws Exception {
		// 여행 후기 태그 확인 및 생성하기
		List<String> tags = (List<String>) map.get("tags");

		List<Integer> tagIds = new ArrayList<>();
		for (String tag : tags) {
			// 여행 후기 태그 확인
			Integer id = articleMapper.getArticleTagId(tag);

			if (id != null) {
				tagIds.add(id);
			} else {
				TagDto t = new TagDto();
				t.setName(tag);
				articleMapper.writeArticleTag(t);

				tagIds.add(t.getId());
			}
		}

		// 여행 후기 수정하기
		int articleId = (Integer) map.get("articleId");
		Map<String, Object> articleMap = new HashMap<>();
		articleMap.put("articleId", articleId);
		articleMap.put("content", map.get("content"));

		articleMapper.modifyArticle(articleMap);

		// 여행 후기랑 태그 관계 삭제 및 형성하기
		List<TagDto> registedTags = articleMapper.getTagsOfArticle(articleId);
		List<Integer> registedTagIds = new ArrayList<>();
		List<Integer> removedTagIds = new ArrayList<>();
		for (TagDto registedTag : registedTags) {
			// 삭제 후보로 등록하기
			if (!tags.contains(registedTag.getName())) {
				removedTagIds.add(registedTag.getId());
			}

			// 이미 관계가 맺어진 태그 아이디 가져오기
			registedTagIds.add(registedTag.getId());
		}

		// 없어진 여행 후기랑 태그 관계 삭제하기
		if (removedTagIds.size() > 0) {
			Map<String, Object> removedTagMap = new HashMap<>();
			removedTagMap.put("articleId", articleId);
			removedTagMap.put("articleTagIds", removedTagIds);

			articleMapper.disconnectArticleAndTag(removedTagMap);
		}

		// 새로 생긴 여행 후기랑 태그 관계 형성하기
		List<Integer> createdTagIds = new ArrayList<>();
		for (Integer tagId : tagIds) {
			if (!registedTagIds.contains(tagId)) {
				createdTagIds.add(tagId);
			}
		}

		if (createdTagIds.size() > 0) {
			Map<String, Object> createdTagMap = new HashMap<>();
			createdTagMap.put("articleId", articleId);
			createdTagMap.put("articleTagIds", createdTagIds);

			articleMapper.connectArticleAndTag(createdTagMap);
		}

		// 여행 후기 사진 서버 저장 후 DB에 정보 저장하기
		MultipartFile mfile = (MultipartFile) map.get("file");
		if (!mfile.isEmpty()) {
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			String saveFolder = uploadImagesPath + File.separator + today;

			File folder = new File(saveFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			FileDto file = new FileDto();
			String originalFileName = mfile.getOriginalFilename();
			if (!originalFileName.isEmpty()) {
				String saveFileName = UUID.randomUUID().toString()
						+ originalFileName.substring(originalFileName.lastIndexOf('.'));
				file.setArticleId(articleId);
				file.setSaveFolder(today);
				file.setOriginFile(originalFileName);
				file.setSaveFile(saveFileName);
				mfile.transferTo(new File(folder, saveFileName));
			}

			articleMapper.writeArticleFile(file);
		}
	}

	@Override
	@Transactional
	public ArticleAndCommentDto getArticle(int articleId) throws Exception {
		return articleMapper.getArticle(articleId);
	}

	@Override
	@Transactional
	public void deleteArticle(int articleId) throws Exception {
		// 여행 후기랑 태그 관계 제거하기
		Map<String, Object> map = new HashMap<>();
		map.put("articleId", articleId);
		articleMapper.disconnectArticleAndTag(map);
		
		// 여행 사진 제거하기
		articleMapper.deleteArticleFile(articleId);
		
		// 여행 후기 제거하기
		articleMapper.deleteArticle(articleId);
	}

}
