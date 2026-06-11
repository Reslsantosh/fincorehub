package com.resustainability.fincorehub.request;

import java.util.List;

public record DeleteRevenueBudgetRequest(
        List<Long> ids
) {
}