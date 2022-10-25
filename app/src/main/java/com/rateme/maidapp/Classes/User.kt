package com.rateme.maidapp.Classes

import com.google.firebase.database.IgnoreExtraProperties

class User {
    companion object Factory {
        fun create(): User = User()
    }

    var sender: String? = null
    var receiver: String? = null
    var payment: Boolean? = false
}