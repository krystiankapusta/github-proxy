package com.krystiankapusta.githubproxy;

import java.util.List;

public record Repository(
    String repositoryName,
    String ownerLogin,
    List<Branch> branches
) {}
