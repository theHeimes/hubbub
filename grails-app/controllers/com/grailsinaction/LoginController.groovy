package com.grailsinaction

class LoginController {

  static defaultAction = 'form'
  def index() { }

  def form() {
  }

  def authenticate(String loginId, String password) {
    def user = User.findByLoginId(loginId)
    if (!user) {
      redirect controller: "user", action: "register"
    }
    if (user.password == password) {
      session.user = user
      redirect(uri: "/")
    } else {
      return [ user: user ]
    }
  }

}
