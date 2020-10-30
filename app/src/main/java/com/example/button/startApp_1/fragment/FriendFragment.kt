package com.example.button.startApp_1.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.button.R
import com.example.button.startApp_1.adapter.FriendAdatper
import com.example.button.startApp_1.data.Friend
import com.example.button.startApp_1.data.FriendAddResponse
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_friend.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


class FriendFragment : Fragment() {


    private var userId = 0

    private var adapter = FriendAdatper(this)

    companion object{

        private const val MY_INT = "userId"

        fun newInstance(userId: Int) : FriendFragment{
            var bundle = Bundle()
            bundle.putInt(MY_INT, userId)
            var fragment = FriendFragment()
            fragment.arguments = bundle

            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_friend, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userId = arguments?.getInt(MY_INT) ?: 0

        friend_add.setOnClickListener {
            addFriend()
        }
        recyclerview_friend.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        reqFriendList()
    }

    fun deleteFriend(friendId : Int){
        RetrofitClient.retrofitService.deleteFriend(userId, friendId,"Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    val data = response.message()
                    if(response.isSuccessful){
                        Toast.makeText(context,"친구삭제가 성공적으로 됐습니다.",Toast.LENGTH_SHORT).show()
                        reqFriendList()
                    }

                }
            })
    }

    private fun addFriend(){

        var mContentView = View.inflate(context,R.layout.view_edittext,null)

        var friendEmail = mContentView.findViewById<EditText>(R.id.etName)
        var confirm  = mContentView.findViewById<Button>(R.id.confirm)
        var cancel  = mContentView.findViewById<Button>(R.id.cancel)


        context?.let{

                context ->
            var dialog = Dialog(context)
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(mContentView)

            }

            confirm.setOnClickListener {
                RetrofitClient.retrofitService.addFriend(userId, friendEmail.text.toString(),"Token " + RetrofitClient.token)
                    .enqueue(object : retrofit2.Callback<FriendAddResponse> {
                        override fun onFailure(call: Call<FriendAddResponse>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<FriendAddResponse>,
                            response: Response<FriendAddResponse>
                        ) {
                            val data = response.message()
                            if(response.isSuccessful){
                                Toast.makeText(context,"친구요청을 성공적으로 보냈습니다.",Toast.LENGTH_SHORT).show()

                                reqFriendList()

                            }else{

                                var errorBody = response.errorBody()
                                errorBody?.let{
                                    when (getErrorResponse(it).email){
                                        "already friend" -> {
                                            Toast.makeText(context,"친구로 등록되어 있는 사용자입니다.",Toast.LENGTH_SHORT).show()
                                        }

                                        "already sent request" -> {
                                            Toast.makeText(context,"친구 요청 전송을 완료 한 사용자입니다.",Toast.LENGTH_SHORT).show()
                                        }
                                        "user not found" -> {
                                            Toast.makeText(context,"해당 아이디를 가진 사용자가 없습니다.",Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            Toast.makeText(context,"친구추가가 성공적으로 됐습니다.",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                            }

                        }
                    })

                dialog.dismiss()
            }
            cancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }





    }
    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse {
        return RetrofitClient.retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(errorBody)
    }

    inner class ErrorResponse {
        var email = ""

    }

    private fun reqFriendList(){
        RetrofitClient.retrofitService.getFriendList(userId,"Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<MutableList<Friend>> {
                override fun onFailure(call: Call<MutableList<Friend>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<MutableList<Friend>>,
                    response: Response<MutableList<Friend>>
                ) {
                    val data = response.message()

                    Log.e("data","respon="+data)

                    var friendList = response.body()?: mutableListOf()
                    adapter.friendItems = friendList
                    adapter.notifyDataSetChanged()

                    for( i in 0 until friendList.size){

                        RetrofitClient.retrofitService.getUserSpecific(friendList[i].frienduser, "Token " + RetrofitClient.token)
                            .enqueue(object : retrofit2.Callback<User> {
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                }

                                override fun onResponse(
                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    val data = response.body()
                                    for(i in 0 until friendList.size){
                                        Log.e("friendFragment","i="+i+"\nfriendList[i].frienduser="+friendList[i].frienduser+"\ndata?.id="+data?.id)
                                        if(friendList[i].frienduser == data?.id){
                                            adapter.friendItems[i].friendName = data?.userNickName
                                            adapter.notifyDataSetChanged()
                                            break
                                        }
                                    }


                                }
                            })
                    }

                }
            })
    }
}
