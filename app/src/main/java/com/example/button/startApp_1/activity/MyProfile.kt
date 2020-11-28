package com.example.button.startApp_1.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.button.BuildConfig
import com.example.button.R
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_add_closet.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.net.URLEncoder
import androidx.core.content.FileProvider
import com.bumptech.glide.request.RequestOptions
import com.example.button.startApp_1.data.Cloth
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Callback
import retrofit2.Retrofit


class MyProfile : AppCompatActivity() {
    var userId: Int=1
    private var REQ_CAMERA_PERMISSION = 1001
    private var REQ_IMAGE_CAPTURE = 2001
    private var REQ_IMAGE_GAEERY = 2002

    private var imagePath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        userId=getIntent().getIntExtra("id",3)
        RetrofitClient.retrofitService.getUserSpecific(
            userId,
            "Token " + RetrofitClient.token
        ).enqueue(object :
            retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val email = response.body()!!.userEmail
                val nickname_=response.body()!!.userNickName
                val gender=response.body()!!.userGender
                val photo=response.body()!!.photo
                //var gender_="기타"
                if(gender=="FEMALE"){
                    gender_female.isChecked=true
                }
                else if(gender=="MALE"){
                    //gender_="남성"
                    gender_male.isChecked=true
                }
                else{
                    gender_etc.isChecked=true
                }
                profile_id.setText(email.toString())
                profile_name.setText(nickname_)
                profile_name.setSelection(nickname_.length)
                if(photo!="media/button/default.jpg") {
                    Glide.with(this@MyProfile)
                        .load(RetrofitClient.imageBaseUrl + photo)
                        .placeholder(R.drawable.person__icon1)
                        .apply(RequestOptions.circleCropTransform()).into(my_profile_pic)
                }

            }
        })


        withdrawal.setOnClickListener {
            RetrofitClient.retrofitService.withdrawal(userId,"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        val data = response.message()
                        if(response.isSuccessful){
                            Toast.makeText(this@MyProfile,"회원탈퇴가 정상적으로 처라됐습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }
                })
        }

        change_pw.setOnClickListener {
            val intent= Intent(this@MyProfile,ChangePasswordActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
        logout.setOnClickListener {
            val intent= Intent(this@MyProfile,SplashActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@MyProfile,"로그아웃이 정상적으로 되었습니다",Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            }
        }
//        change_myinfo.setOnClickListener {
//            update_user_info()
//        }
        my_profile_pic.setOnClickListener {
            checkPermission()
        }
        change_myinfo.setOnClickListener{
            var imageFile: File? = null
            var photo: MultipartBody.Part? = null
            var fileBody: RequestBody? = null
            var user_name = profile_name.text.toString()
            var usernickname=RequestBody.create(MediaType.parse("text/plain"), user_name)
            if (!TextUtils.isEmpty(imagePath)) {
                Log.e("My profile","imagePath is nmot empty imagePath="+imagePath)
                imageFile = File(imagePath)
                fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)

                photo = MultipartBody.Part.createFormData(
                    "photo",
                    URLEncoder.encode(imageFile.name, "utf-8"),
                    fileBody
                )
                Toast.makeText(
                    this@MyProfile,
                    "사진 셀렉 완 료",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Log.e("addClosetActivity","imagePath is empty imagePath="+imagePath)
            }
            //val radioButton = findViewById<RadioButton>(gender_all)
            var gender__=RequestBody.create(MediaType.parse("text/plain"), "ETC")

            if(gender_female.isChecked){
                gender__=RequestBody.create(MediaType.parse("text/plain"), "FEMALE")
//                Toast.makeText(
//                    this@MyProfile,
//                    "성별:"+gender__.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            else if(gender_male.isChecked){
                gender__=RequestBody.create(MediaType.parse("text/plain"), "MALE")
//                Toast.makeText(
//                    this@MyProfile,
//                    "성별:"+gender__.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            else{
                //gender__="ETC"
                gender__=RequestBody.create(MediaType.parse("text/plain"), "ETC")
//                Toast.makeText(
//                    this@MyProfile,
//                    "성별:"+gender__.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            Log.d("myprofile",""+usernickname.toString()+"  "+gender__.toString())
            RetrofitClient.retrofitService.changeUserSpecific(userId,"Token " + RetrofitClient.token,
                userNickName = usernickname,
                userGender = gender__,
                photo=photo
            ).enqueue(object:retrofit2.Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("response", "response=${response}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MyProfile,
                            "정상적으로 수정 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //finish()

                    } else {

                    }            }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("myProfile error", t.toString())
                    TODO("Not yet implemented")
                    Toast.makeText(
                        this@MyProfile,
                        "수정 오류! ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        }

    }

    private fun checkPermission() {
        var permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permission == PackageManager.PERMISSION_DENIED) {
            // 권한 없는 경우
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQ_CAMERA_PERMISSION
            )
        } else {
            // 권한 있는 경우
            showPhotoDialog()
        }
    }
    private fun showPhotoDialog(){
        var items = arrayOf("사진 촬영 ","갤러리")

        var builder = AlertDialog.Builder(this);
        builder.setTitle("사진")
        builder.setItems(items, { dialogInterface, i ->

            if(i == 0){
                selectPhoto()
            }else{
                selectGallery()
            }

        })

        builder.show()

    }
    private fun selectGallery(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, REQ_IMAGE_GAEERY)

    }
    private fun createImageFile(): File { // 사진이 저장될 폴더 있는지 체크
        var file = File(
            getExternalFilesDir(Environment.DIRECTORY_DCIM),
            "/path/"
        )
        if (!file.exists()) file.mkdir()
        var imageName = "${System.currentTimeMillis()}.jpeg"
        var imageFile =
            File(
                "${getExternalFilesDir(Environment.DIRECTORY_DCIM)?.absoluteFile}/path/",
                "$imageName"
            )
        imagePath = imageFile.absolutePath
        return imageFile
    }
    private fun selectPhoto() {
        var state = Environment.getExternalStorageState()
        if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)?.let {
                var photoFile: File? = createImageFile()
                photoFile?.let {
                    var photoUri = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        it
                    )
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        photoUri
                    )
                    startActivityForResult(intent, REQ_IMAGE_CAPTURE)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(
            "imagePath2",
            "requestCode=${requestCode}\nresultCode=${resultCode}\nimagePath=${imagePath}"
        )

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQ_IMAGE_CAPTURE -> {
                    imagePath.apply {
                        Glide.with(this@MyProfile)
                            .load(this)
                            .placeholder(R.drawable.circle)
                            .apply(RequestOptions.circleCropTransform()).into(my_profile_pic)

                        // .apply(RequestOptions.circleCropTransform()).into(closet);

                    }


                }

                REQ_IMAGE_GAEERY -> {
                    data?.data?.let{
                        imagePath = getRealPathFromURI(it)
                        Log.e("test","imagePath="+imagePath)
                        Glide.with(this@MyProfile)
                            .load(imagePath)
                            .placeholder(R.drawable.circle)
                            .apply(RequestOptions.circleCropTransform()).into(my_profile_pic)

                    }

                }

            }
        }


    }
    private fun getRealPathFromURI(uri: Uri): String {
        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor.moveToFirst()) { columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) }
        return cursor.getString(columnIndex)
    }

    // 권한 요청한 결과 ㄱ밧 리턴
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when (requestCode) {
            REQ_CAMERA_PERMISSION -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 동의 했을 경우
                    showPhotoDialog()
                } else {
                    // 동의 안했을 경우
                    Toast.makeText(this, "권한 동의를 해야 가능합니다", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }

}

