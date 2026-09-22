
package com.ktdsuniversity.edu.articles.vo.response;

import lombok.Data;

@Data //위 모든것을 한 번에 만들어줌
public class ArticlesVO {
	
	private String id;
	private String subject;
	private String content;
	private String email;
	private long viewCnt;
	private long recommendCnt;
	private String delYn;
	private String crtDt;
	private String mdfyDt;
	private String fileSetId;
}