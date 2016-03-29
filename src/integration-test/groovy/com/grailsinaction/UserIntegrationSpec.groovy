package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {

    def "Saving our first user to the database"() {
      given: "A brand new user"
      def joe = new User(loginId: "joe", password: "secret")

      when: "The user is saved"
      joe.save()

      then: "It is saved to the database and can be found successfully"
      joe.errors.errorCount == 0
      joe.id != null
      User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes its properties"() {
      given: "An existing user"
      def existingUser = new User(loginId: "joe", password: "secret")
      existingUser.save(failOnError: true)

      when: "A property is changed"
      def foundUser = User.get(existingUser.id)
      foundUser.password = "sesame"
      foundUser.save(failOnError: true)

      then: "The change is reflected in the database"
      User.get(existingUser.id).password == "sesame"
    }

    def "Deleting an existing user removes it from the database"() {
      given: "An existing user"
      def existingUser = new User(loginId: "joe", password: "secret")
      existingUser.save(failOnError: true)

      when: "The user is deleted"
      def foundUser = User.get(existingUser.id)
      foundUser.delete(flush: true)

      then: "The user is removed from the database"
      !User.exists(foundUser.id)
    }

    def "Saving a user with invalid properties causes an error"() {
      given: "A user which fails several field validations"
      def user = new User(loginId: "joe", password: "tiny")

      when: "The user is validated"
      user.validate()

      then:
      user.hasErrors()

      "size.toosmall" == user.errors.getFieldErrors("password").code[0]
      "tiny" == user.errors.getFieldErrors("password").rejectedValue[0]
      !user.errors.getFieldErrors("loginId")
    }

    def "Recovering from a failed save by fixing invalid properties"() {
      given: "A user that has invalid properties"
      def chuck = new User(loginId: "chuck", password: "tiny")
      assert chuck.save() == null
      assert chuck.hasErrors()

      when: "We fix the invalid properties"
      chuck.password = "fistfist"
      chuck.validate()

      then: "The user saves and validates fine"
      !chuck.hasErrors()
      chuck.save(failOnError: true)
    }

    def "The user password mustn't match the loginId"() {
      given: "A user with invalid password"
      def moron = new User(loginId: "whoami", password: "whoami")

      when: "The user is validated"
      moron.validate()

      then: "The user password is rejected"
      moron.hasErrors()
      "validator.invalid" == moron.errors.getFieldErrors("password").code[0]
      "whoami" == moron.errors.getFieldErrors("password").rejectedValue[0]
    }
}
