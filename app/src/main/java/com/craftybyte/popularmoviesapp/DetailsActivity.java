package com.craftybyte.popularmoviesapp;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private static HashMap<String,String> mHashMap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //To set up action bar using android support library!
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        mHashMap = (HashMap) intent.getSerializableExtra("MovieDetailData");
        Log.d(getLocalClassName(),"Hash Values "+mHashMap.get(MovieDataAdapter.PARSE_TITLE));

        TextView titleView = (TextView)findViewById(R.id.movieDetailTitleId);
        titleView.setText(mHashMap.get(MovieDataAdapter.PARSE_TITLE));

        ImageView imageView = (ImageView) findViewById(R.id.movieDetailImageId);
        String poster_path = mHashMap.get(MovieDataAdapter.PARSE_POSTER_PATH);
        String poster_base_path = "http://image.tmdb.org/t/p/w500/";
        String poster_complete_path = poster_base_path + poster_path;
        Picasso.with(this).load(poster_complete_path).into(imageView);

        //Release Date
        String releaseDate = mHashMap.get(MovieDataAdapter.PARSE_RELEASE_DATE);
        TextView releaseView =(TextView) findViewById(R.id.movieDetailReleaseId);
        releaseView.setText("Release Date: "+releaseDate);

        TextView synopsisView = (TextView) findViewById(R.id.movieDetailSynopsisId);
        synopsisView.setText(mHashMap.get(MovieDataAdapter.PARSE_OVERVIEW));

        RatingBar movieDetailRatingBar = (RatingBar) findViewById(R.id.movieDetailRatingId);
        if (movieDetailRatingBar != null) {
            movieDetailRatingBar.setRating(Float.valueOf(mHashMap.get(MovieDataAdapter.PARSE_VOTE_AVERAGE))/2);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_settings: //Action for setting button.
                //Open the settings activity!
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
