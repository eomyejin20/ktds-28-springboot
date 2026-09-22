package com.ktdsuniversity.edu.articles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.articles.dao.ArticlesDao;
import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ArticlesServiceImpl implements ArticlesService {

	private ArticlesDao articlesDao;

//	public ArticlesServiceImpl(ArticlesDao articlesDao) {
//		this.articlesDao = articlesDao;
//	}

	@Override
	public ArticleListVO readAllArticles() {
		ArticlesVO Test = new ArticlesVO();

		long count = this.articlesDao.selectArticlesCount();
		List<ArticlesVO> articleList = this.articlesDao.selectAllArticles();

		ArticleListVO list = new ArticleListVO();
		list.setArticleCount(count);
		list.setArticleList(articleList);
		return list;
	}

	@Override
	public ArticlesVO createNewArticle(RegistArticleVO registArticleVO) {
		int insertedRows = this.articlesDao.insertNewArticle(registArticleVO);
		System.out.println(insertedRows + "개가 만들어졌습니다.");

		if (insertedRows > 0) {
			return this.articlesDao.selectArticleByArticleId(registArticleVO.getId());
		}
		// Insert한 게시글의 Id로 게시글 정보 조회
		// -> Id는 무엇인가?
		throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
	}

	@Override
	public ArticlesVO updateArticle(String articleId, ModifyArticleVO modifyArticleVO) {
		int updatedRows = this.articlesDao.updateArticleId(articleId, modifyArticleVO);
		
		if(updatedRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		return this.articlesDao.selectArticleByArticleId(articleId);
	}

	@Override
	public String deleteArticle(String articleId) {
		int deleteRows = this.articlesDao.deleteArticle(articleId);
		
		if (deleteRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		return articleId;
	}

	@Override
	public ArticlesVO readOneArticle(String articleId) {
		
		int getRows = this.articlesDao.updateIncreaseViewCount(articleId);
		if (getRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		return this.articlesDao.selectArticleByArticleId(articleId);
	}
	
	@Override
	public long recommendOneArticle(String articleId) {
		
		long getRows = this.articlesDao.updateIncreaseRecommendCount(articleId);
		if (getRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		return getRows;
	}

}
