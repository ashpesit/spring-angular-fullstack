Feature: Create read update and delete department
  Scenario Template: Create a department in the database
    Given The system is in healthy state
    When A request to create a department with name <name>, required <required>, read-only <readOnly> is made
    Then A response is received with errorStatus <errorStatus> and message <message> and data object isnull check returns <dataNull>
    And A department is created in the database with values name: <name>, required: <required>, and read only: <readOnly>, given a success error status <errorStatus> is received in response

    Examples:
      | name         | required | readOnly | errorStatus | message                             | dataNull |
      |              | true     | false    | 400         | Department name should not be blank | true     |
      | Organisation | false    | false    | 409         | Department name already exist       | true     |
      | Sales        | false    | false    | 0           | Success                             | false    |


  Scenario Template: Read a department from the system based on department id
    Given The system is in healthy state
    When A request to fetch the department details with department id <departmentId> is made
    Then A response is received with errorStatus <errorStatus> and message <message> and data object isnull check returns <dataNull>
    And The department id in the response matches <departmentId>, given a success error status <errorStatus> is received in response

    Examples:
      | departmentId | errorStatus | message              | dataNull |
      | 9            | 404         | Department not found | true     |
      | 2            | 0           | Success              | false    |

  Scenario Template: Update an non existing department in the system
    Given The system is in healthy state
    When A request is made to update department id <departmentId> with name <name>, required <required> and read-only <readOnly>
    Then A response is received with errorStatus <errorStatus> and message <message> and data object isnull check returns <dataNull>
    And The department with id <departmentId> has updated value as name: <name>, required: <required> and readOnly: <readOnly> in the database, given a success error status <errorStatus> is received in response

    Examples:
      | departmentId | name        | required | readOnly | errorStatus | message                                  | dataNull |
      | 7            | HR          | true     | false    | 404         | Department not found                     | true     |
      | 2            | Sales       | false    | true     | 400         | Update on this department is not allowed | true     |
      | 2            | Development | false    | false    | 0           | Success                                  | false    |
      | 2            |             | false    | false    | 400         | Department name should not be blank      | true     |
      | 3            | Product     | false    | true     | 0           | Success                                  | false    |

  Scenario Template: Delete a department from the system based on department id
    Given The system is in healthy state
    When A request is made to delete a department with id <departmentId>
    Then A response with errorStatus <errorStatus> and message <message> is received
    And The check for existence of department with id <departmentId> in the database return <existenceCheck>

    Examples:
      | departmentId | errorStatus | message                                  | existenceCheck |
      | 7            | 404         | Department not found                     | false          |
      | 1            | 400         | Update on this department is not allowed | true           |
      | 4            | 0           | Success                                  | false          |
