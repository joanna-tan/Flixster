package com.example.flixster.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

        // the movie to display
        Movie movie;

        // the view objects
        TextView tvTitle;
        TextView tvOverview;
        TextView tvPopularity;
        RatingBar rbVoteAverage;
        ImageView thumbnail;

        public static final String TAG = "MovieDetailsActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_movie_details);
            // resolve the view objects
            tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvOverview = (TextView) findViewById(R.id.tvOverview);
            rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
            tvPopularity = (TextView) findViewById(R.id.tvPopularity);
            thumbnail = (ImageView) findViewById(R.id.thumbnail);

            // unwrap the movie passed in via intent, using its simple name as a key
            movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
            assert movie != null;
            Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

            // set the title and overview
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            tvPopularity.setText(String.format("Popularity: %s", movie.getPopularity().toString()));
            String imageUrl = movie.getBackdropPath();
            GlideApp.with(getApplicationContext()).load(imageUrl).transform(new RoundedCornersTransformation(30, 0)).placeholder(R.mipmap.backdrop_placeholder_foreground).into(thumbnail);

            //Glide.with(getApplicationContext()).load(imageUrl).placeholder(R.mipmap.backdrop_placeholder_foreground).into(thumbnail);


            // vote average is 0..10, convert to 0..5 by dividing by 2
            float voteAverage = movie.getVoteAverage().floatValue();
            rbVoteAverage.setRating(voteAverage > 0 ? (voteAverage / 2.0f) : voteAverage);
            //rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get("https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed",
                    new JsonHttpResponseHandler() {

                        //if GET call is successful
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess");
                            JSONObject jsonObject = json.jsonObject;

                            try {
                                final String youtubeKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");

                                thumbnail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //set intent to go to MovieTrailerActivity

                                        Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                        i.putExtra("youtubeKey", youtubeKey);
                                        startActivity(i);
                                    }
                                });
                                Log.d(TAG, youtubeKey);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "onFailure");
                        }
                    });
        }

}
