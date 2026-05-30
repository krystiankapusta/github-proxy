package com.krystiankapusta.githubproxy;

public record Branch(
    String name,
    String lastCommitSha
) {}
