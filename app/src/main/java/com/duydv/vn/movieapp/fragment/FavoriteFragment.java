package com.duydv.vn.movieapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.activity.PlayMovieActivity;
import com.duydv.vn.movieapp.adapter.MovieAdapter;
import com.duydv.vn.movieapp.model.Movie;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private MovieAdapter mMovieAdapter;
    private List<Movie> mListMovieFavorite;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        RecyclerView rcv_favorite = view.findViewById(R.id.rcv_favorite);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rcv_favorite.setLayoutManager(gridLayoutManager);

        mListMovieFavorite = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mListMovieFavorite, getActivity(), new MovieAdapter.IOnClickListener() {
            @Override
            public void onClickFavorite(Movie movie) {
                clickFavorite(movie);
            }

            @Override
            public void onClickMovie(Movie movie) {
                clickItemMovie(movie);
            }
        });
        rcv_favorite.setAdapter(mMovieAdapter);

        getListMovie();

        return view;
    }

    private void getListMovie(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        Query query = myRef.orderByChild("favorite").equalTo(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieFavorite == null || mMovieAdapter == null) {
                    return;
                }
                mListMovieFavorite.add(0,movie);
                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieFavorite == null || mListMovieFavorite.isEmpty() || mMovieAdapter == null) {
                    return;
                }

                for(Movie movieEntity : mListMovieFavorite){
                    if(movie.getId() == movieEntity.getId()){
                        mListMovieFavorite.remove(movieEntity);
                        break;
                    }
                }
                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovieFavorite == null || mListMovieFavorite.isEmpty() || mMovieAdapter == null) {
                    return;
                }
                for (Movie movieDelete : mListMovieFavorite) {
                    if (movie.getId() == movieDelete.getId()) {
                        mListMovieFavorite.remove(movieDelete);
                        break;
                    }
                }
                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clickFavorite(Movie movie) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        myRef.child(String.valueOf(movie.getId())).updateChildren(movie.toMap());
    }

    private void clickItemMovie(Movie movie) {
        Intent intent = new Intent(getActivity(), PlayMovieActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_movie",movie);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
