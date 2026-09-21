package com.ktdsuniversity.edu.articles.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.articles.dao.ArticlesDao;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;

@Service
public class ArticlesServiceImpl implements ArticlesService{
	
	private ArticlesDao articlesDao;
	
	public ArticlesServiceImpl(ArticlesDao articlesDao) {
		this.articlesDao = articlesDao;
	}

	@Override
	public ArticleListVO readAllArticles() {
		
		long count = this.articlesDao.selectArticlesCount();
		List<ArticlesVO> articleList = this.articlesDao.selectAllArticles();
		
		ArticleListVO list = new ArticleListVO();
		list.setArticleCount(count);
		list.setArticleList(articleList);
		return list;
	}
	
	

}
