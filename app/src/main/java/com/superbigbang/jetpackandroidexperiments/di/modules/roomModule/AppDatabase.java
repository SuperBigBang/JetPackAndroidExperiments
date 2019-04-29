package com.superbigbang.jetpackandroidexperiments.di.modules.roomModule;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao.SavedIssueCard;
import com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao.SavedIssueCardDao;

@Database(entities = {SavedIssueCard.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedIssueCardDao savedIssueCardDao();
}