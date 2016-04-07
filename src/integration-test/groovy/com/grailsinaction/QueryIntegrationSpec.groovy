package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

import java.text.SimpleDateFormat

@Integration
@Rollback
class QueryIntegrationSpec extends Specification {

  void "Simple property comparison"() {
    when: "Users are selected by a simple password match"
    def users = User.where {
      password == "testing"
    }.list(sort: "loginId")

    then: "The users with that password are returned"
    users*.loginId == ["frankie"]
  }

  void "Multiple criteria"() {
    when: "A user is selected by loginId or password"
    def users = User.where {
      loginId == "frankie" || password == "crikey"
    }.list(sort: "loginId")

    then: "The matching loginIds are returned"
    users*.loginId == ["dillon", "frankie", "sara"]
  }

  void "Query on association"() {
    when: "The 'following' collection is queried"
    def users = User.where {
      following.loginId == "sara"
    }.list(sort: "loginId")

    then: "A list of followers of the given user is returned"
    users*.loginId == ["phil"]
  }
/*
  void "Query against a range value"() {
    given: "The current date and time"
    def sdf = new SimpleDateFormat("dd-MM-yyyy")
    def now = sdf.parse("05-04-2016")
    //def now = new Date()
    println "++++++++++++++++++++++++++++++++++++++++++++++++++++"
    println now
    println "++++++++++++++++++++++++++++++++++++++++++++++++++++"

    when: "The 'dateCreated' property is queried"
    def users = User.where {
      dateCreated in (now -1)..now
    }.list(sort: "loginId", order: "desc")

    then: "The users created within the specified date range are returned"
    users*.loginId == ["phil", "peter", "glen", "frankie", "chuck_norris", "admin"]
  }
*/
  void "Retrieve a single user"() {
    when: "A specific user is queried with get()"
    def user = User.where {
      loginId == "phil"
    }.get()

    then: "A single instance is returned"
    user.password == "thomas"
  }

}
