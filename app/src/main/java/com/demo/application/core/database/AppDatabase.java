package com.demo.application.core.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.demo.application.core.application.DemoApp;
import com.demo.application.home.data.model.CategoryQuestion;


@Database(entities = {CategoryQuestion.class}, version = 1,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static final String DB_NAME = "passengerhmi_database";


   // public abstract OutletModelDao outletModelDao();



    public static AppDatabase getAppDatabase() {
        if (instance == null) {
            instance = Room.databaseBuilder(DemoApp.getAppContext(), AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
