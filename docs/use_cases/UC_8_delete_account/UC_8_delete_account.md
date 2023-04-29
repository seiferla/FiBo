# 1. Use Case description

## Name of use case

Delete user

## 1.1 Brief Description

Every user should have the possibility to delete their related personal information including everything they have
stored in the FiBo app.

# 2 Flow of Events

## 2.1 Basic Flow

- The user clicks on the settings page
- The user clicks on the delete user account button
- The user receives a confirmation dialog
- If the user confirms the dialog the following things will happen:
  - the users personal data will be deleted from the app
  - the users private accounts including their stored cashflows will be deleted
  - the user will be deleted from every shared account
  - the user will be prompted to the login page
- If the user cancels the dialog, tFinally, the user is shown the login page

### 2.1.1 Activity Diagram

[//]: # (![Change_added_data_Activity_Diagram]&#40;logging_out.drawio.svg&#41;)

### 2.1.2 Mock-up

[//]: # (![settings_page]&#40;../UC_7_logging_out/settings_page.png&#41;)

[//]: # (![settings_page_confirm_dialog]&#40;../UC_7_logging_out/settings_page_confirm_dialog.png&#41;)

### 2.1.3 Narrative

```gherkin
Feature: delete user

  As a logged-in user
  I want to logout my user data

  Background:
    And I am on the settings page



```

## 2.2 Alternative Flows

(n/a)

# 3 Special Requirements

(n/a)

# 4 Preconditions

## 4.1 Login

The user has to be logged into the system.

# 5 Postconditions

The user has to be logged out of the system

# 6 Extension Points

(n/a)


