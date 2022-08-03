//package com.company.dementiacare.database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//
//import com.company.dementiacare.database.Med;
//import com.company.dementiacare.UserHelper;
//import com.company.dementiacare.applayer.Converter;
//
//@Database(entities = {Med.class, UserHelper.class}, version = 1)
//@TypeConverters(Converter.class)
//
//public abstract class AppDatabase extends RoomDatabase {
//
//    private static com.company.dementiacare.database.AppDatabase INSTANCE;
//    public abstract MedDao medModel();
//    public abstract UserDao userModel();
//
//    public static com.company.dementiacare.database.AppDatabase getInMemoryDatabase(Context context){
//
//        if(INSTANCE == null) {
//            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Demento")
//                    .allowMainThreadQueries()
//                    .build();
//        }
//        return INSTANCE;
//    }
//
//    public static void destroyInstance() {INSTANCE = null;}
//}