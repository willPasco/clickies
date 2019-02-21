package com.willpasco.clickies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("vote_average")
    @Expose
    private final float voteAverage;
    @SerializedName("title")
    @Expose
    private final String title;
    @SerializedName("poster_path")
    @Expose
    private final String posterPath;
    @SerializedName("overview")
    @Expose
    private final String overview;
    @SerializedName("release_date")
    @Expose
    private final String releaseDate;


    private Movie(Parcel parcel){
        this.voteAverage = parcel.readFloat();
        this.title = parcel.readString();
        this.posterPath = parcel.readString();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle()
    {
        return title;
    }


    public String getPosterPath()
    {
        return posterPath;
    }


    public String getOverview()
    {
        return overview;
    }


    public String getReleaseDate()
    {
        return releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }

    @Override
    public String toString() {
        return "Movie{" +
                ", voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
