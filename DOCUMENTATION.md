# Documentation

This README file includes some details about the way I approached the assignment. It's my hope that
it clarifies my thought process while solving the problem at hand and communicates some of the
decisions and assumptions I made along the way.

## Approach

Below is a high-level step-by-step plan for approaching the assignment:

### Step 0 - Get started

- [x] Create this document

### Step 1 - Preparation

- [x] Fork of the repository
- [x] Run the code and see the features in action
- [x] Get familiar with the code
- [x] Read the tasks thoroughly and ask for clarifications if needed
- [x] Make a plan for how you want to approach every task - include an estimate for time-boxing.
  Include testing and documentation in the plan and the estimate

### Step 2 - Complete the main tasks

- [x] Complete the main tasks one by one based on the priority. Write tests and document decisions
  and assumptions as you go.
    - [x] Task 1 - Navigation Framework Refactoring
    - [x] Task 2 - Implement a Fake GithubService and ViewModel unit tests
    - [x] Task 3 - Add a feature: User Details
- [x] Ensure all the requirements in the description are fulfilled before moving on to the bonus
  points

### Step 3 - Tackle the bonus tasks

- [x] If the time permits tackle the bonus tasks. Otherwise write some documentation explaining how
  you would have approached them

### Step 4 - Finalise & submit

- [x] Read the description again and make sure you have completed all the tasks
- [x] Create a PR

## Plan

### Task 1 - Navigation Framework Refactoring

Before starting any major refactoring it's important to ensure we have tests covering the
functionalities the refactoring can impact.
So as a first step we will write some UI tests covering the navigation logic.

Once we have the tests in place, we will create fragments for every screen in the app. The code in
the fragments will not be very different from
what we currently have in the controllers. The only main difference would be the navigation logic to
other screens.

With that in place, we can go ahead and create our navigation graph. Some updates might be required
to the current API of the features, but will try to minimize
breaking changes so that we can do not break existing module while doing the migration.

Next we will replace the conductor navigation logic with our Navigation component set up and make
sure the tests we created still pass.

Finally we will delete all the code related to conductor navigation.

Estimation: 3 hours

### Task 2 - Implement a Fake GithubService and ViewModel unit tests

To start this task we create a `FakeGithubService` that conforms to our `GithubService` interface.
This will provide us with fake data we need to unit test the viewmodel we pick for testing.
We will need to expose additional properties or functions to allow us control the fake data we
expect in different tests.

The unit tests should cover the happy path and the edge cases.

Estimation: 1 hour

### Task 3 - Add a feature: User Details

This includes the following changes

- Tweaks to `GithubService` to fetch user details and map it to a domain model
- A ViewModel that fetches the user details from `GithubService` and maps it to a UI model
- A new fragment containing the actual UI - might do this in Compose to showcase my experience with
  it
- Adding the new fragment as a new navigation destination
- Adding logic to navigate to the new fragment

Estimation: 2 hours

## Bonus tasks

Below are the following bonus tasks I managed to complete:

- Address usability issues:
    - Hiding the keyboard when the user presses the search button.
    - Displaying a "No results found" message when the search query yields no results.
    - Fix the navigation backstack issue when navigating from contributor to repository to contributor.
- Code cleanup:
    - Removing unused resources and files.
    - Organizing and rearranging imports.
    - Future improvement: Adding a static code analyzer like Detekt to detect code smells and
      automatically format code.
- Creating a second app (`pink_browser`) with a different theme.

And this is how I would approach the remaining tasks:

- Creating another app with a subset of features:
    - We can build a graph with a subset of destinations. Providing features with more context about
      available features so they can adjust themselves accordingly can be achieved by passing
      arguments to features, but there might be a more elegant solution.
- Caching GitHub API data:
    - This can be accomplished by introducing a repository layer for our features and a local data
      source that persists the results received from the network. The repository fetches the results
      from the network and caches them in the local data source the first time they are received.
      Subsequent calls to the repository skip fetching the data from the API and return the cached
      data from the local data source.

## Decision log

- To wait for data load in the UI tests I considered two options: Idling resource which is an API
  Espresso provides to wait for asynchronous operations and replacing the Github service DI binding
  with a fake one.
  Decided to go with the latter because it does not require adding test related code in the
  production source code and provides more control over the test (no risk for flaky tests).
- We need a fragment per screen to build our navigation graph. To scope the ViewModels to fragments
  I decided to use `HiltViewModel` that takes care of injections without us needing to declare extra
  Dagger components.
  For that I created a new temporary ViewModel for every screen. This extra ViewModel is not ideal
  but helps us avoid breaking the app while we add the navigation bits and pieces. This will later
  replace the ViewModel once we remove all the conductor specific code.
- I decided to move nav destination declarations to individual feature modules. This keeps the app
  module cleaner and enables us to expand destinations into nested graphs if needed in the future
  without touching the app(s).
- While writing unit tests I considered including a assertion library but realized it might be an
  overkill for this task.
- While covering `RepositorySearchViewModel` with unit tests I tweaked the implementation to cover
  an edge case (search called with an empty string).
- Introduced a new `UserDetails` domain model that includes more details about a user. This will be
  mapped to a UI model in our new user details screen.
- Decided to build the UI for user details screen in Compose to showcase my experience with Compose UI.
- Considered passing the data we have already fetched on the user (avatar, name and login) to the
  user details. That way user sees something some details about the user while we fetch the rest.
  But eventually decided to load all the details and display them in one go. There is a loading
  state (simple spinner) the user sees in the user details screen while the data loads.
- Decided to fix the infinite forward navigation issue by clearing the backstack. An alternative I
  considered was disabling item click on the list of repositories. But decided not to do that as
  that would have changed a potentially useful functionality.
