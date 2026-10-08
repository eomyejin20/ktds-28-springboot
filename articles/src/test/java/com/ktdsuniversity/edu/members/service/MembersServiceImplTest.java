package com.ktdsuniversity.edu.members.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.vo.request.RegistMembersVO;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

@SpringBootTest // Spring이 생성하고 관리하는 Bean을 자동 주입 받기 위한 애노테이션
//@ExtendWith(SpringExtension.class) // JNIT5 사용 명시 @@SpringBootTest가 상속받음
//@Import({MembersDao.class, MembersServiceImpl.class}) // MembersServiceImpl <-- 주입이 필요한 Bean
public class MembersServiceImplTest {

	// SpringBootTest와 Import가 준비한 bean을 주입받는다.
	@Autowired
	private MembersService membersService;
	
	@MockitoBean
	private MembersDao membersDao;
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void testCreateNewMember() {
		
		RegistMembersVO registMembersVO = new RegistMembersVO();
		registMembersVO.setEmail("test@gmail.com");
		registMembersVO.setName("TestUser");
		registMembersVO.setNickname("TestNickName");
		registMembersVO.setPassword("test_password");
		
		// Given(Dao에게 역할 부여) → When(테스트) → Then(테스트결과)
		// MembersDao.selectEmailCount에게 "test@gmail.com"이 전달되면, 0을 반환하도록 역할 부여
		BDDMockito.given(this.membersDao.selectEmailCount("test@gmail.com"))
				  .willReturn(0);
		
		BDDMockito.given(this.membersDao.selectNicknameCount("TestNickName"))
				  .willReturn(0);
		
		BDDMockito.given(this.membersDao.insertNewMember(registMembersVO))
		  		  .willReturn(1);
		
		MembersVO returnedMember = new MembersVO();
		
		BDDMockito.given(this.membersDao.selectMemberByEmail("test@gmail.com"))
		  		  .willReturn(returnedMember);
		
		// When
		MembersVO membersVO  = this.membersService.createNewMember(registMembersVO);
		System.out.println("membersVO => " + membersVO); //membersVO => MembersVO(email=null, name=null, nickname=null, password=null, registDate=null, modifyDate=null, latestLoginSuccessDate=null, latestLoginFailDate=null, latestLoginDate=null, loginFailCount=null, loginBlockYn=null, loginBlockDate=null, loginYn=null, salt=null, delYn=null)
		System.out.println("registMembersVO => " + registMembersVO); //registMembersVO => RegistMembersVO(email=test@gmail.com, name=3bedc4930e3e0b16d09f2966b2081607, nickname=b8ac3e69d413e64ea66d6f74accf220f, password=0c9b2a20364bec015c9c7a8d9ae0038a2154f2ca7afd07642c64282eb0805a13, salt=4c27db88e0f1eb07)
		

		// Then
//		1. 반환 값이 올바른지
		assertNotNull(membersVO);
		assertEquals(membersVO, returnedMember); // 메모리 동일
//		2. 비밀번호 암호화
		assertNotNull(registMembersVO.getSalt());
		assertNotEquals("test_password", registMembersVO.getPassword());
		
		
		
	}
}
