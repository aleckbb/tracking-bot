package edu.java.dtoClasses.github;

public record GitHub(
    Branch[] branches,
    PullRequest[] pullRequests,
    Repository repository
) {
}
