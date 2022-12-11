package com.example.healthcare.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.healthcare.ConsultantActivity;
import com.example.healthcare.MainActivity;
import com.example.healthcare.NewsActivity;
import com.example.healthcare.ProfileActivity;
import com.example.healthcare.R;
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

public class MovieActivity extends AppCompatActivity {

  public static final String ITEM_EXTRA = "item_extra";
  private DrawerLayout drawer;
  private ActionBarDrawerToggle toggle;
  RecyclerView rvMovie;
  MovieAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie);
    Toolbar toolbar = findViewById(R.id.toolbar);
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
            Intent intent = new Intent(MovieActivity.this, MainActivity.class);
            startActivity(intent);
            break;
          case R.id.nav_news:
            Intent intent1 = new Intent(MovieActivity.this, NewsActivity.class);
            startActivity(intent1);
            break;
          case R.id.nav_consult:
            Intent intent2 = new Intent(MovieActivity.this, ConsultantActivity.class);
            startActivity(intent2);
            break;
          case R.id.nav_profile:
            Intent intent3 = new Intent(MovieActivity.this, ProfileActivity.class);
            startActivity(intent3);
            break;
          case R.id.nav_sql:
            Intent intent4 = new Intent(MovieActivity.this, DisplayData.class);
            startActivity(intent4);
            break;
          case R.id.nav_movie:
              Intent intent5 = new Intent(MovieActivity.this, MovieActivity.class);
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
    Uri uri = Uri.parse("https://api.themoviedb.org/3/discover/movie?api_key=cda5e6184f2f4368e0cb688a4e08fb55")
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
      JSONArray jsonArray = jsonObject.getJSONArray("results");
      List<MovieModel> movieModels = new ArrayList<>();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject object = jsonArray.getJSONObject(i);
        String id = object.getString("id");
        String title = object.getString("title");
        String poster = object.getString("poster_path");
        String release = object.getString("release_date");
        String vote = object.getString("vote_average");
        movieModels.add(new MovieModel(id, title, poster, release, vote));
      }
      rvMovie = findViewById(R.id.rv_movie);
      adapter = new MovieAdapter(movieModels);
      rvMovie.setLayoutManager(new GridLayoutManager(MovieActivity.this, 2));
      rvMovie.setAdapter(adapter);

      adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
        @Override
        public void onItemClicked(MovieModel data) {
          Intent moveIntent = new Intent(MovieActivity.this, MovieDetailActivity.class);
          moveIntent.putExtra(MovieDetailActivity.ITEM_EXTRA, data);
          startActivity(moveIntent);
        }
      });
    }
  }
}