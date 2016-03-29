package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class PostIntegrationSpec extends Specification {

  def "Adding posts to user links post to user"() {
    given: "A brand new user"
    def user = new User(loginId: "joe", password: "secret")
    user.save(failOnError: true)

    when: "Several posts are added to the user"
    user.addToPosts(new Post(content: "First post... W00t!"))
    user.addToPosts(new Post(content: "Second pst..."))
    user.addToPosts(new Post(content: "Third pst..."))

    then: "The user has a list of posts attached"
    3 == User.get(user.id).posts.size()
  }

  def "Ensure posts linked to a user can be retrieved"() {
    given: "A user with several posts"
    def user = new User(loginId: "joe", password: "secret")
    user.save(failOnError: true)
    user.addToPosts(new Post(content: "First"))
    user.addToPosts(new Post(content: "Second"))
    user.addToPosts(new Post(content: "Third"))
    

    when: "The user is retrieved by their id"
    def foundUser = User.get(user.id)
    def sortedPostContent = foundUser.posts.collect {
      it.content
    }.sort()

    then: "The posts appear on the retrieved user"
    sortedPostContent == [ "First", "Second", "Third" ]

  }

}
