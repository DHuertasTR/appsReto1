package com.example.reto1aplication


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.reto1aplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.InputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var users = HashMap<String,UserTemplate>()


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            val inputStream: InputStream = File("data.txt").inputStream()
            val inputString = inputStream.bufferedReader().use { it.readText() }
            println(inputString)
        }catch (e: Exception){
            println(e.stackTrace)
        }

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        var json = sharedPreferences.getString("allUsers","NO_DATA")

        if(json != "NO_DATA"){
            Log.e("<<<",json.toString())
            Log.e("error","#############")
            val loggedUsers = Gson().fromJson<HashMap<String,UserTemplate>>(json.toString(),Array<PublicationTemplate>::class.java)
            if(loggedUsers.isNotEmpty()){
                users = loggedUsers
            }
        }else{
            Log.e("tag","no se encuentra la data")
        }
        if(users.isEmpty()){
            val user =  UserTemplate("a","a", "a","a")
            val user1 =  UserTemplate("dumy1","dummer", "alfa@gmail.com","aplicacionesmoviles")
            val user2 =  UserTemplate("dummy2","dummer2", "beta@gmail.com","aplicacionesmoviles")
            users.put("dumy1",user1);
            users.put("dummy2",user2);
            users.put("a",user)
        }

        //agregar metodo para manejar el estado de guardado!!!!
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener() {
            val i = Intent(this, TabMenuActivity::class.java)
            val email = binding.editTextTextEmailAddress.text.toString()
            val pass = binding.editTextTextPassword2.text.toString()
            var currentUserTemplate:UserTemplate? =null
            for(user in users.values){
                if((email == user.email) and (pass == user.password)){
                    currentUserTemplate = user
                    break
                }
            }

            //val userlog=this.setSupportActionBar()

            if(currentUserTemplate != null){
                i.putExtra("user", Gson().toJson(currentUserTemplate))
                startActivity(i)
            }else{
                Toast.makeText(this.baseContext,"Credenciales invalidas",Toast.LENGTH_LONG).show()
            }

        }
    }
}