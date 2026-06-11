package com.resustainability.fincorehub.request;

import java.util.List;

public record DeletePcRequest(
		
		List<Long> ids
) {}
