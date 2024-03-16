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
  - Improve the UI/UX of the app.
    - Add a loading indicator when fetching data.
    - Dismiss the keyboard when the search button is pressed.
    - Fix error message edge cases when search input is empty or invalid.
    - Add AppBar title and back button where necessary.
    - Fix CardView padding and margins.
  
## Execution Plan:
- [-] Setup:
  - [x] Fork the repository
  - [X] Update README.md file with assignment details and execution plan.
  - [X] Clone the forked repository locally.
  - [X] Ensure the project can be compiled and run without errors
    - The app builds and runs without errors. 
    - Tested to work on Android 13 (API 33) with no issues on configuration changes.
- [X] Task 1: Navigation Refactoring
  - [X] Implement a feature flag to enable the new navigation setup
  - [X] Integrate a `FragmentContainerView` widget into the `activity_main.xml` layout
  - [X] Refactor the navigation setup to incorporate the Android Navigation Component
    - [X] RepositorySearchScreen
    - [X] ContributorsScreen
    - [X] UserRepositoriesScreen
- [X] Task 2: Implement FakeGithubService and ViewModel tests
  - [X] Implement FakeGithubService class
  - [X] Write unit tests for RepositorySearchViewModel
- [X] Task 3: Add User Details feature
  - [X] Design the User Details screen
  - [X] Add a new Fragment for the User Details screen
  - [X] Add navigation to the User Details screen with dummy data
  - [X] Add Jetpack Compose dependencies
  - [X] Add User Details screen Composable
  - [X] Add a new ViewModel for the User Details screen
  - [X] Connect the User Details screen to the ViewModel
- [ ] Task 4: Bonus Tasks
- [ ] Testing and Quality Assurance
- [ ] Documentation
- [ ] Submit the Pull Request
- [ ] Code Review
- [ ] Notify the Hiring Manager
