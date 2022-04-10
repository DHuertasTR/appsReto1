package com.example.reto1aplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PublicationViewHold(itemView: View):RecyclerView.ViewHolder(itemView) {
    //UI Controllers
    var postAutorRow:TextView = itemView.findViewById(R.id.postAutorRow)
    var postCityRow:TextView = itemView.findViewById(R.id.postCityRow)
    var postDateRow:TextView = itemView.findViewById(R.id.postDateRow)
    var postDescriptionRow:TextView = itemView.findViewById(R.id.postDescriptionRow)
    var postAvatarRow:ImageView = itemView.findViewById(R.id.postAvatarRow)
    var postImageRow:ImageView = itemView.findViewById(R.id.postImageRow)
    //State

    init {

    }
}