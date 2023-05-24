#Author:akshit.shiv@gmail.com
#Date:May 3,2023
#Description:Scenarios to validate the OTT Platform Media Services

Feature: Feature to test OTT Platform media services

  @e2e
  Scenario: Validate the response time and status code of OTT platform Media services
    Given I am using "MediaService"
    When I send "GET" Request
    Then status code is 200
    And response time is below 1000 milliseconds

  @e2e
  Scenario: Validate the id field and segment type of every track in OTT platform Media services
    Given I am using "MediaService"
    When I send "GET" Request
    And status code is 200
    Then the "id" field is neither empty nor null for all the items
    And the "segment_type" for all track is "music"

  @e2e
  Scenario: Validate the primary field in title list of every track in OTT platform Media services
    Given I am using "MediaService"
    When I send "GET" Request
    And status code is 200
    Then the "primary" field in "title_list" is neither empty nor null for all the items

	@e2e
  Scenario: Validate that only one track has now_playing offset as true in offset
    Given I am using "MediaService"
    When I send "GET" Request
    And status code is 200
    Then only one track has now_playing offset as true

	@e2e
  Scenario: Validate the Date field in OTT platform Media services
    Given I am using "MediaService"
    When I send "GET" Request
    And status code is 200
    And the response header has "Date" value as "Tue, 23 May"
