package com.willpasco.clickies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devs.readmoreoption.ReadMoreOption;
import com.willpasco.clickies.R;
import com.willpasco.clickies.model.Review;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewViewHolder> {

    private List<Review> reviewList;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.onBind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        if (reviewList != null) {
            return reviewList.size();
        } else {
            return 0;
        }
    }

    public void addAll(List<Review> reviewList) {
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuthor, textViewDescription;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.text_view_review_author);
            textViewDescription = itemView.findViewById(R.id.text_view_review_description);
        }

        void onBind(final Review model) {

            Context context = textViewAuthor.getContext();

            ReadMoreOption readMoreOption = new ReadMoreOption.Builder(context)
                    .textLength(3, ReadMoreOption.TYPE_LINE)
                    .moreLabel(context.getString(R.string.more_text_option))
                    .lessLabel(context.getString(R.string.less_text_option))
                    .moreLabelColor(context.getResources().getColor(R.color.color_progress))
                    .lessLabelColor(context.getResources().getColor(R.color.color_progress))
                    .labelUnderLine(true)
                    .expandAnimation(true)
                    .build();

            readMoreOption.addReadMoreTo(textViewDescription, model.getContent());

            textViewAuthor.setText(model.getAuthor());
//            textViewDescription.setText(model.getContent());
        }

    }
}
