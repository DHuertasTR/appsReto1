package com.example.reto1aplication

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

public class PublicationHolder:RecyclerView.Adapter<PublicationViewHold>() {
    private var posts = ArrayList<PublicationTemplate>()
    private var users = HashMap<String,UserTemplate>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationViewHold {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.postrow, parent, false)
        return PublicationViewHold(view)
    }

    override fun onBindViewHolder(hold: PublicationViewHold, position: Int) {
        val postN = posts[position]

        var autor = postN.author

        if (autor != null) {
            hold.postAutorRow.text = autor.user
        }
        hold.postCityRow.text = postN.city
        hold.postDateRow.text = postN.date
        hold.postDescriptionRow.text = postN.description
        if (autor != null) {
            if(autor.photo!=""){
                val uri = Uri.parse(autor.photo)
                Log.e("URI",uri.toString())

                hold.postAvatarRow.setImageURI(uri)
            }
        }

       if(postN.image!=""){

           val uri = Uri.parse(postN.image)
           Log.e("URI",uri.toString())

           hold.postImageRow.setImageURI(uri)
       }
    }

    fun onPause(sharedPreferences: SharedPreferences){
        val json = Gson().toJson(posts)
        Log.e(">>>>>",json.toString())
        sharedPreferences.edit().putString("currentPosts",json).apply()
    }

    fun onResume(sharedPreferences: SharedPreferences){
        var json = sharedPreferences.getString("currentPosts","NO_DATA")

        if(json != "NO_DATA"){
            if(posts.size==0){
                Log.e("ERROR",json.toString())
                val array = Gson().fromJson<Array<PublicationTemplate>>(json.toString(),Array<PublicationTemplate>::class.java)
                val oldPosts = ArrayList(array.toMutableList())
                if(oldPosts.isNotEmpty()){
                    posts = oldPosts
                }
            }
        }else{
            Log.e("ERROR","No se encuentra la serialziacion de posts")
        }

        json = sharedPreferences.getString("allUsers","NO_DATA")

        if(json != "NO_DATA"){
            Log.e("<<<",json.toString())
            val oldUsers = Gson().fromJson<HashMap<String,UserTemplate>>(json.toString(),Array<PublicationTemplate>::class.java)

            if(oldUsers.isNotEmpty()){
                users = oldUsers
            }
        }else{
            Log.e("ERROR","sin data")
        }
    }
    fun addPost(publicationTemplate:PublicationTemplate){
        posts.add(publicationTemplate)
        Log.e("AMMOUNT",posts.size.toString())
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}