package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {

    def "Saving our first user to the database"() {
      given: "A brand new user"
      def joe = new User(loginId: "joe", password: "secret",
        homepage: "http://www.grailsinaction.com" )

      when: "The user is saved"
      joe.save()

      then: "It is saved to the database and can be found successfully"
      joe.errors.errorCount == 0
      joe.id != null
      User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes its properties"() {
      given: "An existing user"
      def existingUser = new User(loginId: "joe", password: "secret",
        homepage: "http://www.grailsinaction.com")
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
      def existingUser = new User(loginId: "joe", password: "secret",
        homepage: "http://www.grailsinaction.com")
      existingUser.save(failOnError: true)

      when: "The user is deleted"
      def foundUser = User.get(existingUser.id)
      foundUser.delete(flush: true)

      then: "The user is removed from the database"
      !User.exists(foundUser.id)
    }

    def "Saving a user with invalid properties causes an error"() {
      given: "A user which fails several field validations"
      def user = new User(loginId: "joe", password: "tiny",
        homepage: "not-a-url")

      when: "The user is validated"
      user.validate()

      then:
      user.hasErrors()

      "size.toosmall" == user.errors.getFieldErrors("password").code[0]
      "tiny" == user.errors.getFieldErrors("password").rejectedValue[0]
      "url.invalid" == user.errors.getFieldErrors("homepage").code[0]
      "not-a-url" == user.errors.getFieldErrors("homepage").rejectedValue[0]
      !user.errors.getFieldErrors("loginId")
    }
}
