package com.example.devstreenav.RoomDb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "storedmap")

public class Mock {
    @PrimaryKey(autoGenerate = true)
    public int id=0;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "lat")
    public String lat;

    @ColumnInfo(name = "longi")
    public String longi;



}
