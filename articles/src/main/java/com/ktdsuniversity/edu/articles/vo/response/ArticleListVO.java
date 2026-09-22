package com.ktdsuniversity.edu.articles.vo.response;

import java.util.List;

public class ArticleListVO {

	/**
	 * 검색된 게시글의 총 개수
	 */
	private long articleCount;
	
	private List<ArticlesVO> articleList;
	
	public long getArticleCount() {
		return this.articleCount;
	}

	public void setArticleCount(long articleCount) {
		this.articleCount = articleCount;
	}

	public List<ArticlesVO> getArticleList() {
		return this.articleList;
	}

	public void setArticleList(List<ArticlesVO> articleList) {
		this.articleList = articleList;
	}
	
	
}
