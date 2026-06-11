package com.resustainability.fincorehub.request;

public record UpdateEbitdaBudgetRequest(
		
		String bu,
	    String sbu,
	    String site,
	    String financialYear,

	    Double apr,
	    Double may,
	    Double jun,
	    Double jul,
	    Double aug,
	    Double sep,
	    Double oct,
	    Double nov,
	    Double dec,
	    Double jan,
	    Double feb,
	    Double mar
) {}
