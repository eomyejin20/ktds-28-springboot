package com.ktdsuniversity.edu.members.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.vo.response.MemberListVO;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

@Service
public class MembersServiceImpl implements MembersService{

	private MembersDao membersDao;
	
	public MembersServiceImpl(MembersDao membersDao) {
		this.membersDao = membersDao;
	}
	
	@Override
	public MemberListVO readAllMembers() {
		
		long count = this.membersDao.selectMembersCount();
		List<MembersVO> memberList = this.membersDao.selectALLMembers();
		
		MemberListVO list = new MemberListVO();
		list.setMemberCount(count);
		list.setMemberList(memberList);
		return list;
	}
	
	

}
