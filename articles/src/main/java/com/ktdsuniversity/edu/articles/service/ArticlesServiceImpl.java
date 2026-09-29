package com.ktdsuniversity.edu.articles.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.articles.dao.ArticlesDao;
import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;
import com.ktdsuniversity.edu.files.dao.FilesDao;
import com.ktdsuniversity.edu.files.vo.request.RequestFileSetVO;
import com.ktdsuniversity.edu.files.vo.request.RequestFileVO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ArticlesServiceImpl implements ArticlesService {

	private ArticlesDao articlesDao;
	private FilesDao filesDao;

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
			if (registArticleVO.getFile() != null) {
				
				//fileset생성
				RequestFileSetVO fileSetVO = new RequestFileSetVO();
				fileSetVO.setEmail(registArticleVO.getEmail());
				int fileSetInsertCount = this.filesDao.insertNewFileSet(fileSetVO);
				
				if (fileSetInsertCount == 0) {
					throw new IllegalArgumentException("파일 세트를 생성할 수 없습니다.");
				}
				
				// fileSetId값을 전달
				registArticleVO.setFileSetId(fileSetVO.getId());
				
				for (MultipartFile f: registArticleVO.getFile()) {
					String homeDirectory = System.getProperty("user.home");
					   
					   File uploadFolder = new File(homeDirectory, "uploadFiles");
					   if (!uploadFolder.exists()) {
						   uploadFolder.mkdirs();
					   }
					   
					   // 파일이 저장될 위치와 이름 지정하기
					   File storeFile = new File(uploadFolder, f.getOriginalFilename());
					   
					   // 2. 파일 저장.
					   try {
						   f.transferTo(storeFile);
						   
						   // FILES 데이터 생성
						   RequestFileVO fileVO = new RequestFileVO();
						   fileVO.setFileSetId(fileSetVO.getId());
						   fileVO.setDisplayFileName(f.getOriginalFilename());
						   fileVO.setObfuscateFileName(storeFile.getName());
						   fileVO.setFileSize(storeFile.length());
						   
						   this.filesDao.insertNewFile(fileVO);
						   
					   } catch (IllegalStateException | IOException e) {
						   throw new IllegalArgumentException(e.getMessage());
					   }
				}
			}
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
