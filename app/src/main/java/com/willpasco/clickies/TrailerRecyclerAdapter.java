package com.willpasco.clickies;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.willpasco.clickies.model.Trailer;
import com.willpasco.clickies.util.ImageLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerViewHolder> {

    public static final String BASE_IMAGE_PATH = "http://i3.ytimg.com/vi/id/hqdefault.jpg";
    private List<Trailer> trailerList;

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.onBind(trailerList.get(position));
    }

    @Override
    public int getItemCount() {
        if (trailerList != null) {
            if (trailerList.size() > 3) {
                return 3;
            } else {
                return trailerList.size();
            }
        } else {
            return 0;
        }
    }

    public void addAll(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        private static final String YOUTUBE_SITE = "YouTube";
        private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
        private ImageView previewTrailer;
        private TextView trailerTitle;
        private View view;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.previewTrailer = itemView.findViewById(R.id.image_view_preview_trailer);
            this.trailerTitle = itemView.findViewById(R.id.text_view_trailer_title);
            this.view = itemView;
        }

        void onBind(final Trailer model) {
            trailerTitle.setText(model.getName());
            if (model.getSite().equals(YOUTUBE_SITE)) {

                String url = BASE_IMAGE_PATH.replace("id", model.getKey());
                ImageLoader.loadImageCenterCrop(url, previewTrailer);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_URL + model.getKey()));
                        view.getContext().startActivity(browserIntent);
                    }
                });
            }
        }

    }
}
