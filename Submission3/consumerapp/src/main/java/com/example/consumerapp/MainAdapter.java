package com.example.consumerapp;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private Cursor mCursor;

    void setData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);

        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainViewHolder holder, int position) {
        holder.setItem(mCursor.moveToPosition(position));
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fav_txt_username) TextView username;
        @BindView(R.id.fav_txt_htmlUrl) TextView html_url;
        @BindView(R.id.fav_img_avatar) ImageView avatar;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void setItem(boolean moveToPosition) {
            if (moveToPosition) {
                html_url.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(MainActivity.COLUMN_HTML_URL)));
                username.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(MainActivity.COLUMN_USERNAME)));
                Picasso.get()
                        .load(mCursor.getString(mCursor.getColumnIndexOrThrow(MainActivity.COLUMN_AVATAR)))
                        .resize(100, 100)
                        .centerCrop()
                        .into(avatar);
            }
        }
    }
}
