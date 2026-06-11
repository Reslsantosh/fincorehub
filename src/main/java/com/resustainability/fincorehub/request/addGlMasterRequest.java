package com.resustainability.fincorehub.request;

public record addGlMasterRequest(
		
	Long accountNumber,
	String description,
	String type,
	String consoleGroup,
	String bu,
	String categoryGroup,
	String plHeaders
  ) {

}
