package com.craftybyte.popularmoviesapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> mMovieDetailsFinalList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main );

        duplicateData();
        MoviesFetchTask moviesFetchTask = new MoviesFetchTask(this);
        moviesFetchTask.execute();

        MovieDataAdapter movieDataAdapter = new MovieDataAdapter(this,0,  mMovieDetailsFinalList);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(movieDataAdapter);
    }

    private void duplicateData()
    {
        mMovieDetailsFinalList = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<5;i++)
        {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("overview",i+"th overview");
            hashMap.put("backdrop_path","/6bCplVkhowCjTHXWv49UjRPn0eK.jpg");
            hashMap.put("poster_path","/6bCplVkhowCjTHXWv49UjRPn0eK.jpg");
            hashMap.put("vote_average","5");
            hashMap.put("title",i+"th Title");
            hashMap.put("release_date","12/14/12");
            mMovieDetailsFinalList.add(hashMap);

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        MoviesFetchTask moviesFetchTask = new MoviesFetchTask(this);
        moviesFetchTask.execute();
    }

    class MoviesFetchTask extends AsyncTask<Void,Void,List<HashMap<String,String>>> {

        private GridView gridView = null;
        private Context context = null ;
        private List mMovieDetailsList = null;
        private final String LOG_TAG = MoviesFetchTask.class.getSimpleName() ;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        private InputStream inputStream =null;
        static final String PARSE_RESULT = "results";
        static final String PARSE_POSTER_PATH = "poster_path";
        static final String PARSE_OVERVIEW = "overview";
        static final String PARSE_RELEASE_DATE = "release_date";
        static final String PARSE_TITLE = "title";
        static final String PARSE_VOTE_AVERAGE = "vote_average";
        static final String PARSE_BACKDROP_PATH = "backdrop_path";

        public MoviesFetchTask(Context c)
        {
            context = c;
        }

        //This helper method is used to initialize the data before executing the work in background thread...
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMovieDetailsList = new ArrayList<HashMap<String,String>>();
            mMovieDetailsFinalList = new ArrayList<HashMap<String,String>>();
        }

        //To get the JSON data from server.
        private String getJSONData(URL movieDiscoverUrl ) throws IOException {

            String mMovieJsonDetail;

            Log.d(LOG_TAG,"Trying to connect to "+movieDiscoverUrl.toString());

            urlConnection = (HttpURLConnection) movieDiscoverUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                mMovieJsonDetail = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                mMovieJsonDetail = null;
            }
            mMovieJsonDetail = buffer.toString();
            Log.v(LOG_TAG+" Data","Here it is "+ mMovieJsonDetail);

            return mMovieJsonDetail;
        }


        //Makes a HTTP request and get the movies list data from themoviesdb.org...
        @Override
        protected List<HashMap<String, String>> doInBackground(Void... voids) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            //HTTP request to fetch JSON data as String
            //Parameters URL and then
            final String BASE_URI_MOVIES = "https://api.themoviedb.org/3/discover/movie?";
            String movieJsonDetail = null;

            //TODO:INSERT THE API KEY HERE
            final String API_KEY ="";

            Uri.Builder movieDicoverUri = Uri.parse(BASE_URI_MOVIES)
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY);
            try {
                movieJsonDetail = getJSONData(new URL(movieDicoverUri.toString()));
                Log.d(LOG_TAG,"JSON DATA IS"+movieJsonDetail);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG+ "Error", "Error closing stream", e);
                    }
                }

                try {
                    mMovieDetailsList = getMovieDataFromJSON(movieJsonDetail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mMovieDetailsList;
            }
        }

        private List<HashMap<String,String>> getMovieDataFromJSON(String movieJsonDetail) throws JSONException {
            List<HashMap<String,String>> movieDetailsList = new ArrayList<>();

            //Parse the JSON data appropriately.
            if(movieJsonDetail == null)
                return null;
            else
            {
                JSONObject jsonObject = new JSONObject(movieJsonDetail);
                JSONArray jsonResultsArray = jsonObject.getJSONArray(PARSE_RESULT);
                //TODO: add extra details if needed.
                //PARSE the movie details seperately.
                for(int loop=0; loop < jsonResultsArray.length() ; loop++)
                {
                    HashMap<String, String> hashMap = new HashMap<>();
                    JSONObject json = jsonResultsArray.getJSONObject(loop);
                    hashMap.put(PARSE_OVERVIEW,json.getString(PARSE_OVERVIEW));
                    hashMap.put(PARSE_BACKDROP_PATH,json.getString(PARSE_BACKDROP_PATH));
                    hashMap.put(PARSE_POSTER_PATH,json.getString(PARSE_POSTER_PATH));
                    hashMap.put(PARSE_RELEASE_DATE,json.getString(PARSE_RELEASE_DATE));
                    hashMap.put(PARSE_TITLE,json.getString(PARSE_TITLE));
                    Log.d(LOG_TAG,"TITLE "+json.getString(PARSE_TITLE));
                    hashMap.put(PARSE_VOTE_AVERAGE,json.getString(PARSE_VOTE_AVERAGE));
                    movieDetailsList.add(hashMap);
                }
            }
            return movieDetailsList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();
            super.onPostExecute(hashMaps);
            if(hashMaps == null || hashMaps.size() == 0)
            {
                Log.d(LOG_TAG,"List is empty | onPostExecute");
                //Nothing to display.
            }
            else
            {
                Log.d(LOG_TAG,"List not empty | onPostExecute");
                if(mMovieDetailsFinalList != null || mMovieDetailsFinalList.size() > 0)
                    mMovieDetailsFinalList.clear();
                mMovieDetailsFinalList.addAll(hashMaps);
                Log.d(LOG_TAG,"Done with | onPostExecute | Copied to mMovieDetailsFinalList ");
                Log.d(LOG_TAG,"Value is Title of 1: "+mMovieDetailsFinalList.get(0).get(PARSE_TITLE).toString());
            }
        }
    }


}
