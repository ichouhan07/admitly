package com.applligent.admitly.chatSystem.model

class Users {
    var userId: String? = null
    var name: String? = null
    var email: String? = null
    var imageUri: String? = null
    var message: String? = null

    constructor() {}
    constructor(userId: String?, name: String?, email: String?, imageUri: String?, message: String?) {
        this.userId = userId
        this.name = name
        this.email = email
        this.imageUri = imageUri
        this.message = message
    }
}