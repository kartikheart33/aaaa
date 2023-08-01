package com.example.devstreenav.RoomDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Mock_Dao {

    @Query("SELECT * FROM storedmap")
    List<Mock> getAllMockList();

    @Insert
    void inserImage(Mock favorite);

    @Query("DELETE FROM storedmap WHERE id = :id")
    void deleteById(int id);

    @Delete
    void delete(Mock favorite);

    @Update
    Void update(Mock favorite);

    @Query("DELETE FROM storedmap")
    public void nukeTable();

}
