package com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SavedIssueCardDao {
    @Query("SELECT * FROM SavedIssueCard")
    List<SavedIssueCard> getAll();

    @Query("SELECT * FROM SavedIssueCard WHERE id = :id")
    SavedIssueCard getById(long id);

    @Insert
    void insert(SavedIssueCard employee);

    @Update
    void update(SavedIssueCard employee);

    @Delete
    void delete(SavedIssueCard employee);
}
