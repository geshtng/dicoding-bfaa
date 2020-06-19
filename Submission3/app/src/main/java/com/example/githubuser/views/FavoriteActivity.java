package com.example.githubuser.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.githubuser.R;
import com.example.githubuser.db.AppDatabase;
import com.example.githubuser.db.UserDAO;
import com.example.githubuser.models.User;
import com.example.githubuser.views.adapter.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity {

    @BindView(R.id.fav_progressBar) ProgressBar progressBar;
    @BindView(R.id.fav_recyclerView) RecyclerView rvFavorite;
    @BindView(R.id.fav_txt_not_found) TextView txtNotFound;
    @BindView(R.id.fav_linearNotFound) LinearLayout linearNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Favorites");

        rvFavorite.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));

        FavoriteAdapter adapter = new FavoriteAdapter();
        adapter.notifyDataSetChanged();
        rvFavorite.setAdapter(adapter);

        UserDAO userDAO = Room.databaseBuilder(this, AppDatabase.class, "user")
                .allowMainThreadQueries()
                .build()
                .userDAO();

        List<User> listUser = userDAO.getAll();
        ArrayList<User> arrayUser = new ArrayList<>(listUser);

        txtNotFound.setText(R.string.empty_favorite);

        showLoading(true);
        if (listUser.isEmpty()) {
            showLoading(false);
            linearNotFound.setVisibility(View.VISIBLE);
        }

        adapter.setData(arrayUser);
        adapter.setOnItemClickCallback(data -> {
            Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_USER, data);
            startActivity(intent);
        });

        showLoading(false);
    }

    private void showLoading(Boolean state) {
        if (state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }
}
