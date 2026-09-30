package com.ktdsuniversity.edu.replies.vo.response;

import java.util.List;

public class ReplyListVO {
	
	private long replyCount;
	
	private List<RepliesVO> repliyList;
	
	public long getReplyCount() {
		return this.replyCount;
	}

	public void setReplyCount(long replyCount) {
		this.replyCount = replyCount;
	}

	public List<RepliesVO> getRepliyList() {
		return this.repliyList;
	}

	public void setRepliyList(List<RepliesVO> repliyList) {
		this.repliyList = repliyList;
	}
	
}
