package com.example.reto1aplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.reto1aplication.databinding.ActivityMainMenuBinding
import com.google.gson.Gson


class TabMenuActivity : AppCompatActivity() {
    private lateinit var homeFrag: HomeFrag
    private lateinit var publicationFrag: PublicationFrag
    private lateinit var profileFrag: ProfileFrag

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val userLogged = intent.getStringExtra("user")
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var user = Gson().fromJson(userLogged, UserTemplate::class.java)
        homeFrag = HomeFrag.newInstance()
        publicationFrag = PublicationFrag.newInstance(user)
        profileFrag = ProfileFrag.newInstance(user)

        publicationFrag.listener = homeFrag
        showFragment(homeFrag)

        binding.navigator.setOnItemSelectedListener { menuItem->
            if(menuItem.itemId == R.id.homeItem){
                showFragment(homeFrag)
            }else if(menuItem.itemId == R.id.postItem){
                showFragment(publicationFrag)
            }else if (menuItem.itemId == R.id.profileItem){
                showFragment(profileFrag)
            }
            true
        }
    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer,fragment)
        transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}