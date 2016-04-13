package com.grailsinaction

import grails.test.mixin.TestMixin
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

  def "Ensure basic mapping operations for user permalink"() {
    expect:
    assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
      id = expectId
    }
//    assertForwardUrlMapping("/users/glen", controller: "post", action: "timeline") {
//      id = "glen"
//    }
//    assertForwardUrlMapping("/users/chuck_norris", controller: "post", action: "timeline") {
//      id = "chuck_norris"
//    }

    where:
    url                   | expectCtrl    | expectAction  | expectId
    '/users/glen'         | 'post'        | 'timeline'    | 'glen'
    '/users/chuck_norris' | 'post'        | 'timeline'    | 'chuck_norris'
  }
}