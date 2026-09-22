package com.ktdsuniversity.edu.articles.service;

import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;

public interface ArticlesService {
	
	/**
	 * 게시글의 목록을 조회
	 * @return (게시글 개수, 게시글 목록)
	 */
	ArticleListVO readAllArticles();

	/**
	 * 게시글 생성
	 * 	 * @param registArticleVO 클라이언트가 보내준 게시글 등록 정보(제목, 내용 이메일)
	 */
	ArticlesVO createNewArticle(RegistArticleVO registArticleVO);

	/**
	 * 게시글 수정
	 * @param articleId 수정하려는 게시글의 아이디
	 * @param modifyArticleVO 수정하려는 내용
	 * @return 수정된 결과
	 */
	ArticlesVO updateArticle(String articleId, ModifyArticleVO modifyArticleVO);

	/**
	 * 게시글 삭제
	 * @param articleId
	 * @return 삭제한 게시글 아이디
	 */
	String deleteArticle(String articleId);

	
	/**
	 * 게시글 1개 조회
	 * @param articleId
	 * @return 조회한 게시글 1개 (조회수 증가된)
	 */
	ArticlesVO readOneArticle(String articleId);

	/**
	 * 추천수 1 증가
	 * @param articleId
	 * @return
	 */
	long recommendOneArticle(String articleId);
}
