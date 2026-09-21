package com.ktdsuniversity.edu.members.service;

import com.ktdsuniversity.edu.members.vo.response.MemberListVO;

public interface MembersService {

	/**
	 * 멤버의 목록을 조회한다.
	 * @return (멤버의 총 개수, 멤버 목록)
	 */
	MemberListVO readAllMembers();
}
