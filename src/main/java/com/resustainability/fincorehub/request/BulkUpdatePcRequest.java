package com.resustainability.fincorehub.request;

import java.util.List;

public record BulkUpdatePcRequest(
        List<Long> ids,
        String sbu,
        String bu
) {}
