package com.ktdsuniversity.edu.members.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.members.vo.request.RegistMembersVO;
import com.ktdsuniversity.edu.members.vo.request.SearchMemberVO;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

@Mapper
public interface MembersDao {
	
//	long selectMembersCount();
//	
//	List<MembersVO> selectALLMembers();
	
	int selectEmailCount(String email);

	int selectNicknameCount(String nickname);

	int insertNewMember(RegistMembersVO registMembersVO);

	MembersVO selectMemberByEmail(String email);

	int updateLoginStatus(String email);

	int updateLoginFailed(String email);

	int updateBlock(String email);

	int updateResetBlock(String email);

	int updateLogoutStatus(String email);

	int deleteMember(String email);

	long selectMemberCount(SearchMemberVO searchMemberVO);

	List<MembersVO> selectAllMembers(SearchMemberVO searchMemberVO);

}
