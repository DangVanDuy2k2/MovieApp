package com.duydv.vn.movieapp.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.adapter.MovieAdapter;
import com.duydv.vn.movieapp.model.Movie;
import com.duydv.vn.movieapp.utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private EditText edt_search_movie;
    private ImageView img_search;
    private RecyclerView rcv_movie;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mListMovie;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        edt_search_movie = view.findViewById(R.id.edt_search_movie);
        img_search = view.findViewById(R.id.img_search);
        rcv_movie = view.findViewById(R.id.rcv_movie);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rcv_movie.setLayoutManager(gridLayoutManager);

        mListMovie = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(mListMovie, getActivity(), new MovieAdapter.IOnClickListener() {
            @Override
            public void onClickFavorite(Movie movie) {
                updateFavoriteMovie(movie);
            }
        });
        rcv_movie.setAdapter(mMovieAdapter);
        getListMovie("");

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMovie();
            }
        });

        edt_search_movie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    mListMovie.clear();
                    getListMovie("");
                }
            }
        });

        return view;
    }

    private void getListMovie(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if (movie == null || mListMovie == null || mMovieAdapter == null) {
                    return;
                }

                if (key == null || key.equals("")) {
                    mListMovie.add(movie);
                } else {
                    if (movie.getTitle().toLowerCase().trim().contains(key.toLowerCase().trim())) {
                        mListMovie.add(movie);
                    }
                }

                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Movie movie = snapshot.getValue(Movie.class);
                if(movie == null || mListMovie == null || mListMovie.isEmpty() || mMovieAdapter == null){
                    return;
                }

                for(Movie movieEntity : mListMovie){
                    if(movie.getId() == movieEntity.getId()){
                        movieEntity.setFavorite(movie.isFavorite());
                        break;
                    }
                }

                mMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateFavoriteMovie(Movie movie){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("movie");

        myRef.child(String.valueOf(movie.getId())).updateChildren(movie.toMap());
    }

    private void searchMovie(){
        String key = edt_search_movie.getText().toString().trim();
        if(mListMovie == null || mListMovie.isEmpty()){
            return;
        }
        mListMovie.clear();
        getListMovie(key);
        Utils.hideSoftKeyboard(getActivity());
    }
}
