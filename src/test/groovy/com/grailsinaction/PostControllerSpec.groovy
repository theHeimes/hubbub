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
@Mock([User,Post,LameSecurityInterceptor])
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
    given: "A mock post service"
    def mockPostService = Mock(PostService)
    1 * mockPostService.createPost(_, _) >> new Post(content: "Mock Post")
    controller.postService = mockPostService

    when: "controller is invoked"
    def result = controller.addPost("joe_cool", "Posting up a storm")

    then: "our flash message and redirect confirms the success"
    flash.message ==~ /Added new post: Mock.*/
    response.redirectUrl == "/post/timeline/joe_cool"
  }
  def "Adding an invalid post to the timeline"() {
    given: "A user with posts in the database"
    User chuck = new User(loginId: "chuck_norris", password: "secret").save(failOnError: true)

    and: "a post service that throws an exception with the given data"
    def errorMsg = "Invalid or empty post"
    def mockPostService = Mock(PostService)
    1 * mockPostService.createPost(chuck.loginId, null) >> { throw new PostException(message: errorMsg) }
    controller.postService = mockPostService

    when: "addPost is invoked"
    def model = controller.addPost(chuck.loginId, null)

    then: "our flash message and redirect confirms the success"
    flash.message == errorMsg
    response.redirectUrl == "/post/timeline/${chuck.loginId}"
    Post.countByUser(chuck) == 0
  }
/*
  def "Adding a post with invalid user to the timeline"() {
    given: "A user with posts in the database"
    def mockPostService = Mock(PostService)
    1 * mockPostService.createPost(_, _) >> new Post(content: "Mock Post")
    controller.postService = mockPostService

    and: "A loginId parameter"
    def invalidLoginId = "this-user-id-does-not-exist"

    and: "some content for the post"
    def postContent = "Chuck Norris can unit test entire applications with a single assert"

    when: "addPost is invoked"
    def model = controller.addPost(invalidLoginId, postContent)

    then: "our flash message and redirect confirms the success"
    thrown(PostException)
    flash.message == "Invalid User Id"
    response.redirectUrl == "/post/timeline/${params.id}"
    Post.countByUser(chuck) == 0
  }
*/
  def "Testing id of #supplierId redirects to #expectedUrl"() {
    given:
    params.id = supplierId

    when: "Controller is invoked"
    controller.home()

    then:
    response.redirectUrl == expectedUrl

    where:
    supplierId    |   expectedUrl
    "joe_cool"    |   "/post/timeline/joe_cool"
    null          |   "/post/timeline/chuck_norris"
  }
/*
  def "Exercising security interceptor for unauthenticated user"() {
    when:
    withInterceptors(action: "addPost"){
      controller.addPost("glen_a_smith", "A first post")
    }
    then: response.redirectUrl == "/login/form"
  }
*/
}