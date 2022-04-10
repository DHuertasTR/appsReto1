package com.example.reto1aplication

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.reto1aplication.databinding.FragmentNewPostBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PublicationFrag(private val userTemplateLogged:UserTemplate): Fragment() {

    private var _binding: FragmentNewPostBinding?=null
    private val binding get() = _binding!!
    private var permissionAccepted = false
    private var id:String= ""
    private var image:String=""
    private var file:File? =null

    var listener: OnNewPostListerner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPostBinding.inflate(inflater,container,false)
        var view = binding.root
        val newPostFragment = newInstance(userTemplateLogged)
        binding.btnNewPost.setOnClickListener{

            listener?.let{


                val author = this.userTemplateLogged.toString()
                val city = binding.spinnerCities.selectedItem.toString()
                val date = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
                val description = binding.textDescription.text.toString()
                if( author.isNullOrEmpty() or city.isNullOrEmpty() or date.isNullOrEmpty() or description.isNullOrEmpty() ){
                    Toast.makeText(activity,"Datos incompletos o errados",Toast.LENGTH_LONG).show()
                }else{

                    binding.textDescription.text.clear()

                    it.onNewPost(id,userTemplateLogged,city,date,description,image)
                    Toast.makeText(activity,"OK",Toast.LENGTH_LONG).show()

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer,newPostFragment)
                    transaction.commit()
                }
            }
        }
        val cameraLauncher = registerForActivityResult(StartActivityForResult(),:: onCameraResult)
        val galleryLauncher = registerForActivityResult(StartActivityForResult(),:: onGalleryResult)

        binding.btnCamera.setOnClickListener{
            requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),1)

            if(permissionAccepted){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                id = UUID.randomUUID().toString()
                file = File("${context?.getExternalFilesDir(null)}/photo_post_${id}.png")
                val uri = FileProvider.getUriForFile(requireContext(),context?.packageName!!,file!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
                this.image = uri.toString()
                Log.e(">>>",uri?.path.toString())

                cameraLauncher.launch(intent)
            }
        }

        binding.btnGalery.setOnClickListener{
            requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),1)
            if(permissionAccepted) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                galleryLauncher.launch(intent)
            }
        }
        return view
    }

    fun onCameraResult(result: ActivityResult){
//        val bitMap = result.data?.extras?.get("data") as Bitmap
//        binding.imageView2.setImageBitmap(bitMap)
        if(result.resultCode == RESULT_OK){

            val bitmap = BitmapFactory.decodeFile(file?.path)
            val thumbnail = Bitmap.createScaledBitmap(bitmap, 256,128,true)
            binding.imageNewPost.setImageBitmap(thumbnail)

        }
    }

    fun onGalleryResult(result: ActivityResult){
        if(result.resultCode == RESULT_OK){
            val sourceTreeUri: Uri = result.data?.data!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                requireContext().contentResolver.takePersistableUriPermission(
                    sourceTreeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            val uriImage = result.data?.data
            image = uriImage.toString()
            Log.e("URI",uriImage.toString())
            Log.e("PATH",image)
            uriImage?.let {
                binding.imageNewPost.setImageURI(uriImage)

            }
        }
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            var allGrand = true
            for(result in grantResults){
                if(result == PackageManager.PERMISSION_DENIED){
                    allGrand = false
                    break
                }
            }
            permissionAccepted = allGrand
        }
    }
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface  OnNewPostListerner{
        fun onNewPost(
            id:String,
            author:UserTemplate,
            city:String,
            date:String,
            description:String,
            image: String
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(userTemplate: UserTemplate) = PublicationFrag(userTemplate)

    }
}