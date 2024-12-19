package com.example.quizonline.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizonline.Adapter.UserListAdapter
import com.example.quizonline.R
import com.example.quizonline.databinding.FragmentProfileBinding
import com.example.quizonline.network.ApiClient
import com.example.quizonline.network.ApiServise
import com.example.quizonline.network.model.user.UserList
import com.example.quizonline.network.model.user.UserListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private lateinit var adapter: UserListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = UserListAdapter(UserList(arrayListOf()))
        getDataResponse()
        return binding.root
    }

    fun getDataResponse() {
        val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("access_token", null)

        // Check if token is null before making the request
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Token is missing or invalid", Toast.LENGTH_SHORT)
                .show()
            return
        }


        // Make the API call
        val apiService = ApiClient.getRetrofit().create(ApiServise::class.java)
        val call =
            apiService.getScore(
                "Bearer $token",
                5
            )  // Assuming the token is passed as "Bearer <token>"

        call.enqueue(object : Callback<ArrayList<UserListItem>> {
            override fun onResponse(
                call: Call<ArrayList<UserListItem>>,
                response: Response<ArrayList<UserListItem>>
            ) {
                if (response.isSuccessful) {


                    // Process the topics data
                    response.body()?.let { topics ->
                        adapter = UserListAdapter(UserList(topics))

                        binding.recyclerView.adapter = adapter

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ArrayList<UserListItem>>, t: Throwable) {
                // Log the error and display a failure message
                Toast.makeText(requireContext(), "Network Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}