package com.example.sub1_githubuser;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username, name, company, location;
    private String avatar;
    private String repository, follower, following;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public User() {
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        company = in.readString();
        location = in.readString();
        avatar = in.readString();
        repository = in.readString();
        follower = in.readString();
        following = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(company);
        parcel.writeString(location);
        parcel.writeString(avatar);
        parcel.writeString(repository);
        parcel.writeString(follower);
        parcel.writeString(following);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
