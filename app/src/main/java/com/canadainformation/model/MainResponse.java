package com.canadainformation.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created By Akash
 * on 17,Dec,2019 : 2:45 PM
 */
@Entity(tableName = "title")
public class MainResponse {

    @PrimaryKey
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @Ignore
    @SerializedName("rows")
    @Expose
    private List<Information> informationList;

    public MainResponse() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Information> getInformationList() {
        return informationList;
    }

    public void setInformationList(List<Information> informationList) {
        this.informationList = informationList;
    }
}
