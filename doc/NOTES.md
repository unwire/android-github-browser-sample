#### Note Navigation Framework Refactoring
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
