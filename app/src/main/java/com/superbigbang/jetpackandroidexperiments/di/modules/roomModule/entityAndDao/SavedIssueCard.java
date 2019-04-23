package com.superbigbang.jetpackandroidexperiments.di.modules.roomModule.entityAndDao;

import androidx.room.Entity;
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
}
