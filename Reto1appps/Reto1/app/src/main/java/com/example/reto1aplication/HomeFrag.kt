package com.example.reto1aplication

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reto1aplication.databinding.FragmentNewHomeBinding

class HomeFrag : Fragment(),PublicationFrag.OnNewPostListerner {

    private var _binding: FragmentNewHomeBinding?=null

    private val binding get() =_binding!!

    private var adapter = PublicationHolder()

    private var adapterTEST=adapter.getItemId(5).hashCode()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        _binding = FragmentNewHomeBinding.inflate(inflater,container, false)
        val view = binding.root

        val postRecycler = binding.postRecycler
        postRecycler.setHasFixedSize(true)
        postRecycler.layoutManager = LinearLayoutManager(activity)

        postRecycler.adapter = adapter

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        adapter.onResume(sharedPreferences)
        return view
    }

    override fun onDestroyView() {
        try {
            var adapterTEST=adapter.getItemId(5).hashCode()
        }catch (e:Exception){
            e.printStackTrace()
        }


        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFrag()
    }

    /*
    override fun onNewPost(
        id:String,
        title:String,
        author:UserTemplate,
        city:String,
        date:String,
        description:String,
        image: String,
        nameUser: String,
        iDimage:string
    ) {
        val newPost = PublicationTemplate(id,title,author,city,date,description,image,nameUser,idimage)
        adapter.addPost(newPost)
    }

     */

    override fun onNewPost(
        id:String,
        author:UserTemplate,
        city:String,
        date:String,
        description:String,
        image: String
    ) {
        val newPost = PublicationTemplate(id,author,city,date,description,image)
        adapter.addPost(newPost)
    }

    override fun onPause() {
        super.onPause()

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        adapter.onPause(sharedPreferences)
    }

}