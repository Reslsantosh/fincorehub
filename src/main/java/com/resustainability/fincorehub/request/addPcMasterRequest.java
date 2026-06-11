package com.resustainability.fincorehub.request;

public record addPcMasterRequest(
		
		Long profitCentre,
		String profitCentreName,
		String siteNameForMIS,
		String sbuFinal,
		String unit,
		String sbu,
		String bu,
		String plantCode
  ) {

}
