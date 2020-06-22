package com.example.githubuser.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.githubuser.R;
import com.example.githubuser.viewmodels.DetailViewModel;
import com.example.githubuser.views.adapter.FollowAdapter;

import java.util.Objects;

public class FollowFragment extends Fragment {
    private RecyclerView rootView;
    private String username, queryType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        rootView = view.findViewById(R.id.follow_recycler_view);
        rootView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

        assert getArguments() != null;
        username = getArguments().getString("username");
        queryType = getArguments().getString("query");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FollowAdapter adapter = new FollowAdapter();
        rootView.setAdapter(adapter);

        DetailViewModel detailViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DetailViewModel.class);
        detailViewModel.fetchFollow(username, queryType);
        detailViewModel.getUsers().observe(getViewLifecycleOwner(), adapter::setData);
    }
}
