package com.ktdsuniversity.edu.replies.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
		
		throw new IllegalArgumentException("입력값이 유효하지 않습니다.");
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
			throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
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
			throw new IllegalArgumentException("삭제할 수 없는 댓글입니다.");
		}
		
		int deleteRows = this.repliesDao.deleteReply(articleId, replyId);
		if (deleteRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
		}
		
		int deleteCount = this.multipartHandler.deleteFiles(reply.getFileSetId());
		logger.info("{}개의 파일이 삭제되었습니다.", deleteCount);
		return articleId + replyId;
	}

	@Override
	public long recommendReply(String articleId, String replyId) {
		long getRows = this.repliesDao.updateIncreaseRecommendCount(articleId, replyId);
		if (getRows == 0) {
			throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
		}		
		return getRows;
	}

	@Override
	public RepliesVO readReply(String articleId, String replyId) {
		RepliesVO reply = this.repliesDao.selectReplyById(articleId, replyId);
		if (reply == null) {
			throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
		}
		return reply;
	}

}
