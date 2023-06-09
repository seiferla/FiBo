Feature: scan receipt

  Background:
    And I am on the homepage

  Scenario: open camera to scan receipt
    Given I am signed in with email "EMAIL" and password "PASSWORD"
    And I am on the "home" page
    When I press the "New receipt" button
    Then I see two additional buttons "Add manually" and "Scan receipt" fade in
    When I press the "Scan receipt" button
    Then I am in the camera layout

  Scenario: successfully scan and save it
    Given I am signed in with email "EMAIL" and password "PASSWORD"
    And I am in the camera layout
    Then I turn the focus of the camera to the receipt
    When I press the camera button
    Then I take a picture of the receipt
    Then I get an overview of the scanned receipt and their values
    When I press the "save" button
    Then I am on the "home" page
    And I receive a "success" message

  Scenario: incomplete scan and save the operation by rescanning
    Given I am signed in with email "EMAIL" and password "PASSWORD"
    And I am in the camera layout
    Then I turn the focus of the camera to the receipt
    When I press the camera button
    Then I take a picture of the receipt
    Then I get an overview of the scanned receipt and their values
    When I see a incorrect receipt position
    Then I can press the camera button to perform a new scan

  Scenario: failed scan
    Given I am signed in with email "EMAIL" and password "PASSWORD"
    And I am in the camera layout
    Then I turn the focus of the camera to the receipt
    When I press the camera button
    Then I take a picture of the receipt
    Then I receive a "error" message