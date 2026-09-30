package com.ktdsuniversity.edu.replies.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ktdsuniversity.edu.replies.vo.request.ModifyReplyVO;
import com.ktdsuniversity.edu.replies.vo.request.RegistReplyVO;
import com.ktdsuniversity.edu.replies.vo.response.RepliesVO;

@Mapper
public interface RepliesDao {

	long selectRepliesCount(String articleId);

	List<RepliesVO> selectAllReplies(String articleId);

	int insertNewReply(@Param("articleId")String articleId, @Param("registReplyVO") RegistReplyVO registReplyVO);

	RepliesVO selectReplyById(@Param("articleId") String articleId, @Param("replyId") String replyId);

	int updateReplyById(@Param("articleId") String articleId, @Param("replyId") String replyId, @Param("modifyReplyVO") ModifyReplyVO modifyReplyVO);

	int deleteReply(@Param("articleId") String articleId, @Param("replyId") String replyId);

	long updateIncreaseRecommendCount(@Param("articleId") String articleId, @Param("replyId") String replyId);

}
