package com.example.reto1aplication

import android.net.Uri
import java.util.*

data class UserTemplate (var user:String, var name:String, var email:String, var password:String){
    var id:String =  UUID.randomUUID().toString()
    var photo:String = ""
}