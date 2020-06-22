package com.example.githubuser.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.githubuser.models.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDAO();
}
