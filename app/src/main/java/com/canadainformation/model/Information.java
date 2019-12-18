package com.canadainformation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:45 PM
 */

/**
 * Immutable model class for a Quake.
 */
@Entity(tableName = "information")
public final class Information {

    @Ignore
    private static final long STALE_MS = 5 * 60 * 1000; // Data is stale after 5 minutes

    @PrimaryKey
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "imageHref")
    @SerializedName("imageHref")
    @Expose
    private String imageHref;

    @ColumnInfo(name = "timeStamp")
    @Expose
    private Long timeStampAdded;


    public Information() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    public Long getTimeStampAdded() {
        return timeStampAdded;
    }

    public void setTimeStampAdded(Long timeStampAdded) {
        this.timeStampAdded = timeStampAdded;
    }

    public boolean isUpdated() {
        return System.currentTimeMillis() - timeStampAdded < STALE_MS;
    }

}
