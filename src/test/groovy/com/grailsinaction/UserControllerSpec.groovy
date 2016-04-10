package com.grailsinaction

import grails.test.mixin.*
import spock.lang.*

@TestFor(UserController)
@Mock([User, Profile])
class UserControllerSpec extends Specification {

  def "Registering a user with known good parameters"() {
    given: "a set of parameters"
    params.with {
      loginId = "glen_a_smith"
      password = "winnings"
    }

    and: "a set of profile parameters"
    params["profile.fullName"] = "Glen Smith"
    params["profile.email"] = "glen@bytecode.com"
    params["profile.homepage"] = "http://blogs.bytecode.com.au/glen"

    when: "The user is registered"
    request.method = "POST"
    controller.register()

    then: "the user is created, and browser redirected"
    response.redirectUrl == "/"
    User.count() == 1
    Profile.count() == 1
  }
  
  @Unroll
  def "Registration command object for #loginId validates correctly"() {
    given: "A mocked command object"
    //def urc = mockCommandObject(UserRegistrationCommand)
    def urc = new UserRegistrationCommand()

    and: "a set of initial values from the spock test"
    urc.loginId = loginId
    urc.password = password
    urc.passwordRepeat = passwordRepeat
    urc.fullName = "Your Name Here"
    urc.email = "someone@somewhere.com"

    when: "the validator is invoked"
    def isValidRegistration = urc.validate()

    then: "Then the appropriate fields are flagged as errors"
    isValidRegistration == anticipateValid
    urc.errors.getFieldError(fieldInError)?.code == errorCode

    where:
    loginId | password | passwordRepeat | anticipateValid  | fieldInError  | errorCode
    "glen"  | "password" | "no-match"   | false            | "passwordRepeat" | "validator.invalid"
    "peter" | "password" | "password"   | true             | null          | null
    "a"     | "password" | "password"   | false            | "loginId"     | "size.toosmall"
  }

  def "Invoking the new register action via a command object"() {
    given: "A configured command object"
    def urc = new UserRegistrationCommand()
    urc.with {
      loginId = "glen_a_smith"
      fullName = "Glen Smith"
      email = "glen@bytecode.com"
      password = "password"
      passwordRepeat = "password"
  }
    
    and: "which has been validated"
    urc.validate()

    when: "the register action is invoked"
    controller.register2(urc)

    then: "the user is registered and browser redirected"
    !urc.hasErrors()
    response.redirectUrl == "/"
    User.count == 1
    Profile.count() == 1
  }
}
