package com.ktdsuniversity.edu.replies.web;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ktdsuniversity.edu.commons.util.ApiResponse;
import com.ktdsuniversity.edu.members.vo.response.MembersVO;
import com.ktdsuniversity.edu.replies.service.RepliesService;
import com.ktdsuniversity.edu.replies.vo.request.ModifyReplyVO;
import com.ktdsuniversity.edu.replies.vo.request.RegistReplyVO;
import com.ktdsuniversity.edu.replies.vo.response.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.response.ReplyListVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController // (@Controller + @ReponseBody) 메소드에 @ResponseBody 생략 가능.
@AllArgsConstructor
public class RepliesController {

	private RepliesService repliesService;

	@GetMapping("/articles/{articleId}/replies/list")
	public ApiResponse<ReplyListVO> getReplies(
			@Pattern(regexp = "^AR-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.") 
			@PathVariable String articleId) {
		try {
			return ApiResponse.OK(this.repliesService.readAllReplies(articleId));
		} catch (IllegalArgumentException iae) {
			return ApiResponse.FORBIDDEN(iae.getMessage());
		}
	}
	
	@PostMapping("/articles/{articleId}/replies")
	public ApiResponse<RepliesVO> makeNewReply(
			@Pattern(regexp = "^AR-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String articleId, 
			@Valid @ModelAttribute RegistReplyVO registRepliesVO,
			BindingResult validationResult,
			@SessionAttribute("__LOGIN_USER__") MembersVO membersVO
//			HttpSession session
			) {
		
		if (validationResult.hasErrors()) {
			return ApiResponse.BAD_REQUEST(validationResult.getFieldErrors());
		}
		//HttpSession에 있는 __LOGIN_USER__에 있는 email을 꺼내 RegistArticleVO에 할당
//		MembersVO membersVO = (MembersVO) session.getAttribute("__LOGIN_USER__");
//		if (membersVO == null) {
//			throw new IllegalArgumentException("로그인이 필요한 기능입니다.");
//		}
		registRepliesVO.setEmail(membersVO.getEmail());
		
		try {
			return ApiResponse.OK(this.repliesService.createNewReply(articleId, registRepliesVO));
		} catch (IllegalArgumentException iae) {
			return ApiResponse.FORBIDDEN(iae.getMessage());
		}
	}

	@PutMapping("/articles/{articleId}/replies/{replyId}")
	public ApiResponse<RepliesVO> updateReply(
			@Pattern(regexp = "^AR-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String articleId, 
			@Pattern(regexp = "^RL-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String replyId,
			@Valid @ModelAttribute ModifyReplyVO modifyReplyVO,
			BindingResult validationResult,
			@SessionAttribute("__LOGIN_USER__") MembersVO membersVO
//			HttpSession session
			) {
		
		if (validationResult.hasErrors()) {
			return ApiResponse.BAD_REQUEST(validationResult.getFieldErrors());
		}
		
		//HttpSession에 있는 __LOGIN_USER__에 있는 email을 꺼내 RegistArticleVO에 할당
//		MembersVO membersVO = (MembersVO) session.getAttribute("__LOGIN_USER__");
//		if (membersVO == null) {
//			throw new IllegalArgumentException("로그인이 필요한 기능입니다.");
//		}
		modifyReplyVO.setEmail(membersVO.getEmail());
		
		try {
			return ApiResponse.OK(this.repliesService.updateReply(articleId, replyId, modifyReplyVO));
		} catch (IllegalArgumentException iae) {
			return ApiResponse.FORBIDDEN(iae.getMessage());
		}
	}

	@DeleteMapping("/articles/{articleId}/replies/{replyId}")
	public ApiResponse<String> deleteReply(
			@Pattern(regexp = "^AR-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String articleId, 
			@Pattern(regexp = "^RP-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String replyId) {
		try {
			return ApiResponse.OK(this.repliesService.deleteReply(articleId, replyId));
		} catch (IllegalArgumentException iae) {
			return ApiResponse.FORBIDDEN(iae.getMessage());
		}
	}

	@PutMapping("/articles/{articleId}/replies/recommend/{replyId}")
	public ApiResponse<Long> RecommendReply(
			@Pattern(regexp = "^AR-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String articleId, 
			@Pattern(regexp = "^RP-[0-9]{8}-[0-9]{6,8}$", message="잘못된 요청입니다.")
			@PathVariable String replyId) {
		try {
			return ApiResponse.OK(this.repliesService.recommendReply(articleId, replyId));
		} catch (IllegalArgumentException iae) {
			return ApiResponse.FORBIDDEN(iae.getMessage());
		}
	}
}