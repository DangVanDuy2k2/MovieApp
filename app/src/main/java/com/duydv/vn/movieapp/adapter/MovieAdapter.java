package com.duydv.vn.movieapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.model.Movie;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> mListMovie;
    private Activity mActivity;
    private IOnClickListener mIOnClickListener;

    public interface IOnClickListener{
        void onClickFavorite(Movie movie);
    }

    public MovieAdapter(List<Movie> mListMovie, Activity mActivity, IOnClickListener mIOnClickListener) {
        this.mListMovie = mListMovie;
        this.mActivity = mActivity;
        this.mIOnClickListener = mIOnClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mListMovie.get(position);
        if(movie == null){
            return;
        }

        holder.txt_movie_name.setText(movie.getTitle());

        if(movie.getImage() != null && !movie.getImage().equals("")){
            //Glide thu vien tai anh
            Glide.with(mActivity).load(movie.getImage()).error(R.drawable.ic_no_image).into(holder.img_movie);
        }else {
            holder.img_movie.setImageResource(R.drawable.ic_no_image);
        }

        if(movie.isFavorite()){
            holder.img_ic_favorite.setImageResource(R.drawable.ic_favorite_on);
        }else{
            holder.img_ic_favorite.setImageResource(R.drawable.ic_favorite_off);
        }

        holder.img_ic_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIOnClickListener.onClickFavorite(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListMovie != null){
            return mListMovie.size();
        }
        return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_movie,img_ic_favorite;
        private TextView txt_movie_name;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            img_movie = itemView.findViewById(R.id.img_movie);
            img_ic_favorite = itemView.findViewById(R.id.img_ic_favorite);
            txt_movie_name = itemView.findViewById(R.id.txt_movie_name);
        }
    }
}
