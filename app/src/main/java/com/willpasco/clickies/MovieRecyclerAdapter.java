package com.willpasco.clickies;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.willpasco.clickies.model.Movie;
import com.willpasco.clickies.util.ImageLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.willpasco.clickies.MovieDetailsActivity.MOVIE_EXTRA_KEY;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {

    public static final String BASE_IMAGE_PATH = "http://image.tmdb.org/t/p/original/";
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
        private final ImageView moviePoster;
        private final TextView movieTitle;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.image_view_movie_poster);
            this.movieTitle = itemView.findViewById(R.id.text_view_movie_title);
        }

        void onBind(final Movie model) {
            ImageLoader.loadImageCenterCrop(BASE_IMAGE_PATH + model.getPosterPath(), moviePoster);
            movieTitle.setText(model.getTitle());
            moviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(moviePoster.getContext(), MovieDetailsActivity.class);
                    intent.putExtra(MOVIE_EXTRA_KEY, model);
                    moviePoster.getContext().startActivity(intent);
                }
            });
        }

    }
}
