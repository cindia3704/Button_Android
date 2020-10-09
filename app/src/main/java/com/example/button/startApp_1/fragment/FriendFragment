package com.example.button.startApp_1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.button.R


class FriendFragment : Fragment() {


    private var userId = 0

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

        reqFriendList()
    }

    private fun reqFriendList(){

    }
}
