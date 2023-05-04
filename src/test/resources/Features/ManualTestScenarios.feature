#Author:akshit.shiv@gmail.com
#Date:May 3,2023
#Description:Scenarios to validate the OTT Platform Media Services
Feature: Feature to test OTT Platform media services

	@ManualTest
  Scenario Outline: Validate the domain name for tracks in OTT platform Media services
    Given I am using "MediaService"
    When I send GET Request
    Then status code is 200
    And the domain name is <domainName> for track with label <label>

    Examples: 
      | domainName | label       |
      | spotify    | Spotify     |
      | apple      | Apple Music |

	@ManualTest
  Scenario: Validate the schema for GET request of OTT platform media service.
    Given I am using "MediaService"
    When I send GET Request
    Then status code is 200
    And the response is received with schema "MediaServiceSchema"

	@ManualTest
  Scenario: Validate that the track type and segment type for each track within OTT platform media service
    Given I am using "MediaService"
    When I send GET Request
    Then status code is 200
    And the response is received with all tracks having "type" as "music_track"
    And the response is received with all tracks having "segment_type" as "music"
