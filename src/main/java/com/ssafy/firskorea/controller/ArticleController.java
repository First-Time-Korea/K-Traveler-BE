package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;
import com.ssafy.firskorea.board.service.ArticleService;
import com.ssafy.firskorea.util.CommentStratify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
public class ArticleController {

	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		super();
		this.articleService = articleService;
	}

	// 여행 후기 작성하기
	@PostMapping("/write")
	public ResponseEntity<Map<String, Object>> writeArticle(@RequestParam("userid") String userId,
			@RequestParam("tags") List<String> tags, @RequestParam("content") String content,
			@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("tags", tags);
		map.put("content", content);
		map.put("file", file);

		articleService.writeArticle(map);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 작성 성공");

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(201).body(response);

		return responseEntity;
	}

	// 여행 후기 리스트 조회하기
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getArticles(@RequestParam Map<String, String> map) throws Exception {
		Map<String, Object> result = articleService.getArticles(map);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 리스트 조회 성공");
		response.put("articles", result.get("articleFiles"));
		response.put("currentPage", result.get("currentPage"));
		response.put("totalPageCount", result.get("totalPageCount"));

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

	// 여행 후기 사진 조회하기
	@GetMapping("/img/{savefolder}/{savefile}")
	public ResponseEntity<byte[]> getArticleFile(@PathVariable("savefolder") String saveFolder,
			@PathVariable("savefile") String saveFile) throws Exception {
		String src = saveFolder + "/" + saveFile;

		byte[] img = articleService.getArticleFile(src);

		if (img == null) {
			return ResponseEntity.notFound().build();
		} else {
			String imgType = src.substring(src.indexOf(".") + 1);
			MediaType mt = null;
			switch (imgType) {
			case "jpg":
				mt = MediaType.IMAGE_JPEG;
				break;
			case "png":
				mt = MediaType.IMAGE_PNG;
				break;
			case "gif":
				mt = MediaType.IMAGE_GIF;
				break;
			}

			ResponseEntity<byte[]> responseEntity = ResponseEntity.status(200).contentType(mt).body(img);

			return responseEntity;
		}
	}
	
	// 여행 후기 조회하기 for 수정
	@GetMapping("/modify/{articleid}")
	public ResponseEntity<Map<String, Object>> getArticleForModification(@PathVariable("articleid") int articleId) throws Exception {
		ArticleDto article = articleService.getArticleForModification(articleId);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 리스트 조회 성공");
		response.put("article", article);

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	// 여행 후기 수정하기
	@PutMapping("modify")
	public ResponseEntity<Map<String, Object>> modifyArticle(@RequestParam("articleid") int articleId,
			@RequestParam("tags") List<String> tags, @RequestParam("content") String content,
			@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("articleId", articleId);
		map.put("tags", tags);
		map.put("content", content);
		map.put("file", file);
		
		articleService.modifyArticle(map);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 수정 성공");

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	// 여행 후기 조회하기
	@GetMapping("detail/{articleid}")
	public ResponseEntity<Map<String, Object>> getArticle(@PathVariable("articleid") int articleId) throws Exception {
		ArticleAndCommentDto ac = articleService.getArticle(articleId);
		if (ac.getComments() != null) {
			CommentStratify.stratify(ac.getComments());
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 조회 성공");
		response.put("article", ac);

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	// 여행 후기 삭제하기
	@DeleteMapping("delete/{articleid}")
	public ResponseEntity<Map<String, Object>> deleteArticle(@PathVariable("articleid") int articleId) throws Exception {
		articleService.deleteArticle(articleId);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 삭제 성공");

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

}
