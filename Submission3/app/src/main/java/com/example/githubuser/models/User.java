package com.example.githubuser.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "avatar")
    private String avatar;

    @ColumnInfo(name = "html_url")
    private String htmlUrl;

    public int getUid() { return uid; }

    public void setUid(int uid) { this.uid = uid; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHtmlUrl() { return htmlUrl; }

    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    public User() {
    }

    private User(Parcel in) {
        uid = in.readInt();
        username = in.readString();
        avatar = in.readString();
        htmlUrl = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(username);
        parcel.writeString(avatar);
        parcel.writeString(htmlUrl);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
