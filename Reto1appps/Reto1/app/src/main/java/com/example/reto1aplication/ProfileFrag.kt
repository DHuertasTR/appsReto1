package com.example.reto1aplication

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.reto1aplication.databinding.FragmentNewProfileBinding
import java.io.File


class ProfileFrag (private var userTemplateLogged:UserTemplate): Fragment() {
    private var _binding: FragmentNewProfileBinding?=null
    private val binding get() = _binding!!

    private var permissionAccepted = false
    private var file:File?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        //Setting
        if(userTemplateLogged.photo!=""){
            binding.imageProfile.setImageURI(Uri.parse(userTemplateLogged.photo))
        }

        binding.testUserNameProfile.text = userTemplateLogged.user
        binding.userinput.hint=userTemplateLogged.user

        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),:: onCameraResult)
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),:: onGalleryResult)


        binding.btnLogOut.setOnClickListener {
            val mStartActivity = Intent(context, MainActivity::class.java)
            val mPendingIntentId = 123456
            val mPendingIntent = PendingIntent.getActivity(
                context,
                mPendingIntentId,
                mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val mgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr[AlarmManager.RTC, System.currentTimeMillis() + 10] = mPendingIntent
            System.exit(0)
        }

        binding.btnEditUserName.setOnClickListener {
            val nameTemp= binding.userinput.text.toString()
            userTemplateLogged.user=nameTemp
            binding.testUserNameProfile.text = userTemplateLogged.user
            Toast.makeText(this.context, "correcto $nameTemp", Toast.LENGTH_LONG).show()
        }

        binding.btnCamera.setOnClickListener{
            requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),1)

            if(permissionAccepted){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                file = File("${context?.getExternalFilesDir(null)}/photo_PROFILE_${id}.png")
                val uri = FileProvider.getUriForFile(requireContext(),context?.packageName!!,file!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
                userTemplateLogged.photo = uri.toString()

                Log.e(">>>",file?.path.toString())

                cameraLauncher.launch(intent)
            }
        }

        binding.btnGallery.setOnClickListener{
            requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),1)
            if(permissionAccepted) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.type = "image/*";
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                galleryLauncher.launch(intent)
            }
        }

        return view
    }

        fun onCameraResult(result: ActivityResult){

        if(result.resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/4,bitmap.height/4,true)
            binding.imageProfile.setImageBitmap(thumbnail)
        }else if(result.resultCode == Activity.RESULT_CANCELED){
            file = null
        }
    }

        fun onGalleryResult(result: ActivityResult){

        if(result.resultCode == Activity.RESULT_OK){
            val sourceTreeUri: Uri = result.data?.data!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                requireContext().contentResolver.takePersistableUriPermission(
                    sourceTreeUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            val uriImage = result.data?.data
            userTemplateLogged.photo = uriImage.toString()
            uriImage?.let {
                binding.imageProfile.setImageURI(uriImage)
            }
        }else if(result.resultCode == Activity.RESULT_CANCELED){
            file = null
        }
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(userTemplate:UserTemplate) = ProfileFrag(userTemplate)
    }
}
