package com.shash.projects.lovable_clone.dto.subscription;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokensLimit,
        Integer previewRunning,
        Integer previewsList
) {
}
