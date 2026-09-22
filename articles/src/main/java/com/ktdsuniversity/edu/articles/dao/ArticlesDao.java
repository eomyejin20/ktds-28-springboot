package com.ktdsuniversity.edu.articles.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;

/**
 * Spring @Respository 를 한 번 감싼 애노테이션.
 * mabatis가 익명의 클래스를 만들어서 데이터베이스에 접근하도록 한다.
 */
@Mapper
public interface ArticlesDao {

	/**
	 * 게시글의 총 개수를 반환
	 * @return
	 */
	long selectArticlesCount();
	
	/**
	 * 게시글을 검색해서 반환
	 * @return
	 */
	List<ArticlesVO> selectAllArticles();
	
	/**
	 *  클라이언트가 보내준 게시글 등록 정보를 데이터베이스에 insert한다
	 * @param regisArticleVO (제목 내용 이메일)
	 * @return insert row의 개수
	 */
	int insertNewArticle(RegistArticleVO registArticleVO);
	
	
	/**
	 * 게시글의 아이디로 게시글 정보를 조회한다.
	 * @param articleId 게시글의 PK
	 * @return 게시글의 PK로 조회한 게시글정보
	 */
	ArticlesVO selectArticleByArticleId(String articleId);

	int updateArticleId(@Param("articleId") String articleId, @Param("modifyArticleVO") ModifyArticleVO modifyArticleVO);
	
	//게시글 삭제
	int deleteArticle(String articleId);
}















