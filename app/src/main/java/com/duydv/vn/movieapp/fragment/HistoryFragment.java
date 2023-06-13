package com.duydv.vn.movieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.activity.PlayMovieActivity;
import com.duydv.vn.movieapp.adapter.MovieHistoryAdapter;
import com.duydv.vn.movieapp.model.Movie;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {
    private List<Movie> mListMovieHistory;
    private MovieHistoryAdapter mMovieHistoryAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);

        RecyclerView rcv_history = view.findViewById(R.id.rcv_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_history.setLayoutManager(linearLayoutManager);

        mListMovieHistory = new ArrayList<>();
        mMovieHistoryAdapter = new MovieHistoryAdapter(mListMovieHistory, getActivity(), new MovieHistoryAdapter.IOnClickListenter() {
            @Override
            public void onClickDelete(Movie movie) {
                clickDeleteMovie(movie);
            }

            @Override
            public void onClickItemMovieHistory(Movie movie) {
                clickItemMovieHistory(movie);
            }
        });
        rcv_history.setAdapter(mMovieHistoryAdapter);

        getListMovieHistory();

        return view;
    }

    private void getListMovieHistory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        Query query = myRef.orderByChild("history").equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieHistory == null || mMovieHistoryAdapter == null) {
                    return;
                }
                mListMovieHistory.add(0,movie);
                mMovieHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieHistory == null || mListMovieHistory.isEmpty() || mMovieHistoryAdapter == null) {
                    return;
                }

                for(Movie movieEntity : mListMovieHistory){
                    if(movie.getId() == movieEntity.getId()){
                        mListMovieHistory.remove(movieEntity);
                        break;
                    }
                }
                mMovieHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieHistory == null || mListMovieHistory.isEmpty() || mMovieHistoryAdapter == null) {
                    return;
                }
                for (Movie movieDelete : mListMovieHistory) {
                    if (movie.getId() == movieDelete.getId()) {
                        mListMovieHistory.remove(movieDelete);
                        break;
                    }
                }
                mMovieHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickDeleteMovie(Movie movie) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        Map<String,Object> map = new HashMap<>();
        map.put("history",false);

        myRef.child(String.valueOf(movie.getId())).updateChildren(map);
    }

    private void clickItemMovieHistory(Movie movie){
        Intent intent = new Intent(getActivity(), PlayMovieActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_movie",movie);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
