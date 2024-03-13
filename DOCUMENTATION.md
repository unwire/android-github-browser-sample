# Documentation

This README file includes some details about the way I approached the assignment. It's my hope that it clarifies my thought process while solving the problem at hand and communicates some of the decisions and assumptions I made along the way.

## Approach

Below is a high-level step-by-step plan for approaching the assignment:

### Step 0 - Get started
- [x] Create this document

### Step 1 - Preparation
- [x] Fork of the repository
- [x] Run the code and see the features in action
- [x] Get familiar with the code
- [x] Read the tasks thoroughly and ask for clarifications if needed
- [x] Make a plan for how you want to approach every task - include an estimate for time-boxing. Include testing and documentation in the plan and the estimate

### Step 2 - Complete the main tasks
- [ ] Complete the main tasks one by one based on the priority. Write tests and document decisions and assumptions as you go.
    - [ ] Task 1 - Navigation Framework Refactoring
    - [ ] Task 2 - Implement a Fake GithubService and ViewModel unit tests
    - [ ] Task 3 - Add a feature: User Details
- [ ] Ensure all the requirements in the description are fulfilled before moving on to the bonus points

### Step 3 - Tackle the bonus tasks
- [ ] If the time permits tackle the bonus tasks. Otherwise write some documentation explaining how you would have approached them

### Step 4 - Finalise & submit
- [ ] Read the description again and make sure you have completed all the tasks
- [ ] Create a PR 

## Plan

### Task 1 - Navigation Framework Refactoring

Before starting any major refactoring it's important to ensure we have tests covering the functionalities the refactoring can impact. 
So as a first step we will write some UI tests covering the navigation logic. 

Once we have the tests in place, we will create fragments for every screen in the app. The code in the fragments will not be very different from
what we currently have in the controllers. The only main difference would be the navigation logic to other screens.

With that in place, we can go ahead and create our navigation graph. Some updates might be required to the current API of the features, but will try to minimize 
breaking changes so that we can do not break existing module while doing the migration.

Next we will replace the conductor navigation logic with our Navigation component set up and make sure the tests we created still pass.

Finally we will delete all the code related to conductor navigation.

Estimation: 3 hours

### Task 2 - Implement a Fake GithubService and ViewModel unit tests

To start this task we create a `FakeGithubService` that conforms to our `GithubService` interface. 
This will provide us with fake data we need to unit test the viewmodel we pick for testing.
We will need to expose additional properties or functions to allow us control the fake data we expect in different tests.

The unit tests should cover the happy path and the edge cases.

Estimation: 1 hour

### Task 3 - Add a feature: User Details

This includes the following changes 

- Tweaks to `GithubService` to fetch user details and map it to a domain model
- A ViewModel that fetches the user details from `GithubService` and maps it to a UI model
- A new fragment containing the actual UI - might do this in Compose to showcase my experience with it
- Adding the new fragment as a new navigation destination
- Adding logic to navigate to the new fragment 

Estimation: 2 hours
