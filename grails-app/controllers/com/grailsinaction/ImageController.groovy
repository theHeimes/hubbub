package com.grailsinaction

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImageController {

  static logger = LoggerFactory.getLogger(ImageController.class)
  static defaultAction = 'form'

  def upload(PhotoUploadCommand puc) {
    def user = User.findByLoginId(puc.loginId)
    if (puc.hasErrors() && !user) {
      render view: "form"
    }
    user.profile.photo = puc.photo
    redirect controller: "user", action: "profile", id: puc.loginId
  }

  def form() {
    // pass through to upload form
    [ userList: User.list() ]
  }

  def rawUpload() {
    // A Spring MultipartFile
    def mpf = request.getFile("photo")
    if (!mpf?.empty && mpf.size < 1024 * 2000) {
      def fileExtension = mpf.getContentType.split("/")[1]
      mpf.transferTo( new File("/hubbub/images/${params.loginId}/mugshot.${fileExtension}") )
    }
  }

  def renderImage(String id) {
    def user = User.findByLoginId(id)
    if (user?.profile?.photo) {
      response.setContentLength(user.profile.photo.size())
      response.outputStream.write(user.profile.photo)
    } else {
      response.sendError(404)
    }
  }
}

class PhotoUploadCommand {
  byte[] photo
  String loginId
}