package com.example.button.startApp_1.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.BuildConfig
import com.example.button.R
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.network.RetrofitClient
import com.example.button.startApp_1.network.RetrofitService
import kotlinx.android.synthetic.main.activity_add_closet.*
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.net.URLEncoder
import java.util.*

class AddClosetActivity : AppCompatActivity() {


    private var REQ_CAMERA_PERMISSION = 1001
    private var REQ_IMAGE_CAPTURE = 2001
    private var REQ_IMAGE_GAEERY = 2002

    private var imagePath = ""


    private var user_id = 1
    private var category = ""

    private var select_item: Cloth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_closet)

        user_id = intent.getIntExtra("userId", 1)
        Log.e("user_id","AddClosetActivity user_id="+user_id)
        category = intent.getStringExtra("category") ?: ""
        select_item = intent.getParcelableExtra("item")




        if (select_item == null) {
            checkPermission()
            delete.visibility = View.GONE
        } else {
            setUi(select_item!!)
            delete.visibility = View.VISIBLE
        }
        initUi()


    }

    private fun setUi(item: Cloth) {
        category = item.category
        closer_category.text = category

        if (item.season.contains("SUMMER")) {
            summer.isChecked = true
        }
        if (item.season.contains("HWAN")) {
            hwan.isChecked = true
        }
        if (item.season.contains("WINTER")) {
            winter.isChecked = true
        }

        if(item.style.contains("CASUAL")){
            casual.isChecked = true
        }

        if(item.style.contains("FORMAL")){
            suit.isChecked = true
        }
        if(item.style.contains("SEMI-FORMAL")){
            semi_suit.isChecked = true
        }
        if(item.style.contains("OUTDOOR")){
            outdoor.isChecked = true
        }
        if(item.style.contains("VACANCE")){
            vacance.isChecked = true
        }


        Glide.with(this@AddClosetActivity)
            .load(RetrofitClient.imageBaseUrl + item.photo)
            .placeholder(R.drawable.circle)
            .into(closet)
           // .apply(RequestOptions.circleCropTransform()).into(closet);

    }

    private fun initUi() {
        closer_category.text = category
        Log.e("testset","category="+category)

        if(TextUtils.equals("TOP",category) || TextUtils.equals("DRESS",category)){
            ll_style.visibility = View.VISIBLE
            tv_style.visibility = View.VISIBLE
        }

        delete.setOnClickListener {
            RetrofitClient.retrofitService.deleteCloset(
                "Token " + RetrofitClient.token,
                user_id,
                clothID = select_item?.clothID ?: 0
            ).enqueue(object :
                retrofit2.Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("response", "response=${response}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddClosetActivity,
                            "정상적으로 삭제 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    } else {

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("error", t.toString())
                }
            })
        }

        save.setOnClickListener {
            updateCloset()
        }
        closet.setOnClickListener {
            checkPermission()
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


    private fun updateCloset() {
        category = closer_category.text.toString()

        var imageFile: File? = null
        var photo: MultipartBody.Part? = null
        var fileBody: RequestBody? = null


        if (!TextUtils.isEmpty(imagePath)) {
            Log.e("addClosetActivity","imagePath is nmot empty imagePath="+imagePath)
            imageFile = File(imagePath)
            fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)

            photo = MultipartBody.Part.createFormData(
                "photo",
                URLEncoder.encode(imageFile.name, "utf-8"),
                fileBody
            )
        }else{
            Log.e("addClosetActivity","imagePath is empty imagePath="+imagePath)
        }


        var categoryBody = RequestBody.create(MediaType.parse("text/plain"), category)
        var closeIdBody = RequestBody.create(
            MediaType.parse("text/plain"),
            (select_item?.clothID ?: user_id).toString()
        )
        var style = mutableListOf<RequestBody>()
        if(casual.isChecked){
            style.add(RequestBody.create(MediaType.parse("text/plain"), "CASUAL"))
        }

        if(semi_suit.isChecked){
            style.add(RequestBody.create(MediaType.parse("text/plain"), "SEMI-FORMAL"))
        }

        if(suit.isChecked){
            style.add(RequestBody.create(MediaType.parse("text/plain"), "FORMAL"))
        }

        if(outdoor.isChecked){
            style.add(RequestBody.create(MediaType.parse("text/plain"), "OUTDOOR"))
        }

        if(vacance.isChecked){
            style.add(RequestBody.create(MediaType.parse("text/plain"), "VACANCE"))
        }


        var season = mutableListOf<RequestBody>()
        if (hwan.isChecked) {
            season.add(RequestBody.create(MediaType.parse("text/plain"), "HWAN"))
        }
        if (summer.isChecked) {
            season.add(RequestBody.create(MediaType.parse("text/plain"), "SUMMER"))
        }
        if (winter.isChecked) {
            season.add(RequestBody.create(MediaType.parse("text/plain"), "WINTER"))
        }

        Log.d("season size","season size="+season.size+"\nseason="+season.toString())


        if (select_item == null) {
            // 옷 입력
            RetrofitClient.retrofitService.insertCloset(
                "Token " + RetrofitClient.token,
                user_id,
                clothID = closeIdBody,
                category = categoryBody,
                season = season,
                style = style,
                photo = photo,
                coordiList = null
            ).enqueue(object :
                retrofit2.Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("response", "response=${response}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddClosetActivity,
                            "정상적으로 저장 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()
                    } else {

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("error", t.toString())
                }
            })
        } else {
            var coordi = mutableListOf<RequestBody>()
            for(i in 0 until select_item!!.outfit!!.size){
                coordi.add(RequestBody.create(MediaType.parse("text/plain"), "${select_item!!.outfit.get(i)}"))
            }
            var userIdBody = RequestBody.create(MediaType.parse("text/plain"), (user_id).toString())
            // 옷 수정
            RetrofitClient.retrofitService.updateCloset(
                "Token " + RetrofitClient.token,
                user_id,
                clothID = select_item!!.clothID,
                id = userIdBody,
                category = categoryBody,
                season = season,
                coordiList = coordi,
                photo = photo
            ).enqueue(object :
                retrofit2.Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("response", "response=${response}")
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AddClosetActivity,
                            "정상적으로 수정 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()

                    } else {

                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("error", t.toString())
                }
            })
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
                        Glide.with(this@AddClosetActivity)
                            .load(this)
                            .placeholder(R.drawable.circle)
                            .into(closet)

                           // .apply(RequestOptions.circleCropTransform()).into(closet);

                    }


                }

                REQ_IMAGE_GAEERY -> {
                    data?.data?.let{
                        imagePath = getRealPathFromURI(it)
                        Log.e("test","imagePath="+imagePath)
                        Glide.with(this@AddClosetActivity)
                            .load(imagePath)
                            .placeholder(R.drawable.circle)
                            .into(closet)

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
