#### Note Navigation Framework Refactoring
Given the heavy reliance on the Conductor library for navigation and screen rendering, transitioning gradually to the Android Navigation Component is crucial. This is because the Android Navigation Component operates differently, utilizing Fragments and NavHostFragment rather than Conductor's Controller approach.

Fortunately, I've found significant reusable code, including ViewModels, Domain-models, and Repository classes, which will facilitate a smoother and less risky transition.

To begin, I'll integrate a `FragmentContainerView` widget into the `activity_main.xml` layout. Initially hidden, it will only become visible when we enable a feature flag. This approach allows for seamless testing of the new navigation framework without disturbing the current project setup.

Moreover, I propose not to immediately remove the Conductor library neither rename files. Instead, I'll refactor the navigation setup to incorporate the Android Navigation Component. This phased approach enables thorough testing of the new navigation setup before removing Conductor and its associated code.

Additionally, I'll ensure compatibility with the existing dagger-hilt setup to prevent any regression or disruption to the current dependency injection configuration. This ensures a smooth integration of the new navigation system with the current project setup.