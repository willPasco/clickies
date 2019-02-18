package com.willpasco.clickies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.willpasco.clickies.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.moview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.onBind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
        } else {
            return 0;
        }
    }

    public void addAll(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.image_view_movie_poster);
        }

        void onBind(Movie model) {

            Glide.with(moviePoster.getContext())
                    .load("http://image.tmdb.org/t/p/w185/"+model.getPosterPath())
                    .fitCenter()
                    .into(moviePoster);
        }

    }
}
