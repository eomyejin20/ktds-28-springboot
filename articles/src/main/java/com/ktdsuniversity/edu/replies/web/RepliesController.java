package com.ktdsuniversity.edu.replies.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktdsuniversity.edu.commons.util.ApiResponse;
import com.ktdsuniversity.edu.replies.service.RepliesService;
import com.ktdsuniversity.edu.replies.vo.request.ModifyReplyVO;
import com.ktdsuniversity.edu.replies.vo.request.RegistReplyVO;
import com.ktdsuniversity.edu.replies.vo.response.RepliesVO;
import com.ktdsuniversity.edu.replies.vo.response.ReplyListVO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController // @Controller + @ResponseBody(메소드 생략가능)
public class RepliesController {
	
	private RepliesService repliesService;
	
	// GET  /replies/{게시글 아이디}
	// 게시글에 등록된 댓글을 반환
	@GetMapping("/articles/{articleId}/replies")
	public ApiResponse<ReplyListVO> getReplies(@PathVariable String articleId) {
		ReplyListVO result = this.repliesService.readAllReplies(articleId);
		return ApiResponse.OK(result);
	}
	
	
	// GET /replies/{게시글 아이디}/{댓글아이디}
		// 게시글에 등록된 댓글을 반환
		@GetMapping("/articles/{articleId}/replies/{replyId}")
		public ApiResponse<RepliesVO> getReply(@PathVariable String articleId,@PathVariable String replyId) {
			RepliesVO result = this.repliesService.readReply(articleId, replyId);
			return ApiResponse.OK(result);
		}
	
	// POST /replies/{게시글 아이디}
	// 게시글에 댓글 작성 (파일 첨부 가능)
	@PostMapping("/articles/{articleId}/replies")
	public ApiResponse<RepliesVO> makeNewReply(@PathVariable String articleId, RegistReplyVO registReplyVO) {
		RepliesVO result = this.repliesService.createNewReply(articleId, registReplyVO);
		return ApiResponse.CREATED(result);
	}
	
	// PUT /replies/{게시글 아이디}/{댓글아이디}
	// 게시글에 등록된 댓글을 수정 (파일 첨부 가능)
	@PutMapping("/articles/{articleId}/replies/{replyId}")
	public ApiResponse<RepliesVO> updateReply(@PathVariable String articleId,@PathVariable String replyId, ModifyReplyVO modifyReplyVO) {
		RepliesVO result = this.repliesService.updateReply(articleId, replyId, modifyReplyVO);
		return ApiResponse.OK(result);
	}
	
	// DELETE /replies/{게시글 아이디}/{댓글아이디}
	// 게시글에 등록딘 댓글 하나를 삭제
	// 첨부된 파일 제거 
	@DeleteMapping("/articles/{articleId}/replies/{replyId}")
	public ApiResponse<String> deleteReply(@PathVariable String articleId,@PathVariable String replyId) {
		String result = this.repliesService.deleteReply(articleId, replyId);
		return ApiResponse.OK(result);
	}
	
	
	// PUT /replies/{게시글 아이디}/recommend/{댓글아이디}
	// 게시글에 등록된 댓글 하나를 추천
	@PutMapping("/articles/{articleId}/replies/recommend/{replyId}")
	public ApiResponse<Long> recommendReply(@PathVariable String articleId,@PathVariable String replyId) {
		long result = this.repliesService.recommendReply(articleId, replyId);
		return ApiResponse.OK(result);
	}
	
}
