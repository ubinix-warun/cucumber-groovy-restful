Feature: Post "echo" message to https://httpbin.org/

  @Positive
  Scenario: check echo-data from /post
    Given I access the resource host "https://httpbin.org/"
    And I request "POST" to "/post"
    And I provide parameter "TEST" as "john"
    And I provide parameter "Sample" as "john"
    When I retrieve the results
    Then it should have the field "TEST" containing the value "john"
    And it should have the field "Sample" containing the value "john"