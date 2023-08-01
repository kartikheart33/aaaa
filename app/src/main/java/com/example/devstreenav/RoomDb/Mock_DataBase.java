package com.example.devstreenav.RoomDb;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {Mock.class, }, version = 1, exportSchema = false)
public abstract class Mock_DataBase extends RoomDatabase {



    public abstract Mock_Dao mock_dao();



    private static Mock_DataBase INSTANCE;

    public static Mock_DataBase getDbInstance(Context context) {

        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Mock_DataBase.class, "BrainyQ_DB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
