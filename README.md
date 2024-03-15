This repository contains android code for a simple app that can search through github repositories and users.

It is intended to serve as a basis for android tech assignments. As such it is not intended to be "production complete" in any sense. 
But to serve as a foundation for exploring ways to extend, improve and refactor functionality - both in terms of user and developer experience.

## Primary Tasks:

- Navigation Framework Refactoring
  - Analysis:
    - Understand the current navigation setup using Conductor.
    - Analyse the impact on public feature APIs and Dagger injection scopes.
    - Identify potential risks such as navigation issues or regressions.
    - Plan for testing and quality assurance. 
  - Refactoring:
    - Replace Conductor with Android Navigation Component.
    - Update navigation destinations using route-based destinations.
  - Testing:
    - Create a fake implementation of GithubService for unit testing.
    - Write unit tests for one of the ViewModels using the fake service.
- User Details feature:
  - Decide on the relevant user information to display.
  - Design the User Details screen.
  - Implement the feature to fetch and display user details from the GitHub API.

## Bonus Tasks:
  - TBA...
  
## Execution Plan:
- [-] Setup:
  - [x] Fork the repository
  - [X] Update README.md file with assignment details and execution plan.
  - [ ] Clone the forked repository locally.
  - [ ] Ensure the project can be compiled and run without errors
- [ ] Task 1: Navigation Refactoring
- [ ] Task 2: Implement FakeGithubService and ViewModel tests
- [ ] Task 3: Add User Details feature
- [ ] Task 4: Bonus Tasks
- [ ] Testing and Quality Assurance
- [ ] Documentation
- [ ] Submit the Pull Request
- [ ] Code Review
- [ ] Notify the Hiring Manager
