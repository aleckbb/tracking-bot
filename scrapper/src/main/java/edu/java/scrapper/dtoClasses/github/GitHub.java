package edu.java.scrapper.dtoClasses.github;

public record GitHub(
    Branch[] branches,
    PullRequest[] pullRequests,
    Repository repository
) {
}
