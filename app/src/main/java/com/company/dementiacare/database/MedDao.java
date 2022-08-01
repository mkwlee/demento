package com.company.dementiacare.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;

@Dao

public interface MedDao{
    @Query("select * from med")
    List<Med> loadAllMeds();

    @Query("select * from med where id = :id")
    com.company.dementiacare.database.Med loadMedById(int id);

    @Query("select * from med where medName = :medname")
    Med loadMedByName(String medname);

    @Insert(onConflict = IGNORE)
    void insertMeds(com.company.dementiacare.database.Med med);

    @Query("DELETE FROM med")
    void deleteAll();

    @Query("select * from med where startDate = :StartDate")
    List<Med> loadMedByDate(String StartDate);

    @Query("select * from med where tagDaily = :tagDaily")
    List<Med> loadMedByTag(int tagDaily);

}