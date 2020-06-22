package com.example.githubuser.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.githubuser.db.AppDatabase;
import com.example.githubuser.db.UserDAO;

import java.util.Objects;

public class FavoriteUserProvider extends ContentProvider {
    private static final int CODE_USER = 1;
    private static final String AUTHORITY = "com.example.githubuser.provider";
    private static final String TABLE_NAME = "user";
    private static final String DB_NAME = "user";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private UserDAO userDAO;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_USER);
    }

    @Override
    public boolean onCreate() {
        userDAO = Room.databaseBuilder(Objects.requireNonNull(getContext()), AppDatabase.class, DB_NAME)
                .build()
                .userDAO();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor = null;

        if (sUriMatcher.match(uri) == CODE_USER) {
            cursor = userDAO.selectAll();
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
