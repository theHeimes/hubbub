package com.grailsinaction

class Profile {
  static belongsTo = [ user: User]
  byte[] photo
  String fullName
  String bio
  String homepage
  String email
  String timezone
  String country
  String jabberAddress

  String toString(){ return "Profile of $fullName (id: $id)" }  
  String getDisplayString() { return fullName }

  static constraints = {
    fullName blank: false
    bio nullable: true, maxSize: 1000
    homepage url: true, nullable: true
    email email: true, blank: false
    photo nullable: true, maxSize: 2 * 1024 * 1024 // 2 MB
    country nullable: true
    timezone nullable: true
    jabberAddress email: true, nullable: true
  }
}
