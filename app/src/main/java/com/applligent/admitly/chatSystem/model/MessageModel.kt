package com.applligent.admitly.chatSystem.model

class MessageModel {
    var message: String? = null
    var senderId: String? = null
    var timeStamp: String? = null

    constructor() {}
    constructor(message: String?, senderId: String?, timeStamp: String?) {
        this.message = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }
}