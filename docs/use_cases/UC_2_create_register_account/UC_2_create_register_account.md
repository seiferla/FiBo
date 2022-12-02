# 1. Use Case description

## Name of use case

Create account

## 1.1 Brief Description

Each user should be able to create an account using their mobile device. The data that the user must provide are as follows:

- email
- username
- password

# 2 Flow of Events

- User starts the app. The login page opens
- User fills in the mail and password
- User clicks login button
- Login-data gets checked for validity
- If the data is invalid, the user receives the error message "invalid password or mail" and will be returned to the login page
- After successful login the splash activity appears


### 2.1.1 Activity Diagram

![login_account_diagram](./login_ad.drawio.svg)

### 2.1.2 Mock-up

![login_account_diagram](./Anmeldung.png)
![splash_activity](./Anmeldung.png)



### 2.1.3 Narrative

```gherkin
Feature: manual adding of data

  As a signed in user
  I want to add a receipt by manually entering the data
  in order to track my spent money.

  Background:
    And I am on the homepage

  Scenario: open new "manual adding of data" form
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am on the "home" page
    When I press the "New receipt" button
    Then I see two additional buttons "Add manually" and "Scan receipt" fade in
    When I press the "Add manually" button
    Then I am on the "manual adding of data" form

  Scenario: enter valid data and save it
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am at the "manual adding of data" form
    When I enter "store xy" in the field "Store"
    And I enter "x€" in the field "Price"
    And I enter "DD/MM/YYYY" in the field "Date"
    And I enter "category xy" in the field "Category"
    And I add a file to the form
    And I enter "note xy" in the field "Notes"
    When I press the "save" button
    Then I am on the "home" page
    And I receive a "success" message

  Scenario: enter invalid data and save the operation
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am at the "manual adding of data" form
    When I enter "x€" in the field "Store"
    And I enter "store xy" in the field "Price"
    And I enter "DD/MM/YYYY" in the field "Date"
    And I press the "save" button
    Then I am at the "manual adding of data" form
    And I receive a "error" message
```

## 2.2 Alternative Flows

(n/a)

# 3 Special Requirements

(n/a)

# 4 Preconditions

(n/a)

# 5 Postconditions

(n/a)

# 6 Extension Points

(n/a)