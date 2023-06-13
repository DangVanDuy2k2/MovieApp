package com.duydv.vn.movieapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duydv.vn.movieapp.R;
import com.duydv.vn.movieapp.model.Movie;

import java.util.List;


public class MovieHistoryAdapter extends RecyclerView.Adapter<MovieHistoryAdapter.MovieHistoryHolder> {
    private List<Movie> mListMovieHistory;
    private Activity mActivity;
    private IOnClickListenter mIOnClickListenter;

    public interface IOnClickListenter{
        void onClickDelete(Movie movie);
        void onClickItemMovieHistory(Movie movie);
    }

    public MovieHistoryAdapter(List<Movie> mListMovieHistory, Activity mActivity, IOnClickListenter mIOnClickListenter) {
        this.mListMovieHistory = mListMovieHistory;
        this.mActivity = mActivity;
        this.mIOnClickListenter = mIOnClickListenter;
    }

    @NonNull
    @Override
    public MovieHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_history,parent,false);
        return new MovieHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHistoryHolder holder, int position) {
        Movie movie = mListMovieHistory.get(position);
        if(movie == null){
            return;
        }

        if(movie.getImage() != null && !movie.getImage().equals("")){
            Glide.with(mActivity).load(movie.getImage()).error(R.drawable.ic_no_image).into(holder.img_movie_history);
        }else{
            holder.img_movie_history.setImageResource(R.drawable.ic_no_image);
        }

        holder.txt_title_history.setText(movie.getTitle());

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIOnClickListenter.onClickDelete(movie);
            }
        });

        holder.item_movie_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIOnClickListenter.onClickItemMovieHistory(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListMovieHistory != null){
            return mListMovieHistory.size();
        }
        return 0;
    }

    public class MovieHistoryHolder extends RecyclerView.ViewHolder{
        private ImageView img_movie_history,img_delete;
        private TextView txt_title_history;
        private RelativeLayout item_movie_history;

        public MovieHistoryHolder(@NonNull View itemView) {
            super(itemView);

            img_movie_history = itemView.findViewById(R.id.img_movie_history);
            img_delete = itemView.findViewById(R.id.img_delete);
            txt_title_history = itemView.findViewById(R.id.txt_title_history);
            item_movie_history = itemView.findViewById(R.id.item_movie_history);
        }
    }
}
