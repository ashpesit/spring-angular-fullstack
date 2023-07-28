Feature: Create read update and delete employee
  Scenario Outline: Create Employee in the system
    Given The system is in healthy state
    When A request to create an employee with first name <firstName>, last name <lastName>, email address <emailAddress> and department ids <departmentIds>
    Then A response with error status is: <errorStatus>, message contains: <message> is received and null check on data value is: <dataNullCheck>
    And An employee is created in the database with values first name: <firstName>, last name: <lastName>, email address: <emailAddress> and departments <departmentIds>, given a success error status <errorStatus> is received in response

    Examples:
      | firstName | lastName | emailAddress     | departmentIds | errorStatus | message                             | dataNullCheck |
      | John      | Doe      | john@example.com | 5,6           | 404         | Department not found                | true          |
      | Eve       | Kine     |                  | 2             | 400         | Email address cannot be blank       | true          |
      |           | Mov      | json@example.com |               | 400         | Employee first name cannot be blank | true          |
      | Jane      | Smith    | jane@example.com | 2             | 0           | Success                             | false         |
      | Tom       | Dane     | tom@example.com  |               | 0           | Success                             | false         |


  Scenario Outline: Read a employee from the system based on invalid employee id
    Given The system is in healthy state
    When A request is made to fetch employee details with employee id <employeeId>
    Then A response with error status is: <errorStatus>, message contains: <message> is received and null check on data value is: <dataNullCheck>
    And The employee id in the response matches <employeeId>, given a success error status <errorStatus> is received in response

    Examples:
      | employeeId | errorStatus | message            | dataNullCheck |
      | 4          | 404         | Employee not found | true          |
      | 2          | 0           | Success            | false         |

  Scenario Outline: Update an employee in the system
    Given The system is in healthy state
    When A request is made to update employee id: <employeeId> with first name: <firstName>, last name: <lastName>, email address: <emailAddress> and department ids: <departmentIds>
    Then A response with error status is: <errorStatus>, message contains: <message> is received and null check on data value is: <dataNullCheck>
    And The employee with employee id: <employeeId> in the database is updated with values first name: <firstName>, last name: <lastName>, email address: <emailAddress> and departments <departmentIds>, given a success error status <errorStatus> is received in response

    Examples:
      | employeeId | firstName | lastName | emailAddress     | departmentIds | errorStatus | message                             | dataNullCheck |
      | 9          | John      | Doe      | john@example.com | 2             | 404         | Employee not found                  | true          |
      | 2          | Jane      | Smith    | jane@example.com | 3,6           | 404         | Department not found                | true          |
      | 1          |           | Bak      | sam@example.com  | 2             | 400         | Employee first name cannot be blank | true          |
      | 2          | Love      | nike     |                  | 2             | 400         | Email address cannot be blank       | true          |
      | 2          | Sam       | Lav      | sam@example.com  | 2             | 0           | Success                             | false         |
      | 1          | John      | Doe      | john@example.com |               | 0           | Success                             | false         |

  Scenario Outline: Delete a employee from the system based on employee id
    Given The system is in healthy state
    When A request to delete an employee with id <employeeId> is made
    Then A response with error status <errorStatus>, message <message> is received
    And Existence check of employee with id <employeeId> in the database returns <dbCheck>
    Examples:
      | employeeId | errorStatus | message            | dbCheck |
      | 2          | 0           | Success            | false   |
      | 9          | 404         | Employee not found | false   |
