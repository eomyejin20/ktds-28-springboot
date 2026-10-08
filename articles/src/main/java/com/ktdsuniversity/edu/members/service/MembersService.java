package com.ktdsuniversity.edu.members.service;

import com.ktdsuniversity.edu.articles.vo.request.SearchArticleVO;
import com.ktdsuniversity.edu.members.vo.request.LoginMemberVO;
import com.ktdsuniversity.edu.members.vo.request.RegistMembersVO;
import com.ktdsuniversity.edu.members.vo.request.SearchMemberVO;
import com.ktdsuniversity.edu.members.vo.response.MemberListVO;
//import com.ktdsuniversity.edu.members.vo.response.MemberListVO;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

public interface MembersService {

	/**
	 * 멤버의 목록을 조회한다.
	 * @return (멤버의 총 개수, 멤버 목록)
	 */
	
	
	MemberListVO readAllMembers(SearchMemberVO searchMemberVO);

	MembersVO readMember(LoginMemberVO loginMemberVO);

	MembersVO createNewMember(RegistMembersVO registMembersVO);

	String updateLogoutStatus(String email);

	String deleteMember(String email, String password);
}
