package com.ktdsuniversity.edu.articles.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ktdsuniversity.edu.articles.dao.ArticlesDao;
import com.ktdsuniversity.edu.articles.vo.request.ModifyArticleVO;
import com.ktdsuniversity.edu.articles.vo.request.RegistArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;
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
//		if (registArticleVO.getFile() != null) {
//			
//			// FILE_SET 생성.
//			RequestFileSetVO fileSetVO = new RequestFileSetVO();
//			fileSetVO.setEmail(registArticleVO.getEmail());
//			
//			int fileSetInsertCount = this.filesDao.insertNewFileSet(fileSetVO);
//			if (fileSetInsertCount == 0) {
//				throw new IllegalArgumentException("파일세트 생성을 할 수 없습니다.");
//			}
//			
//			registArticleVO.setFileSetId( fileSetVO.getId() );
//			
//			for (MultipartFile f: registArticleVO.getFile()) {
//				// 사용자가 업로드한 파일을 서버 컴퓨터에 저장한다.
//				// 1. 저장할 위치 선정.
//				// 사용자 홈 디렉토리 찾기.
//				String homeDirectory = System.getProperty("user.home");
//
//				File uploadFolder = new File(homeDirectory, "uploadFiles");
//				if (!uploadFolder.exists()) {
//					uploadFolder.mkdirs();
//				}
//
//				// 파일이 저장될 위치와 이름 지정하기
////				File storeFile = new File(uploadFolder, f.getOriginalFilename());
//				File storeFile = new File(uploadFolder, UUID.randomUUID().toString() );
//
//				// 2. 파일 저장.
//				try {
//					f.transferTo(storeFile);
//					
//					// FILES 데이터 생성.
//					RequestFileVO fileVO = new RequestFileVO();
//					fileVO.setFileSetId(fileSetVO.getId());
//					fileVO.setDisplayFileName(f.getOriginalFilename());
//					fileVO.setObfuscateFileName( storeFile.getName() );
//					fileVO.setFileSize( storeFile.length() );
//					
//					this.filesDao.insertNewFile(fileVO);
//				} catch (IllegalStateException | IOException e) {
//					throw new IllegalArgumentException(e.getMessage());
//				}
//			}
//		}
		
		String fileSetID = this.multipartHandler.storeFiles(
								registArticleVO.getFile(), 
								registArticleVO.getEmail());
		registArticleVO.setFileSetId(fileSetID);
		
		int insertedRows = this.articlesDao.insertNewArticle(registArticleVO);
		
		// Insert한 게시글의 ID로 게시글 정보를 조회한다.
		// -> Insert한 게시글의 ID가 뭔지 모른다.
		
		System.out.println(insertedRows + "개의 row가 생성되었습니다.");
		
		if (insertedRows > 0) {
			return this.articlesDao.selectArticleByArticleId( registArticleVO.getId() );
		}
		
		throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
	}
	

	@Override
	public ArticlesVO updateArticle(String articleId, ModifyArticleVO modifyArticleVO) {
		// 업로드된 파일이 있는가?
//		if (modifyArticleVO.getFile() != null) {
//			ArticlesVO article = this.articlesDao.selectArticleByArticleId(articleId);
//			modifyArticleVO.setFileSetId(article.getFileSetId());
//			
//			String fileSetId = article.getFileSetId();
//			// 첨부파일이 없는 게시글
//			if (fileSetId == null) {
//				// FILE_SET_ID 생성
//				RequestFileSetVO fileSetVO = new RequestFileSetVO();
//				fileSetVO.setEmail(modifyArticleVO.getEmail());
//				
//				int fileSetInsertCount = this.filesDao.insertNewFileSet(fileSetVO);
//				if (fileSetInsertCount == 0) {
//					throw new IllegalArgumentException("파일세트 생성을 할 수 없습니다.");
//				}
//				
//				modifyArticleVO.setFileSetId(fileSetVO.getId());
//			}
//			
//			for (MultipartFile f: modifyArticleVO.getFile()) {
//				String homeDirectory = System.getProperty("user.home");
//
//				File uploadFolder = new File(homeDirectory, "uploadFiles");
//				if (!uploadFolder.exists()) {
//					uploadFolder.mkdirs();
//				}
//				
//				File storeFile = new File(uploadFolder, UUID.randomUUID().toString() );
//
//				try {
//					f.transferTo(storeFile);
//					
//					// FILES 데이터 생성.
//					RequestFileVO fileVO = new RequestFileVO();
//					fileVO.setFileSetId(modifyArticleVO.getFileSetId());
//					fileVO.setDisplayFileName(f.getOriginalFilename());
//					fileVO.setObfuscateFileName( storeFile.getName() );
//					fileVO.setFileSize( storeFile.length() );
//					
//					this.filesDao.insertNewFile(fileVO);
//				} catch (IllegalStateException | IOException e) {
//					throw new IllegalArgumentException(e.getMessage());
//				}
//			}
//		}
//		
		ArticlesVO article = this.articlesDao.selectArticleByArticleId(articleId);
		
		
		String fileSetId = this.multipartHandler.storeFiles(
								modifyArticleVO.getFile(),
								modifyArticleVO.getEmail(),
								article.getFileSetId());
		
		modifyArticleVO.setFileSetId(fileSetId);
		
		int updatedRows = this.articlesDao.updateArticleId(articleId, modifyArticleVO);
		
		if (updatedRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
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
			throw new IllegalArgumentException("삭제할 수 없는 게시글입니다.");
		}
		
		
		int deleteRows = this.articlesDao.deleteArticle(articleId);
		if (deleteRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}
		
		int deleteCount = this.multipartHandler.deleteFiles(article.getFileSetId());
		System.out.println(deleteCount + "개의 파일이 삭제되었습니다.");
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
