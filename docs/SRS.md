# FiBo - Software Requirements Specification

## Table of contents

- [Table of contents](#table-of-contents)
- [Introduction](#1-introduction)
    - [Purpose](#11-purpose)
    - [Scope](#12-scope)
    - [Definitions, Acronyms and Abbreviations](#13-definitions-acronyms-and-abbreviations)
    - [References](#14-references)
    - [Overview](#15-overview)
- [Overall Description](#2-overall-description)
    - [Vision](#21-vision)
    - [Use Case Diagram](#22-use-case-diagram)
    - [Technology Stack](#23-technology-stack)
- [Specific Requirements](#3-specific-requirements)
    - [Functionality](#31-functionality)
    - [Usability](#32-usability)
    - [Reliability](#33-reliability)
    - [Performance](#34-performance)
    - [Supportability](#35-supportability)
    - [Design Constraints](#36-design-constraints)
    - [Online User Documentation and Help System Requirements](#37-on-line-user-documentation-and-help-system-requirements)
    - [Purchased Components](#purchased-components)
    - [Interfaces](#39-interfaces)
    - [Licensing Requirements](#310-licensing-requirements)
    - [Legal, Copyright And Other Notices](#311-legal-copyright-and-other-notices)
    - [Applicable Standards](#312-applicable-standards)
- [Supporting Information](#4-supporting-information)

## 1. Introduction

### 1.1 Purpose

This Software Requirements Specification (SRS) describes all specifications for the application "
FiBo". It includes an overview about this project and its vision, detailed information about the planned features and boundary conditions of the development process.

### 1.2 Scope

The project is going to be realized as an Android App.

Planned Subsystems are:

* Dashboard:
  The dashboard is the main view of our user interface. It visualizes all the data that the user enters and offers the possibility to customize the view according to the user's wishes while also allow to categorize and filter the displayed data.
* Manage receipts and income:
  A user can manage receipts (manually creating a new one, create one by scanning a physical receipt, delete receipts) and can set their (monthly) budget. The budget is managed by entering a monthly income.
* Share costs:
  Users can invite other users of our product to bear costs (at least partially) by category or by receipt or by whole expenses (for example, a couple might do a 70:30 cut for the whole month, while a flat-sharing community might only share expenses they use together on an individual basis)
  .
* Data Management:
  Data is saved and can be retrieved.
* Account Management:
  Users can register themself, log into their account, log off and delete themself. Upon deletion, all user data is deleted as well.

### 1.3 Definitions, Acronyms and Abbreviations

| Abbrevation | Explanation                            |
| ----------- | -------------------------------------- |
| SRS         | Software Requirements Specification    |
| UC          | Use Case                               |
| n/a         | not applicable                         |
| tbd         | to be determined                       |
| UCD         | overall Use Case Diagram               |
| FAQ         | Frequently asked Questions             |

### 1.4 References

| Title                                              |    Date    | Publishing organization |
|----------------------------------------------------|:----------:|-------------------------|
| [FiBo Blog](https://fibo952390745.wordpress.com/)  | 24.10.2022 | FiBo Team               |
| [GitHub](https://github.com/Cebox82/FiBo)          | 24.10.2022 | FiBo Team               |

### 1.5 Overview

The following chapter provides an overview of this project with vision and Overall Use Case Diagram. The third chapter ([Specific Requirements](#3-specific-requirements)) delivers more details about the specific requirements in terms of functionality, usability and design parameters. Finally, there is a chapter with supporting information.

## 2. Overall Description

### 2.1 Vision

We want to build an app for managing your finances. An app to track and decrease your expenses easily, an app to plan your financial future. You will be able to scan all your receipts, with your phone and FiBo will do the rest for you. It automatically categorizes your expenses and tells you how much money you have left for the month. Fibo also integrates you bank account, so that you can directly see your balance, without changing the app. You want to save money for a big investment, no problem FiBo got your back, it tells you how long it will probably take you to reach your goal. FiBo is the all-in-one app for managing your finances. And we want to build it!

### 2.2 Use Case Diagram

![OUCD](./use-case-diagram.drawio.svg)

### 2.3 Technology Stack

The technology we use is:

Backend:

- Django
- PostgreSQL

Frontend:

- Android
- Java
- XML

IDE:

- IntelliJ
- Android Studio

Project Management:

- YouTrack
- GitHub
- Discord

Deployment:
TBD

Testing:

- Cucumber
- JUnit

## 3. Specific Requirements

### 3.1 Functionality

This section will explain the different use cases, you could see in the Use Case Diagram, and their functionality.
Until the end of December we plan to implement:

- [3.1.1 Create Account](#311-create-account)
- [3.1.2 Logging in](#312-logging-in)
- [3.1.3 Logging out](#313-logging-out)
- [3.1.4 Manual adding of data](#314-manual-adding-of-data)
- [3.1.5 Show data in a dashboard](#315-show-data-in-a-dashboard)
- [3.1.6 Evaluate scanning receipts](#316-evaluate-scanning-receipts)

#### 3.1.1 Create account

To identify all users we need an account system. This account system enables us to build important functions such as sharing costs or a personalized overview over finances (Keeping track of your finances).

[Create account](./use_cases/UC_1_create_account/UC_1_create_account.md)


#### 3.1.2 Logging in

The app will provide the possibility to register and log in. This will also make the usability easier when a user wants to manage his expenses because they don't have to enter their mail address every time.

[Logging in](./use_cases/UC_2_login_account/UC_2_login_account.md)


#### 3.1.3 Change added data

In case you want to change data in the cashflows, you should be able to edit the data with a simple click on the respective item.

[Change added data](./use_cases/UC_3_change_added_data/UC_3_change_added_data.md)

#### 3.1.4 Manual adding of data

This is one of the essentials of our project. The user gets the possibility to manually add their expenses. Therefore, the user has to enter the place of the purchase, and the items with the respective category and the price.

[Manual adding of Data](./use_cases/UC_4_manual_adding_of_data/UC_4_manual_adding_of_data.md)

#### 3.1.5 Show data in a dashboard

All the different kinds of data our users feed us with should be displayed graphically. At this point in time, it must not be user-configurable, yet still showing the most important pieces of information at a breeze.

[Show data in dashboard](./use_cases/UC_5_show_data_in_dashboard/UC_5_show_data_in_dashboard.md)


#### 3.1.6 Evaluate scanning receipts

We want to get a clear view on technologies that enable us to automatically scan receipts with high confidence. Until December, we should have some first pointers and have built an initial understanding on how to tackle this feature.

[Evaluate scanning receipts](./use_cases/UC_6_evaluate_scanning_receipts/UC_6_evaluate_scanning_receipts.md)

#### 3.1.7 Logging out

In case you share your phone, have multiple accounts or just want to be cautious about your privacy you should be able to manually log out.

[Logging out](./use_cases/UC_4_manual_adding_of_data/UC_4_manual_adding_of_data.md)




### 3.2 Usability

We plan on designing the user interface as intuitive and self-explanatory as possible to make the user feel as comfortable as possible using the app. Though an FAQ document will be available, it should not be necessary to use it.

#### 3.2.1 No training time needed

Our goal is that a user installs the android application, opens it and is able to use all features without any explanation or help.

#### 3.2.2 Familiar Feeling

We want to implement an app with familiar designs and functions. This way the user is able to interact in familiar ways with the app without having to get to know new interfaces.

### 3.3 Reliability

#### 3.3.1 Availability

The server shall be available 95% of the time. This also means we have to figure out the "rush hours" of our app because the downtime of the server is only tolerable when as few as possible people want to use the app.

#### 3.3.2 Defect Rate

Our goal is that we have no loss of any data. This is important so that the users financial data is save, even after a downtime of the server.

### 3.4 Performance

#### 3.4.1 Capacity

The system should be able to manage thousands of requests. Also it should be possible to register as many users as necessary.

#### 3.4.2 Storage

Smartphones don't provide much storage. Therefore we are aiming to keep the needed storage as small as possible.

#### 3.4.3 App performance / Response time

To provide the best App performance we aim to keep the response time as low as possible. This will make the user experience much better.

### 3.5 Supportability

#### 3.5.1 Coding Standards

We are going to write the code by using all of the most common clean code standards. For example we will name our variables and methods by their functionalities. This will keep the code easy to read by everyone and make further developement much easier.

#### 3.5.2 Testing Strategy

The application will have a high test coverage and all important functionalities and edge cases should be tested. Further mistakes in the implementation will be discovered instantly and it will be easy to locate the error.

### 3.6 Design Constraints

We are trying to provide a modern and easy to handle design for the UI aswell as for the architecture of our application. To achieve that the functionalities will be kept as modular as possible.

Because we are programming an Android App we chose Java as our programming language. To make the communication between the two parts easy, we will implement a RESTful-API between them which will provide the data in JSON-Format. The supported Platforms will be:

- Android 4.4 and higher
- Java 8 and higher

### 3.7 On-line User Documentation and Help System Requirements

The usage of the app should be as intuitive as possible, so it won't need any further documentation. If the user needs some help we will implement a "Help"-Button in the App which includes a FAQ and a formula to contact the development team.

### 3.8 Purchased Components

We don't have any purchased components yet. If there will be purchased components in the future we will list them here.

### 3.9 Interfaces

#### 3.9.1 User Interfaces

The User interfaces that will be implemented are:

#### TBD

#### 3.9.2 Hardware Interfaces

(n/a)

#### 3.9.3 Software Interfaces

The app will be runnable on Android 5.0 and higher. iOS won't be featured at the moment.

#### 3.9.4 Communication Interfaces

The server and hardware will communicate using the http protocol.

### 3.10 Licensing Requirements

### 3.11 Legal, Copyright, and Other Notices

The logo is licensed to the FiBo Team and is only allowed to use for the application. We do not take responsibility for any incorrect data or errors in the application.

### 3.12 Applicable Standards

The development will follow the common clean code standards and naming conventions. Also we will create a definition of done which will be added here as soon as its complete.

## 4. Supporting Information

For any further information you can contact the FiBo Team or check our [FiBo Blog](https://fibo952390745.wordpress.com/).
The Team Members are:

- Jens Hausdorf
- Jonas Karl
- Lars Seifert
- Christopher Ewert
- Markus Götz

<!-- Picture-Link definitions: -->

[OUCD]: https://github.com/Cebox82/FiBo/blob/master/docs/use-case-diagram.drawio.svg "Overall Use Case Diagram"