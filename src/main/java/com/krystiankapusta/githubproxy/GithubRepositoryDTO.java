package com.krystiankapusta.githubproxy;

public record GithubRepositoryDTO(
    String name,
    OwnerDTO owner,
    boolean fork
) {}
