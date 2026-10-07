package com.ktdsuniversity.edu.replies.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.ktdsuniversity.edu.commons.exceptions.ArticleException;
import com.ktdsuniversity.edu.commons.exceptions.enums.ArticleCodes;
import com.ktdsuniversity.edu.commons.exceptions.enums.ExceptionType;
import com.ktdsuniversity.edu.files.components.MultipartHandler;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;
import com.ktdsuniversity.edu.replies.dao.RepliesDao;
import com.ktdsuniversity.edu.replies.vo.request.ModifyReplyVO;
import com.ktdsuniversity.edu.replies.vo.request.RegistReplyVO;
import com.ktdsuniversity.edu.replies.vo.response.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.response.ReplyListVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RepliesServiceImpl implements RepliesService {

	private RepliesDao repliesDao;
	private MultipartHandler multipartHandler;
	private static final Logger logger = LoggerFactory.getLogger(RepliesServiceImpl.class);

	@Override
	public ReplyListVO readAllReplies(String articleId) {
		long count = this.repliesDao.selectRepliesCount(articleId);
		List<RepliesVO> replyList = this.repliesDao.selectAllReplies(articleId);

		ReplyListVO list = new ReplyListVO();
		list.setReplyCount(count);
		list.setRepliyList(replyList);
		return list;
	}

	@Override
	public RepliesVO createNewReply(String articleId, RegistReplyVO registReplyVO) {
		String fileSetID = this.multipartHandler.storeFiles(
									registReplyVO.getFile(), registReplyVO.getEmail());
		registReplyVO.setFileSetId(fileSetID);
		
		int insertedRows = this.repliesDao.insertNewReply(articleId, registReplyVO);
		logger.info("{} 개의 row가 생성되었습니다.", insertedRows);
		
		if (insertedRows > 0) {
			return this.repliesDao.selectReplyById(articleId, registReplyVO.getId());
		}
		
		throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.BAD_REQUEST);
	}

	@Override
	public RepliesVO updateReply(String articleId, String replyId, ModifyReplyVO modifyReplyVO) {
		RepliesVO reply = this.repliesDao.selectReplyById(articleId, replyId);
		
		String fileSetId = this.multipartHandler.storeFiles(
				modifyReplyVO.getFile(),
				modifyReplyVO.getEmail(),
				reply.getFileSetId());

		modifyReplyVO.setFileSetId(fileSetId);
		
		int updatedRows = this.repliesDao.updateReplyById(articleId, replyId, modifyReplyVO);
		if (updatedRows == 0) {
			throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.NOT_EXISTS);
			
		}
		
		return this.repliesDao.selectReplyById(articleId, replyId);
	}

	@Override
	public String deleteReply(String articleId, String replyId) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpSession session = request.getSession();
		
		RepliesVO reply = this.repliesDao.selectReplyById(articleId, replyId);
		
		
		MembersVO loggedMember = (MembersVO) session.getAttribute("__LOGIN_USER__");
		if (!loggedMember.getEmail().equals(reply.getEmail())) {
			throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.NOT_AUTHORIZED);
		}
		
		int deleteRows = this.repliesDao.deleteReply(articleId, replyId);
		if (deleteRows == 0) {
			throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.NOT_EXISTS);
		}
		
		int deleteCount = this.multipartHandler.deleteFiles(reply.getFileSetId());
		logger.info("{}개의 파일이 삭제되었습니다.", deleteCount);
		return articleId + replyId;
	}

	@Override
	public long recommendReply(String articleId, String replyId) {
		long getRows = this.repliesDao.updateIncreaseRecommendCount(articleId, replyId);
		if (getRows == 0) {
			throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.NOT_EXISTS);
		}		
		return getRows;
	}

	@Override
	public RepliesVO readReply(String articleId, String replyId) {
		RepliesVO reply = this.repliesDao.selectReplyById(articleId, replyId);
		if (reply == null) {
			throw new ArticleException(ExceptionType.REPLIES, ArticleCodes.NOT_EXISTS);
		}
		return reply;
	}

}
