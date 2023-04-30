# 1. Use Case description

## Name of use case

Scanning receipt

## 1.1 Brief Description

Every user should be able to create a cashflow by taking a photo of the receipt

# 2 Flow of Events

## 2.1 Basic Flow

- The user takes a photo of the receipt using their camera
- The app automatically crops and adjusts the image for clarity
- The app extracts relevant information from the scanned receipt, such as the value and total amount
- The extracted information is displayed to the user for confirmation
- If the information ist correct , the user saves it to their cashflow
- If the information is incorrect, the user can make manual adjustments before saving it

### 2.1.1 Activity Diagram
![Change_added_data_Activity_Diagram](logging_out.drawio.svg)


### 2.1.2 Mock-up
![settings_page](../UC_7_logging_out/settings_page.png)

![settings_page_confirm_dialog](../UC_7_logging_out/settings_page_confirm_dialog.png)


### 2.1.3 Narrative

```gherkin
Feature: logging out

  As a logged-in user
  I want to logout my account
  
  Background: 
    And I am on the settings page
    
    Scenario: successfully logout 
      Given I am signed in with username "USER" and password "PASSWORD"
      And I am on the "settings" page
      When I press on the logout button
      Then I receive a confirm dialog
      When I confirm the logout
      Then I am on the "login" page
      
    Scenario: unsuccessfully logout
      Given I am signed in with username "USER" and password "PASSWORD"
      And I am on the "settings" page
      When I press on the logout button
      Then I receive a confirm dialog
      When I cancel the logout
      Then I am on the "settings" page
      
    Scenario: enter invalid data and save
      Given I am signed in with username "USER" and password "PASSWORD"
      And I am at the "manual adding of data" form
      When I adjust "x€" in the "Store" field
      And I adjust "store xy" in the field "Price"
      And I press the "save" button
      Then I am at the "manual adding of data" form
      And I receive a "error" message
      
      

```


## 2.2 Alternative Flows

(n/a)

# 3 Special Requirements

(n/a)

# 4 Preconditions

## 4.1 Login

The user has to be logged into the system.

# 5 Postconditions

(n/a)

# 6 Extension Points

(n/a)


