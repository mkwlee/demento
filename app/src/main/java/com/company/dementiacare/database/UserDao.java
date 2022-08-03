//package com.company.dementiacare.database;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.company.dementiacare.UserHelper;
//
//import java.util.List;
//
//import static androidx.room.OnConflictStrategy.IGNORE;
//
//@Dao
//
//public interface UserDao {
//    @Query("select * from userhelper")
//    List<UserHelper> loadAllUsers();
//
//    @Query("select userPresent from userhelper")
//    boolean userPresent();
//
//    @Insert(onConflict = IGNORE)
//    Long insertUser(UserHelper user);
//
//    @Query("DELETE FROM userhelper")
//    int deleteUser();
//}
