package com.ktdsuniversity.edu.members.vo.response;

import java.util.List;

public class MemberListVO {
	
	private long memberCount;
	private List<MembersVO> memberList;
	
	
	public long getMemberCount() {
		return this.memberCount;
	}
	public void setMemberCount(long memberCount) {
		this.memberCount = memberCount;
	}
	public List<MembersVO> getMemberList() {
		return this.memberList;
	}
	public void setMemberList(List<MembersVO> memberList) {
		this.memberList = memberList;
	}

}
