package com.krystiankapusta.githubproxy;

public record ErrorResponse(
    int status,
    String message
) {}
