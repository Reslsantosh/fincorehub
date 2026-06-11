package com.resustainability.fincorehub.request;

public record updateGlRequest(
		Long accountNumber,
	    String description,
	    String type,
	    String consoleGroup,
	    String bu,
	    String categoryGroup,
	    String plHeaders
) {}
