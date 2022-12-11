package com.example.healthcare.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.healthcare.ConsultantActivity;
import com.example.healthcare.MainActivity;
import com.example.healthcare.NewsActivity;
import com.example.healthcare.ProfileActivity;
import com.example.healthcare.R;
import com.example.healthcare.databinding.ActivityMovieDetailBinding;
import com.example.healthcare.sqllite.DisplayData;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {
  public static final String ITEM_EXTRA = "item_extra";
  ActivityMovieDetailBinding binding;
  private DrawerLayout drawer;
  private ActionBarDrawerToggle toggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    super.onCreate(savedInstanceState);
    binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    drawer.addDrawerListener(toggle);
    toggle.setDrawerIndicatorEnabled(true);
    drawer.addDrawerListener(toggle);
    toggle.syncState();
    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setCheckedItem(R.id.nav_movie);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
          case R.id.nav_alarm:
            Intent intent = new Intent(MovieDetailActivity.this, MainActivity.class);
            startActivity(intent);
            break;
          case R.id.nav_news:
            Intent intent1 = new Intent(MovieDetailActivity.this, NewsActivity.class);
            startActivity(intent1);
            break;
          case R.id.nav_consult:
            Intent intent2 = new Intent(MovieDetailActivity.this, ConsultantActivity.class);
            startActivity(intent2);
            break;
          case R.id.nav_profile:
            Intent intent3 = new Intent(MovieDetailActivity.this, ProfileActivity.class);
            startActivity(intent3);
            break;
          case R.id.nav_sql:
            Intent intent4 = new Intent(MovieDetailActivity.this, DisplayData.class);
            startActivity(intent4);
            break;
          case R.id.nav_movie:
            Intent intent5 = new Intent(MovieDetailActivity.this, MovieActivity.class);
            startActivity(intent5);
            break;
        }
        return true;
      }
    });

    try {
      getData();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
  public void getData() throws MalformedURLException {
    Intent intent = getIntent();
    MovieModel movie = intent.getParcelableExtra(MovieActivity.ITEM_EXTRA);
    Uri uri = Uri.parse("https://api.themoviedb.org/3/movie/" + movie.getId() + "?api_key=cda5e6184f2f4368e0cb688a4e08fb55")
            .buildUpon().build();
    URL url = new URL(uri.toString());
    new DOTask().execute(url);
  }

  class DOTask extends AsyncTask<URL, Void, String> {
    //connection request
    @Override
    protected String doInBackground(URL... urls) {
      URL url = urls[0];
      String data = null;
      try {
        data = NetworkUtils.makeHTTPRequest(url);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return data;
    }

    @Override
    protected void onPostExecute(String s) {
      try {
        parseJson(s);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    //get data json
    public void parseJson(String data) throws JSONException {
      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject(data);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      String title = jsonObject.getString("title");
      String overview = jsonObject.getString("overview");
      String release_date = jsonObject.getString("release_date");
      String vote_average = jsonObject.getString("vote_average");
      String tagline = jsonObject.getString("tagline");
      String backdrop_path = jsonObject.getString("backdrop_path");

      //set data to view
      binding.title.setText(title);
      binding.overview.setText(overview);
      binding.releaseDate.setText(release_date);
      binding.rating.setText(vote_average);
      binding.tagline.setText(tagline);
      Glide.with(MovieDetailActivity.this).load("https://image.tmdb.org/t/p/w500" + backdrop_path).into(binding.backdrop);
    }
  }
}