package com.willpasco.clickies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int id;
    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "vote_average")
    private final float voteAverage;
    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private final String title;
    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "poster_path")
    private final String posterPath;
    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    private final String overview;
    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "release_date")
    private final String releaseDate;
    @SerializedName("popularity")
    @Expose
    @ColumnInfo(name = "popularity")
    private final float popularity;

    public Movie(int id, float voteAverage, String title, String posterPath, String overview, String releaseDate, float popularity) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    private Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.voteAverage = parcel.readFloat();
        this.title = parcel.readString();
        this.posterPath = parcel.readString();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
        this.popularity = parcel.readFloat();
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

    public int getId() {
        return id;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeFloat(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeFloat(popularity);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", voteAverage=" + voteAverage +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity=" + popularity +
                '}';
    }
}
