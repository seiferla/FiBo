# 1. Use Case description

## Name of use case

Manual adding of data

## 1.1 Brief Description

Every user should have the possibility to visualize his cash flow in a diagram according to categories and depending on a period of time. The following data are visualized:

- summed cash flow 
per category depending on period


# 2 Flow of Events

## 2.1 Basic Flow

- User clicks on dashboard
- User selects a specific time slot
  Data gets checked for availability depending on the time period
- If more than one cash flow of a category is available, the value is summed up
- The Dashboard will be updated

### 2.1.1 Activity Diagram

![show_dashboard_data Activity Diagram](show_dashboard.drawio.svg)

### 2.1.2 Mock-up

![dashboard](../UC_5_show_data_in_dashboard/dashboard.png)
![dashboard](../UC_5_show_data_in_dashboard/dashboard_zeitraum.png)




### 2.1.3 Narrative

```gherkin
Feature: show data in dashboard

  As a signed-in user
  I want to display the summed cash flows 
  in a pie chart 
  depending on a time window and seperated by category

  Background:
    And I am on the dashboard

  Scenario: show cash flow data depending on time slot
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am on the "dashboard" page
    When I select a specific time slot
    Then I click the save button
    Then I see a updated pie chart with different colors depending on the category and time slot


  Scenario: show cash flow data without selecting a time slot
    Given I am signed in with username "USER" and password "PASSWORD"
    And I am on the "dashboard" page
    When I don't select any specific time slot
    Then I see a updated pie chart where the data is collected over the complete time
```

## 2.2 Alternative Flows

(n/a)

# 3 Special Requirements

(n/a)

# 4 Preconditions

## 4.1 Login

The user has to be logged in to the system.

# 5 Postconditions

(n/a)

# 6 Extension Points

(n/a)
