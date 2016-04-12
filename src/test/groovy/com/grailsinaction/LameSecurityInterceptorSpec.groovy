package com.grailsinaction


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LameSecurityInterceptor)
class LameSecurityInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test lameSecurity interceptor matches addPost"() {
        when:"A request matches the interceptor"
            withRequest(controller:"post", action: "addPost")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }

    void "Test lameSecurity interceptor matches deletePost"() {
        when:"A request matches the interceptor"
            withRequest(controller:"post", action: "deletePost")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }

    void "Test lameSecurity interceptor does not match timeline"() {
        when:"A request matches the interceptor"
            withRequest(controller:"post", action: "timeline")

        then:"The interceptor does match"
            !interceptor.doesMatch()
    }
}
