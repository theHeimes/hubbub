package com.grailsinaction

import grails.test.mixin.TestMixin
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(PostController)
@Mock([User,Post])
class PostControllerSpec extends Specification {

    def "Get a users timeline given their id"() {
      given: "A user with posts in the database"
      User chuck = new User(loginId: "chuck_norris", password: "secret")
      chuck.addToPosts(new Post(content: "A first post"))
      chuck.addToPosts(new Post(content: "A second post"))
      chuck.save(failOnError: true)

      and: "A loginId parameter"
      params.id = chuck.loginId

      when: "The timeline is invoked"
      def model = controller.timeline()

      then: "The user is in the returned model"
      model.user.loginId == "chuck_norris"
      model.user.posts.size() == 2
   }

   def "Check that non-existent users are handled with an error"() {
    given: "The id of a non-existent user"
    params.id = "this-user-id-does-not-exist"

    when: "The timeline is invoked"
    controller.timeline()

    then: "A 404 is sent to the browser"
    response.status == 404
   }

   def "Adding a valid post to the timeline"() {
    given: "A user with posts in the database"
    User chuck = new User(loginId: "chuck_norris", password: "secret").save(failOnError: true)

    and: "A loginId parameter"
    params.id = chuck.loginId

    and: "some content for the post"
    params.content = "Chuck Norris can unit test entire applications with a single assert"

    when: "addPost is invoked"
    def model = controller.addPost()

    then: "our flash message and redirect confirms the success"
    flash.message == "Successfully created Post"
    response.redirectUrl == "/post/timeline/${chuck.loginId}"
    Post.countByUser(chuck) == 1
  }

  def "Adding an invalid post to the timeline"() {
    given: "A user with posts in the database"
    User chuck = new User(loginId: "chuck_norris", password: "secret").save(failOnError: true)

    and: "A loginId parameter"
    params.id = chuck.loginId

    and: "some content for the post"
    params.content = ""

    when: "addPost is invoked"
    def model = controller.addPost()

    then: "our flash message and redirect confirms the success"
    flash.message == "Invalid or empty post"
    response.redirectUrl == "/post/timeline/${chuck.loginId}"
    Post.countByUser(chuck) == 0
  }

  def "Adding a post with invalid user to the timeline"() {
    given: "A user with posts in the database"
    User chuck = new User(loginId: "chuck_norris", password: "secret").save(failOnError: true)

    and: "A loginId parameter"
    params.id = "this-user-id-does-not-exist"

    and: "some content for the post"
    params.content = "Chuck Norris can unit test entire applications with a single assert"

    when: "addPost is invoked"
    def model = controller.addPost()

    then: "our flash message and redirect confirms the success"
    flash.message == "Invalid User Id"
    response.redirectUrl == "/post/timeline/${params.id}"
    Post.countByUser(chuck) == 0
  }
}