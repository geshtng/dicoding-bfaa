package com.example.githubuser.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.githubuser.db.AppDatabase;
import com.example.githubuser.db.UserDAO;
import com.example.githubuser.views.fragment.FollowFragment;
import com.example.githubuser.R;
import com.example.githubuser.models.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    public static final String EXTRA_USER = "extra_user";
    private String uName;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean isAvatarShown = true;
    private int maxScrollSize;

    @BindView(R.id.detail_tabs) TabLayout tabLayout;
    @BindView(R.id.detail_viewpager) ViewPager viewPager;
    @BindView(R.id.detail_appbar) AppBarLayout appBarLayout;
    @BindView(R.id.detail_avatar) ImageView avatar;
    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.detail_progressBar) ProgressBar progressBar;
    @BindView(R.id.txt_detail_username) TextView username;
    @BindView(R.id.txt_detail_name) TextView name;
    @BindView(R.id.txt_detail_company) TextView company;
    @BindView(R.id.txt_detail_blog) TextView blog;
    @BindView(R.id.txt_detail_location) TextView location;
    @BindView(R.id.fab_favorite) FloatingActionButton fabFavorite;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        appBarLayout.addOnOffsetChangedListener(this);
        maxScrollSize = appBarLayout.getTotalScrollRange();

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        if (user != null) {
            uName = user.getUsername();
            username.setText(user.getUsername());
        }

        viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager(), uName));
        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(getSupportActionBar()).setTitle(uName);

        showProgressBar(true);
        setDetail(uName);

        UserDAO userDAO = Room.databaseBuilder(this, AppDatabase.class, "user")
                .allowMainThreadQueries()
                .build()
                .userDAO();

        User check = userDAO.getUserByUsername(uName);
        if (check != null) {
            fabFavorite.setVisibility(View.GONE);
        } else {
            fabFavorite.setOnClickListener(view -> {
                userDAO.insertAll(user);

                Toast.makeText(this, R.string.fav_success_add, Toast.LENGTH_SHORT).show();

                fabFavorite.setVisibility(View.GONE);
            });
        }
    }

    public void setDetail(final String uName) {
        String url = "https://api.github.com/users/" + uName;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "DELETE TOKEN");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);

                    if (responseObject.getString("name").equals("null")) name.setVisibility(View.GONE);
                    else name.setText(responseObject.getString("name"));

                    if (responseObject.getString("company").equals("null")) company.setVisibility(View.GONE);
                    else company.setText(responseObject.getString("company"));

                    if (responseObject.getString("blog").equals("null")) blog.setVisibility(View.GONE);
                    else blog.setText(responseObject.getString("blog"));

                    if (responseObject.getString("location").equals("null")) location.setVisibility(View.GONE);
                    else location.setText(responseObject.getString("location"));

                    name.setText(responseObject.getString("name"));
                    Picasso.get()
                            .load(responseObject.getString("avatar_url"))
                            .resize(200,200)
                            .centerCrop()
                            .into(avatar);

                    showProgressBar(false);
                } catch (Exception e){
                    Log.d("[Exception] : ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("[onFailure] : ", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollSize == 0)
            maxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / maxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && isAvatarShown) {
            isAvatarShown = false;

            avatar.animate()
                    .scaleX(0).scaleY(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !isAvatarShown) {
            isAvatarShown = true;

            avatar.animate()
                    .scaleX(1).scaleY(1)
                    .start();
        }
    }

    private void showProgressBar (Boolean state) {
        if (state) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;
        private final Context mContext;
        private String username;

        SectionsPagerAdapter(Context context, FragmentManager fm, String username) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            mContext = context;
            this.username = username;
        }

        @StringRes
        private final int[] TAB_TITLES = new int[] {
                R.string.tab_follower,
                R.string.tab_following
        };

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            Bundle data = new Bundle();
            data.putString("username", username);

            if (i == 0) data.putString("query", "followers");
            else data.putString("query", "following");

            FollowFragment followFragment = new FollowFragment();
            followFragment.setArguments(data);

            return followFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mContext.getResources().getString(TAB_TITLES[position]);
        }
    }
}
