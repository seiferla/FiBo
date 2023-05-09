# 1. Use Case description

## Name of use case

Account Picker

## 1.1 Brief Description

Each user should be able to use multiple accounts under the same useraccount. Therefore it should be possible to create new accounts, pick which account they want to use and switch between them. For the creation of a account the user must provide a name for the account

# 2 Flow of Events

- User clicks on the account icon
- If the user wants to create an account he clicks on the "Add Account" button
- User clicks on the account he wants to change to
- The current account of the user is changed to the picked account and the corresponding cashflows are loaded.


### 2.1.1 Activity Diagram

![account_picker_diagram](./account_picker_ad.drawio.svg)

### 2.1.2 Mock-up

![account_picker_diagram](./Registrierung.png)

### 2.1.3 Narrative

```gherkin
Feature: account picker

  As a registered user,
  I want to change the current account

  Background:
    And I am logged in

  Scenario: add new account
    Given I want to create a new account
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am on the "home" page
    When I press the "Account" icon
    Then I see a popup with a list of all my current accounts and a "Add Account" button fades in 
    When I press the "Add Account" button
    Then I am on the "add account" form
    When I enter "NAME" in the name field
    And I press the "Add new Account" button
    Then I am on the home page
    And I see that the account has changed to the created one
    
  Scenario: switch account
    Given I want to switch to an already existing account
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am on the "home" page
    When I press the "Account" icon
    Then I see a popup with a list of all my current accounts and a "Add Account" button fades in
    When I press on one of the listed accounts
    Then I am on the home page
    And I see that the account has changed to the one selected
```

## 2.2 Alternative Flows

(n/a)

# 3 Special Requirements

(n/a)

# 4 Preconditions

## 4.1 Registration
The user must not have an useraccount
# 5 Postconditions

(n/a)

# 6 Extension Points

(n/a)