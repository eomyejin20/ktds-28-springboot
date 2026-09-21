package com.ktdsuniversity.edu.members.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ktdsuniversity.edu.members.vo.response.MembersVO;

@Mapper
public interface MembersDao {
	
	long selectMembersCount();
	
	List<MembersVO> selectALLMembers();

}
