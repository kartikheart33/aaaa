package com.example.devstreenav.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devstreenav.MainActivity
import com.example.devstreenav.R
import com.example.devstreenav.RoomDb.Mock
import com.example.devstreenav.RoomDb.Mock_DataBase
import com.example.devstreenav.adapter.AllData
import com.example.devstreenav.adapter.StoredLocationAdapter
import com.example.devstreenav.databinding.ActivityMainBinding
import com.example.devstreenav.databinding.ActivityStoreLocationListBinding

class StoreLocationListActivity : AppCompatActivity(), AllData {
    lateinit var binding: ActivityStoreLocationListBinding
    lateinit var adapter:StoredLocationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val db: Mock_DataBase = Mock_DataBase.getDbInstance(applicationContext)

        var mockList: List<Mock> = db.mock_dao().getAllMockList()

        if (mockList.isEmpty()){
            binding.mainll.visibility = View.GONE
            binding.addfirst.visibility=View.VISIBLE
        }else{
            binding.mainll.visibility = View.VISIBLE
            binding.addfirst.visibility=View.GONE
        }

        binding.addfirst.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }



        binding.addlocation.setOnClickListener {
            binding.addfirst.performClick()
        }
        binding.routeloctions.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java).putExtra("type","route"))
        }
         adapter = StoredLocationAdapter( applicationContext, mockList,this)
        binding.recyclerview.adapter = adapter
        Log.d("000", "onCreate=====>>"+mockList.size.toString())
    }

    override fun remove(position: Int) {
        val db: Mock_DataBase = Mock_DataBase.getDbInstance(applicationContext)
        var mockList: List<Mock> = db.mock_dao().getAllMockList()
//        adapter.notifyItemRemoved(position)
        val adapter = StoredLocationAdapter( applicationContext, mockList,this)
        binding.recyclerview.adapter = adapter
        if(mockList.isEmpty()){
            binding.mainll.visibility = View.GONE
            binding.addfirst.visibility=View.VISIBLE
        }
    }
}