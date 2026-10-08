package com.ktdsuniversity.edu.members.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.ktdsuniversity.edu.articles.vo.request.SearchArticleVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticleListVO;
import com.ktdsuniversity.edu.articles.vo.response.ArticlesVO;
import com.ktdsuniversity.edu.commons.crypto.AES;
import com.ktdsuniversity.edu.commons.crypto.encrypt.hash.SHA;
import com.ktdsuniversity.edu.commons.exceptions.ArticleException;
import com.ktdsuniversity.edu.commons.exceptions.enums.ArticleCodes;
import com.ktdsuniversity.edu.commons.exceptions.enums.ExceptionType;
import com.ktdsuniversity.edu.files.service.FilesServiceImpl;
import com.ktdsuniversity.edu.members.dao.MembersDao;
import com.ktdsuniversity.edu.members.vo.request.LoginMemberVO;
import com.ktdsuniversity.edu.members.vo.request.RegistMembersVO;
import com.ktdsuniversity.edu.members.vo.request.SearchMemberVO;
import com.ktdsuniversity.edu.members.vo.response.MemberListVO;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {

	@Value("${app.encrypt.aes.key}")
	private String aesSecretKey;

	private final MembersDao membersDao;
	private static final Logger logger = LoggerFactory.getLogger(FilesServiceImpl.class);

	@Transactional
	@Override
	public MembersVO createNewMember(RegistMembersVO registMembersVO) {

		String email = registMembersVO.getEmail();
		int emailCount = this.membersDao.selectEmailCount(email);
		if (emailCount > 0) {
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.USED);
		}

		String nickname = registMembersVO.getNickname();
		int nicknameCount = this.membersDao.selectNicknameCount(nickname);
		if (nicknameCount > 0) {
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.USED);
		}

		String rawName = registMembersVO.getName();
		String encryptedName = AES.encode(this.aesSecretKey, rawName);
		registMembersVO.setName(encryptedName);

		String rawNickname = registMembersVO.getNickname();
		String encryptedNickname = AES.encode(this.aesSecretKey, rawNickname);
		registMembersVO.setNickname(encryptedNickname);

		String rawPassword = registMembersVO.getPassword();
		String salt = SHA.generateSalt();
		String encryptedPassword = SHA.getEncrypt(rawPassword, salt);

		registMembersVO.setSalt(salt);
		registMembersVO.setPassword(encryptedPassword);

		int insertCount = this.membersDao.insertNewMember(registMembersVO);
		if (insertCount == 0) {
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.SYSTEM_ERROR);
		}

		MembersVO newMember = this.membersDao.selectMemberByEmail(email);
		newMember.setName(AES.decode(this.aesSecretKey, newMember.getName()));
		newMember.setNickname(AES.decode(this.aesSecretKey, newMember.getNickname()));
		return newMember;
	}

	@Override
	public MembersVO readMember(LoginMemberVO loginMemberVO) {

		MembersVO membersVO = this.membersDao.selectMemberByEmail(loginMemberVO.getEmail());

		if (membersVO == null) {
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.NOT_MATCHED_IDENTIFY);
		}

		if (membersVO.getLoginBlockYn().equals("Y")) {
			// 차단계정
			// 차단된 후 1시간이 지났는가?
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime loginBlockDate = LocalDateTime.parse(membersVO.getLoginBlockDate(), formatter);
			loginBlockDate.plusHours(1);
			logger.debug(loginBlockDate.toString());

			if (now.equals(loginBlockDate) || now.isAfter(loginBlockDate)) {
				// 차단 후 1시간 경과
				// 로그인 실패 횟수 0으로 초기화 &차단여부 N으로 수정
				int updateRows = this.membersDao.updateResetBlock(loginMemberVO.getEmail());
				logger.info("{}건이 블락 해제되었음", updateRows);
			} else {
				throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.NOT_MATCHED_IDENTIFY);
			}
		}

		// 활성계정
		// 사용자 salt필요
		// 로그인요청비밀번호
		// 암호화
		String rawPassword = loginMemberVO.getPassword();
		String storedSalt = membersVO.getSalt();
		String encrytedPassword = SHA.getEncrypt(rawPassword, storedSalt);

		if (encrytedPassword.equals(membersVO.getPassword())) {
			// 비밀번호 일치함
			int updateRows = this.membersDao.updateLoginStatus(membersVO.getEmail());
			if (updateRows == 0) {
				throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.FAILURE_LOGIN);
			}
			MembersVO loggedMember = this.membersDao.selectMemberByEmail(membersVO.getEmail());
			loggedMember.setName(AES.decode(this.aesSecretKey, loggedMember.getName()));
			loggedMember.setNickname(AES.decode(this.aesSecretKey, loggedMember.getNickname()));
			return loggedMember;
		}

		int updateRows = this.membersDao.updateLoginFailed(membersVO.getEmail());
		logger.info("{} 로그인실패", membersVO.getEmail());

		int blockUpdateRows = this.membersDao.updateBlock(membersVO.getEmail());
		if (blockUpdateRows > 0) {
			// 계정이 차단
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.BLOCKED_LOGIN);
		}

		// 비밀번호가 틀렸으므로 사용자 정보를 반환하지 않음
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");

	}

	@Transactional
	@Override
	public String updateLogoutStatus(String email) {
		int updatedRows = this.membersDao.updateLogoutStatus(email);

		if (updatedRows > 0) {
			return email;
		}
		return null;
	}

	@Transactional
	@Override
	public String deleteMember(String email, String password) {
		// 로그인된 회원의 이메일로 회원 정보를 조회
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpSession session = request.getSession();
		//현재 로그인
		MembersVO member = (MembersVO) session.getAttribute("__LOGIN_USER__");
		
		
		MembersVO loggedMember = this.membersDao.selectMemberByEmail(member.getEmail());
		if (loggedMember == null) {
			throw new ArticleException(ExceptionType.MEMBERS, ArticleCodes.NOT_EXISTS);
		}
		
		
	    String salt = loggedMember.getSalt();
	    String encryptedPassword = SHA.getEncrypt(password, salt);
	    if (!loggedMember.getPassword().equals(encryptedPassword)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
	    }

		int deleteRows = this.membersDao.deleteMember(email);
		if (deleteRows == 0) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
		}
		// 로그아웃 처리
		updateLogoutStatus(email);
		return email + "의 탈퇴가 완료되었습니다.";
	}

	@Override
	public MemberListVO readAllMembers(SearchMemberVO searchMemberVO) {
		long count = this.membersDao.selectMemberCount(searchMemberVO);
		searchMemberVO.calculatePageCount(count);
		List<MembersVO> memberList = this.membersDao.selectAllMembers(searchMemberVO);

		MemberListVO list = new MemberListVO();
		list.setMemberCount(count);
		list.setMembersVO(memberList);
		return list;
	}

}
