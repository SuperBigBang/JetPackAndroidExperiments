package com.superbigbang.jetpackandroidexperiments.model.issueResponse;

import com.squareup.moshi.Json;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    @Json(name = "url")
    public String url;
    @Json(name = "repository_url")
    public String repositoryUrl;
    @Json(name = "labels_url")
    public String labelsUrl;
    @Json(name = "comments_url")
    public String commentsUrl;
    @Json(name = "events_url")
    public String eventsUrl;
    @Json(name = "html_url")
    public String htmlUrl;
    @Json(name = "id")
    public Integer id;
    @Json(name = "node_id")
    public String nodeId;
    @Json(name = "number")
    public Integer number;
    @Json(name = "title")
    public String title;
    @Json(name = "user")
    public User user;
    @Json(name = "labels")
    public List<Object> labels = null;
    @Json(name = "state")
    public String state;
    @Json(name = "locked")
    public Boolean locked;
    @Json(name = "assignee")
    public Object assignee;
    @Json(name = "assignees")
    public List<Object> assignees = null;
    @Json(name = "milestone")
    public Object milestone;
    @Json(name = "comments")
    public Integer comments;
    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "updated_at")
    public String updatedAt;
    @Json(name = "closed_at")
    public Object closedAt;
    @Json(name = "author_association")
    public String authorAssociation;
    @Json(name = "body")
    public String body;
}