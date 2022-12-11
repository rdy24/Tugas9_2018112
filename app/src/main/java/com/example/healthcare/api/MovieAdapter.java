package com.example.healthcare.api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.healthcare.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

  List<MovieModel> movieList;
  OnItemClickCallback onItemClickCallback;

  public MovieAdapter(List<MovieModel> movieList) {
    this.movieList = movieList;
  }

  public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
    this.onItemClickCallback = onItemClickCallback;
  }

  @NonNull
  @Override
  public MovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View movieView = layoutInflater.inflate(R.layout.item_movie,parent,false);
    MyViewHolder viewHolder = new MyViewHolder((ViewGroup) movieView);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull MovieAdapter.MyViewHolder holder, int position) {
    MovieModel movie = movieList.get(position);
    holder.title.setText(movie.getTitle());
    holder.rating.setText(movie.getVote_average());
    holder.releaseDate.setText(movie.getRelease_date());
    Glide.with(holder.itemView.getContext())
            .load("https://image.tmdb.org/t/p/original/" + movie.getPoster_path())
            .apply(new RequestOptions().override(200,300))
            .into(holder.poster);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onItemClickCallback.onItemClicked(movieList.get(holder.getAdapterPosition()));
      }
    });
  }

  @Override
  public int getItemCount() {
    return (movieList != null) ? movieList.size() : 0;
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, rating, releaseDate;
    public ImageView poster;
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.result_title);
      rating = itemView.findViewById(R.id.result_rating);
      releaseDate = itemView.findViewById(R.id.result_release);
      poster = itemView.findViewById(R.id.result_image);
    }
  }

  public static class OnItemClickCallback {
    public void onItemClicked(MovieModel movie) {
    }
  }
}
