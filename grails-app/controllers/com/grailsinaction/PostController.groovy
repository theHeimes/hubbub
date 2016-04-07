package com.grailsinaction

class PostController {

  static scaffold = Post

  def timeline() {
    def user = User.findByLoginId(params.id)
    if (!user) {
      response.sendError(404)
    } else {
      [ user : user ]
    }
  }

  def addPost() {
    def user = User.findByLoginId(params.id)
    if (user) {
      def post = new Post(params)
      user.addToPosts(post)
      if (user.save()) {
        flash.message = "Successfully created Post"
      } else {
        flash.message = "Invalid or empty post"
      }
    } else {
      flash.message = "Invalid User Id"
    }
    redirect (action: "timeline", id: params.id)
  }

}
