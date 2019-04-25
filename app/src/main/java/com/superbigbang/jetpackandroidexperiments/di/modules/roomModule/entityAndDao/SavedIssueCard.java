package com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class SavedIssueCard {
    @PrimaryKey
    private long id;

    private String titleOfIssue;
    private String creatorName;
    private String author_avatar;


    @Ignore
    public SavedIssueCard(long id) {
        this.id = id;
    }

    public SavedIssueCard(long id, String titleOfIssue, String creatorName, String author_avatar) {
        this.id = id;
        this.titleOfIssue = titleOfIssue;
        this.creatorName = creatorName;
        this.author_avatar = author_avatar;
    }
}
