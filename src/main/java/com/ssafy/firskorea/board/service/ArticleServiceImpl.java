package com.ssafy.firskorea.board.service;

import java.io.File;
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
import com.ssafy.firskorea.board.dto.request.SearchDto;
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

	/**
	 * 사용자 ID, 태그, 내용 그리고 사진을 토대로 여행 후기를 생성한다.
	 * 
	 * @param map 태그, 내용 그리고 사진을 포함하는 전송 객체다.
	 */
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

	/**
	 * 태그 또는 작성자 기준으로 여행 후기를 조회한다.
	 * 
	 * @param 페이지네이션 및 여행 후기 검색을 위한 정보(태그, 작성자)를 포함하는 전송 객체다.
	 * @return 페이징 처리된 조회 결과인 {@link ArticleDto} 객체의 리스트를 반환한다.
	 */
	@Override
	@Transactional
	public Map<String, Object> getArticles(SearchDto search) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		// 여행 후기 리스트 가져오기
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("key", search.getKey());
		param.put("word", search.getWord() == null ? "" : search.getWord());
		int pgNo = search.getPgNo() == 0 ? 1 : search.getPgNo();
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

		// 페이지네이션 계산하기
		int currentPage = pgNo;
		int sizePerPage = SizeConstant.LIST_SIZE;
		int totalArticleCount = articleMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		result.put("currentPage", currentPage);
		result.put("totalPageCount", totalPageCount);

		return result;
	}
	
	/**
	 * 여행 후기 ID를 기준으로 특정 여행 후기의 상세 정보 및 해당 여행 후기의 댓글 리스트를 조회한다.
	 * 
	 * @param articleId 여행 후기의 식별자다.
	 * @return {@link ArticleAndCommentDto} 객체를 반환한다.
	 */
	@Override
	@Transactional
	public ArticleAndCommentDto getArticle(int articleId) throws Exception {
		return articleMapper.getArticle(articleId);
	}

	/**
	 * 여행 후기 ID를 기준으로 특정 여행 후기의 상세 정보를 조회한다.
	 * 
	 * @param articleId 여행 후기의 식별자다.
	 * @return {@link ArticleDto} 객체를 반환한다.
	 */
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

	/**
	 * 여행 후기 ID에 해당하는 여행 후기 상세 정보를 수정한다.
	 * 
	 * @param map 여행 후기 ID, 태그, 내용 그리고 사진을 포함하는 전송 객체다.
	 */
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
		
		if (map.containsKey("content")) {
			articleMap.put("content", map.get("content"));
	
			articleMapper.modifyArticle(articleMap);
		}

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
		if (map.containsKey("file")) {
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
	}

	/**
	 * 여행 후기 ID에 해당하는 여행 후기를 삭제한다.
	 * 
	 * @param articleId 여행 후기의 식별자다.
	 */
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
