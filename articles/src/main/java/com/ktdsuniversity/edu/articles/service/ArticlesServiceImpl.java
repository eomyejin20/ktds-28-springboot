package com.ktdsuniversity.edu.articles.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ktdsuniversity.edu.articles.dao.ArticlesDao;
import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;
import com.ktdsuniversity.edu.commons.exceptions.ArticleException;
import com.ktdsuniversity.edu.commons.exceptions.enums.ArticleCodes;
import com.ktdsuniversity.edu.commons.exceptions.enums.ExceptionType;
import com.ktdsuniversity.edu.files.components.MultipartHandler;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ArticlesServiceImpl implements ArticlesService {

	private ArticlesDao articlesDao;
//	private FilesDao filesDao;
	private MultipartHandler multipartHandler;
	
	private static final Logger logger = LoggerFactory.getLogger(ArticlesServiceImpl.class);

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
		logger.debug(registArticleVO.toString());
		
		String fileSetID = this.multipartHandler.storeFiles(
								registArticleVO.getFile(), 
								registArticleVO.getEmail());
		registArticleVO.setFileSetId(fileSetID);
		
		int insertedRows = this.articlesDao.insertNewArticle(registArticleVO);
		
		// Insert한 게시글의 ID로 게시글 정보를 조회한다.
		// -> Insert한 게시글의 ID가 뭔지 모른다.
		
		logger.info("{}개의 row가 생성되었습니다.", insertedRows );
		
		if (insertedRows > 0) {
			return this.articlesDao.selectArticleByArticleId( registArticleVO.getId() );
		}
		
		throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.BAD_REQUEST);
	}
	

	@Override
	public ArticlesVO updateArticle(String articleId, ModifyArticleVO modifyArticleVO) {
		ArticlesVO article = this.articlesDao.selectArticleByArticleId(articleId);
		
		
		String fileSetId = this.multipartHandler.storeFiles(
								modifyArticleVO.getFile(),
								modifyArticleVO.getEmail(),
								article.getFileSetId());
		
		modifyArticleVO.setFileSetId(fileSetId);
		
		int updatedRows = this.articlesDao.updateArticleId(articleId, modifyArticleVO);
		
		if (updatedRows == 0) {
			throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.NOT_EXISTS);
		}
		
		return this.articlesDao.selectArticleByArticleId(articleId);
	}

	@Override
	public String deleteArticle(String articleId) {
//		int deleteFiles = this.filesDao.deleteFilesByArticleId(articleId);
		
		// Controller가 아닌 클래스에서 세션 데이터를 자동으로 주입받을 수 없다.
		// 고전적 방법: Controller에서 Service를 호출할 때 파라미터로 세션의 데이터를 전달.
		// 새로운 방법: Spring에서 Session데이터를 가져온다.
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpSession session = request.getSession();
		
		ArticlesVO article = this.articlesDao.selectArticleByArticleId(articleId);
		
		MembersVO loggedMember = (MembersVO) session.getAttribute("__LOGIN_USER__");
		if (!loggedMember.getEmail().equals(article.getEmail())) {
			throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.NOT_AUTHORIZED);
		}
		
		
		int deleteRows = this.articlesDao.deleteArticle(articleId);
		if (deleteRows == 0) {
			throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.NOT_EXISTS);
		}
		
		int deleteCount = this.multipartHandler.deleteFiles(article.getFileSetId());
		logger.info("{개의 파일이 삭제되었습니다.}",deleteCount);
		return articleId;
	}

	@Override
	public ArticlesVO readOneArticle(String articleId) {
		
		int getRows = this.articlesDao.updateIncreaseViewCount(articleId);
		if (getRows == 0) {
			throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.NOT_EXISTS);
		}
		
		
		
		return this.articlesDao.selectArticleByArticleId(articleId);
	}
	
	@Override
	public long recommendOneArticle(String articleId) {
		
		long getRows = this.articlesDao.updateIncreaseRecommendCount(articleId);
		if (getRows == 0) {
			throw new ArticleException(ExceptionType.ARTICLES, ArticleCodes.NOT_EXISTS);
		}
		
		return getRows;
	}

}
