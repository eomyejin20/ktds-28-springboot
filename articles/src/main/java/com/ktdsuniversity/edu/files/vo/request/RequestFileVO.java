package com.ktdsuniversity.edu.files.vo.request;

import lombok.Data;

@Data
public class RequestFileVO {

	private String id;
	private String fileSetId;
	private String displayFileName;
	private String obfuscateFileName;
	private long fileSize;
	
}
