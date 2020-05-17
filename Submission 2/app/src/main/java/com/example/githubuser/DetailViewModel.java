package com.example.githubuser;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DetailViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<User>> listUser = new MutableLiveData<>();

    void fetchFollow(final String username, String queryType) {
        final ArrayList<User> listItems = new ArrayList<>();

        String url = "https://api.github.com/users/" + username + "/" + queryType;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", "request");
        client.addHeader("Authorization", "DELETE TOKEN");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONArray list = new JSONArray(result);

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject obj = list.getJSONObject(i);
                        User user = new User();
                        user.setUsername(obj.getString("login"));
                        user.setHtmlUrl(obj.getString("html_url"));
                        user.setAvatar(obj.getString("avatar_url"));

                        listItems.add(user);
                    }

                    listUser.postValue(listItems);
                } catch (Exception e) {
                    Log.d("[Exception] : ", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("[onFailure] : ", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    LiveData<ArrayList<User>> getUsers() { return listUser; }
}
