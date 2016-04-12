package com.grailsinaction

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LameSecurityInterceptor {

  static logger = LoggerFactory.getLogger(LameSecurityInterceptor.class)

  LameSecurityInterceptor() {
    match(controller: "post", action: "(addPost|deletePost)")
  }

  boolean before() { 
    if (params.impersonateId) {
      session.user = User.findByLoginId(params.impersonateId)
      logger.debug "Params contained impersonateId"
    }
    if (!session.user) {
      logger.debug "Redirecting to login form"
      redirect(controller: 'login', action: 'form')
      return false
    }
    true
  }

  boolean after() { model }

  void afterView() {
    logger.debug "Finished running ${controllerName} - ${actionName}"
  }
}
