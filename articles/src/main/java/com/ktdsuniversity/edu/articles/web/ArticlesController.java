package com.ktdsuniversity.edu.articles.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.articles.service.ArticlesService;
import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;
import com.ktdsuniversity.edu.commons.util.ApiResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class ArticlesController {
	
//	/**
//	 * BeanContainer에서 같은 타입의 객체가 있다면 그것을 멤버변수에게 할당시켜라
//	 */
//	@Autowired
//	/**
//	 * BeamContainer에 ArticlesService타입의 객체가 여러 개 있을 경우,
//	 * 객체명이 "articlesServiceImpl"인 객체를 멤버변수에 할당시켜라
//	 */
//	@Qualifier("articlesService")
	
	private ArticlesService articlesService;
	
	/**
	 * Spring Framework 7.0 이상
	 * Spring Boot 4.0 이상에서는 @Autowired 사용을 권장하지 않는다.
	 * 
	 * 대신, 생성자를 이용한 DI를 권장한다.
	 * ==> 이유: Lombok Library때문 ...(Getter, Setter, 생성자, toString 자동생성)
	 */
//	public ArticlesController(ArticlesService articlesService) {
//		this.articlesService = articlesService;
//	}
	
	@GetMapping("/articles")
	//컨트롤러가 반환시키는 "객체"를 "JSON"으로 변환시키는 View를 사용해라!
	@ResponseBody
	public ApiResponse<ArticleListVO> getArticles() {
//		System.out.println(this.articlesService);
		ArticleListVO result = this.articlesService.readAllArticles();
		
		return ApiResponse.OK(result);
	}
	
	@PostMapping("/articles")
	@ResponseBody
	public ApiResponse<ArticlesVO> makeNewArticle(@RequestBody RegistArticleVO registArticleVO) {
		 ArticlesVO result = this.articlesService.createNewArticle(registArticleVO);
		return ApiResponse.CREATED(result);
	}
	
	@PutMapping("/articles/{articleId}")
	@ResponseBody
	public ApiResponse<ArticlesVO> updateArticle(@PathVariable String articleId, 
									@RequestBody ModifyArticleVO modifyArticleVO) {
		ArticlesVO result = this.articlesService.updateArticle(articleId, modifyArticleVO);
		return ApiResponse.OK(result);
	}
	

	/**
	 * 게시글 삭제
	 * @param articleId
	 * @return
	 */
	@DeleteMapping("/articles/{articleId}")
	@ResponseBody
	public ApiResponse<String> deleteArticle(@PathVariable String articleId) {
		String deleteResult = this.articlesService.deleteArticle(articleId);
		return ApiResponse.OK(deleteResult);
	}
	
	/**
	 * 게시글 1개 조회
	 * @param articleId
	 * @return
	 */
	@GetMapping("/articles/{articleId}")
	@ResponseBody
	public ApiResponse<ArticlesVO> getOneArticle(@PathVariable String articleId) {
		ArticlesVO result = this.articlesService.readOneArticle(articleId);
		return ApiResponse.OK(result);
	}
	
	/**
	 * 게시글 추천수 1 증가
	 * @param articleId
	 * @return
	 */
	@PutMapping("/articles/recommend/{articleId}")
	@ResponseBody
	public ApiResponse<Long> recommendOneArticle(@PathVariable String articleId) {
		long recommendResult = this.articlesService.recommendOneArticle(articleId);
		return ApiResponse.OK(recommendResult);
	}
	

}
