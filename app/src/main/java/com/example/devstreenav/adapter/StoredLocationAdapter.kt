package com.example.devstreenav.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.devstreenav.MainActivity
import com.example.devstreenav.R
import com.example.devstreenav.RoomDb.Mock
import com.example.devstreenav.RoomDb.Mock_DataBase


class StoredLocationAdapter(var context: Context, var rewareModel:List<Mock>,var allData: AllData):
    RecyclerView.Adapter<StoredLocationAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var delete: ImageView = itemView.findViewById(R.id.delete)
        var edit: ImageView = itemView.findViewById(R.id.edit)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.request_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db: Mock_DataBase = Mock_DataBase.getDbInstance(context)
        holder.name.text = rewareModel[position].name.toString()
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java).putExtra("type","pin")
                .putExtra("id",rewareModel[position].id).addFlags(FLAG_ACTIVITY_NEW_TASK))
        }
        holder.delete.setOnClickListener {
           db.mock_dao().deleteById(rewareModel[position].id)
            allData.remove(position)

//           notifyItemChanged(position)
        }
        holder.edit.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java).putExtra("type","edit")
                .putExtra("id",rewareModel[position].id).addFlags(FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun getItemCount(): Int {
      return rewareModel.size
    }

}
interface AllData {
    fun remove(position: Int)
}