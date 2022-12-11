package com.example.healthcare.api;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable {
  private String id;
  private String title;
  private String overview;
  private String poster_path;
  private String release_date;
  private String vote_average;
  private String backdrop_path;
  private String tagline;

  public MovieModel(String id, String title, String overview, String release_date, String vote_average, String backdrop_path, String tagline) {
    this.id = id;
    this.title = title;
    this.overview = overview;
    this.release_date = release_date;
    this.vote_average = vote_average;
    this.backdrop_path = backdrop_path;
    this.tagline = tagline;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MovieModel(String id, String title, String poster_path, String release_date, String vote_average) {
    this.id = id;
    this.title = title;
    this.poster_path = poster_path;
    this.release_date = release_date;
    this.vote_average = vote_average;
  }

  protected MovieModel(Parcel in) {
    id = in.readString();
    title = in.readString();
    overview = in.readString();
    release_date = in.readString();
    vote_average = in.readString();
    poster_path = in.readString();
    backdrop_path = in.readString();
    tagline = in.readString();

  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getPoster_path() {
    return poster_path;
  }

  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  public String getRelease_date() {
    return release_date;
  }

  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  public String getVote_average() {
    return vote_average;
  }

  public void setVote_average(String vote_average) {
    this.vote_average = vote_average;
  }

  public String getBackdrop_path() {
    return backdrop_path;
  }

  public void setBackdrop_path(String backdrop_path) {
    this.backdrop_path = backdrop_path;
  }

  public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
    @Override
    public MovieModel createFromParcel(Parcel in) {
      return new MovieModel(in);
    }

    @Override
    public MovieModel[] newArray(int size) {
      return new MovieModel[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(id);
    parcel.writeString(title);
    parcel.writeString(overview);
    parcel.writeString(poster_path);
    parcel.writeString(release_date);
    parcel.writeString(vote_average);
    parcel.writeString(backdrop_path);
    parcel.writeString(tagline);
  }
}
