package com.willpasco.clickies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder> {


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position)
    {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.moview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position)
    {
        holder.onBind();
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        MovieViewHolder(@NonNull View itemView)
        {
            super(itemView);
        }

        void onBind(){

        }

    }
}
