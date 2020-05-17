package com.example.githubuser;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username, avatar, htmlUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getAvatar() {
        return avatar;
    }

    void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String getHtmlUrl() { return htmlUrl; }

    void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }

    User() {
    }

    private User(Parcel in) {
        username = in.readString();
        avatar = in.readString();
        htmlUrl = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
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
