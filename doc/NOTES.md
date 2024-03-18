## Tasks:
### Primary Tasks:
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

### Bonus Tasks:
- Improve the UI/UX of the app.
    - Add a loading indicator when fetching data.
    - Dismiss the keyboard when the search button is pressed.
    - Fix error message edge cases when search input is empty or invalid.
    - Add AppBar title and back button where necessary.


### Execution Plan:
- [X] Setup:
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
- [X] Task 4: Bonus Tasks
    - [X] Improve the UI/UX of the app
        - [X] Add AppBar title and back button where necessary
        - [X] Add a loading indicator when fetching data
        - [X] Dismiss the keyboard when the search button is pressed
        - [X] Fix error message edge cases when search input is empty or invalid
        - [X] Add 2nd app in the codebase
        - [X] Implement caching for repository search functionality
- [X] Testing and Quality Assurance
    - [X] Test the app on different devices and configurations
    - [X] Test the app on different Android versions
- [X] Documentation
- [ ] Submit the Pull Request
- [ ] Code Review


## Notes and decisions made during the assignment execution:

#### Navigation Framework Refactoring
Given the heavy reliance on the Conductor library for navigation and screen rendering, transitioning gradually to the Android Navigation Component is crucial. This is because the Android Navigation Component operates differently, utilizing Fragments and NavHostFragment rather than Conductor's Controller approach.

Fortunately, I've found significant reusable code, including ViewModels, Domain-models, and Repository classes, which will facilitate a smoother and less risky transition.

To begin, I'll integrate a `FragmentContainerView` widget into the `activity_main.xml` layout. Initially hidden, it will only become visible when we enable a feature flag. This approach allows for seamless testing of the new navigation framework without disturbing the current project setup.

Moreover, I propose not to immediately remove the Conductor library neither rename files. Instead, I'll refactor the navigation setup to incorporate the Android Navigation Component. This phased approach enables thorough testing of the new navigation setup before removing Conductor and its associated code.

Additionally, I'll ensure compatibility with the existing dagger-hilt setup to prevent any regression or disruption to the current dependency injection configuration. This ensures a smooth integration of the new navigation system with the current project setup.

### FakeGithubService and ViewModel Testing

#### `FakeGithubService`:
I added the `FakeGithubService` to the `:shared:service:imp` module, maintaining project structure. It mimics a real GitHub service with fabricated data, utilising the `FailureProvider` interface for injection.

#### ViewModel Testing:
For the ViewModel testing task, I focused on testing the `RepositorySearchFragmentViewModel`. I tested the `RepositorySearchFragmentViewModel` extensively in a new `RepositorySearchFragmentViewModelTest` file. Using JUnit's parameterized testing, I covered various scenarios to ensure thorough validation.

Note that the `FakeGithubService` class became a test dependency for the `RepositorySearchFragmentViewModelTest` class, exposing the `:shared:service:impl` module. This intentional decision was made to also test the `FakeGithubService` within the app. Although this approach is considered an anti-pattern and not recommended, I wanted to explain my rationale. In a real-world scenario, the `FakeGithubService` would ideally reside within the module where it's tested or in a shared testing module.

### User Details Feature
To demonstrate my proficiency in Android Jetpack Compose, I've chosen to implement the User Details screen in Compose, adhering to best practices like state-hoisting. This decision reflects a commitment to modern Android development standards. 


### Bonus Tasks
For the bonus task, I focused on enhancing the app's UX by introducing loading and empty states, along with an AppBar. I also tackled a peculiar crash on the contributors screen, arising when a repository lacked contributors, though I opted not to delve into its investigation. Instead, I swiftly wrapped all network requests with try-catch blocks to gracefully handle errors by emitting an Error state. Additionally, I created a 2nd app with a distinct theme to explore design variations.

Regarding API data caching, I took on the challenge of implementing caching specifically for repository search functionality, albeit not suitable for production environments. I adjusted the `searchRepos` interface to return `Flow<ServiceResult<List<Repository>>>`, enabling simulation of the caching strategy. The updated implementation first emits a Loading state, then attempts to retrieve data from the local cache. If cached repositories are found, they are emitted as a Success state. Subsequently, data is fetched from the network, mapped, and inserted into the local cache. Upon successful network retrieval, the fetched repositories are emitted as a Success state. In case of a network failure, the function checks for cached repositories; if none are found, it emits a Failure state for network errors. Exception handling ensures that errors are emitted only when both the cache and network operations fail, preventing duplicate error emissions.

Finally, I identified some code duplication within the view models and fragments that could be addressed to reduce redundancy. Though potential extensive refactoring could be done, I opted not to tackle it at this time.