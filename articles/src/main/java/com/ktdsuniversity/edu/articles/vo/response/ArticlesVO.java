
package com.ktdsuniversity.edu.articles.vo.response;

public class ArticlesVO {
	
	private String id;
	private String subject;
	private String content;
	private String email;
	private String viewCnt;
	private String recommendCnt;
	private String delYn;
	private String crtDt;
	private String mdfyDt;
	private String fileSetId;
	
	
//	public ArticlesVO(String id, String subject, String content, String email, String viewCnt, String recommendCnt,
//			String delYn, String crtDt, String mdfyDt, String fileSetId) {
//		super();
//		this.id = id;
//		this.subject = subject;
//		this.content = content;
//		this.email = email;
//		this.viewCnt = viewCnt;
//		this.recommendCnt = recommendCnt;
//		this.delYn = delYn;
//		this.crtDt = crtDt;
//		this.mdfyDt = mdfyDt;
//		this.fileSetId = fileSetId;
//	}



	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getViewCnt() {
		return this.viewCnt;
	}

	public void setViewCnt(String viewCnt) {
		this.viewCnt = viewCnt;
	}

	public String getRecommendCnt() {
		return this.recommendCnt;
	}

	public void setRecommendCnt(String recommendCnt) {
		this.recommendCnt = recommendCnt;
	}

	public String getDelYn() {
		return this.delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getCrtDt() {
		return this.crtDt;
	}

	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}

	public String getMdfyDt() {
		return this.mdfyDt;
	}

	public void setMdfyDt(String mdfyDt) {
		this.mdfyDt = mdfyDt;
	}

	public String getFileSetId() {
		return this.fileSetId;
	}

	public void setFileSetId(String fileSetId) {
		this.fileSetId = fileSetId;
	}
	
}