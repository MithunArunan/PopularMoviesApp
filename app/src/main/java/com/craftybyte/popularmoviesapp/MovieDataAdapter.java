package com.craftybyte.popularmoviesapp;

/**
 * Created by Mithun on 17-Apr-16.
 */
import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Mithun on 16-Apr-16.
 */
public class MovieDataAdapter extends ArrayAdapter<HashMap<String,String>> {

    private Context context;
    private List<HashMap<String,String>> mMovieDetailsList = null;
    static final String PARSE_RESULT = "results";
    static final String PARSE_POSTER_PATH = "poster_path";
    static final String PARSE_OVERVIEW = "overview";
    static final String PARSE_RELEASE_DATE = "release_date";
    static final String PARSE_TITLE = "title";
    static final String PARSE_VOTE_AVERAGE = "vote_average";
    static final String PARSE_BACKDROP_PATH = "backdrop_path";


    public MovieDataAdapter(Context context, int resource, List<HashMap<String,String>> mMovieDetailsList) {
        super(context,0,mMovieDetailsList);
        this.context = context;
        this.mMovieDetailsList = mMovieDetailsList;
    }

    @Override
    public int getCount() {
        return mMovieDetailsList.size();
    }

    @Override
    public HashMap<String, String> getItem(int position) {
        return mMovieDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        if(convertView == null)
        {
            LayoutInflater inflater =  ( LayoutInflater ) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_list_item,null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movieImageView1);
        TextView textView1 = (TextView) convertView.findViewById(R.id.movieTitle);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.movieRatingView);

        if(mMovieDetailsList != null || mMovieDetailsList.size() > 0) {
            String poster_path = mMovieDetailsList.get(position).get(MovieDataAdapter.PARSE_POSTER_PATH);
            String poster_base_path = "http://image.tmdb.org/t/p/w500/";
            String poster_complete_path = poster_base_path + poster_path;
            Log.d(getClass().toString(), "AdapterClass" + mMovieDetailsList.get(position).get(MovieDataAdapter.PARSE_TITLE));

            textView1.setText(mMovieDetailsList.get(position).get(MovieDataAdapter.PARSE_TITLE));
            ratingBar.setRating(Float.parseFloat(String.valueOf(mMovieDetailsList.get(position).get(MovieDataAdapter.PARSE_VOTE_AVERAGE))));
            Picasso.with(context).load(poster_complete_path).into(imageView);
        }
        else {
            Log.d(getClass().toString(),"mMovieDetailsList is empty or null");
            return null;
        }
        return convertView;
    }
}
