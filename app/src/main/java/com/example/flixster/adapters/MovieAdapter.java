package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.R;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.GlideApp;
import com.example.flixster.models.Movie;
import com.example.flixster.models.MovieDetailsActivity;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context; //have a context from where the adapter is being constructed
    List<Movie> movies;
    ItemMovieBinding binding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

//     Involves inflating a layout from XML and returning the holder
//         only create as many views as are on the screen; don't need to call anymore once min views has been created
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }
    
    // Involves populating data into the item through the holder (from data at position)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the view holder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //hold data for view holder to display
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMovieBinding.bind(itemView);
            tvTitle = binding.tvTitle;
            tvOverview = binding.tvOverview;
            ivPoster = binding.ivPoster;
            // add an on click listener on the itemView
            itemView.setOnClickListener(this);
        }

        // Use getter methods on movie to populate a View
        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            //set imageURL to backdrop img if landscape
            //if phone in portrait, set imageURL to posterpath
            int radius = 20; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop


            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                Log.i("yeps", imageUrl);
                //Glide.with(context).load(imageUrl).placeholder(R.mipmap.backdrop_placeholder_foreground).into(ivPoster);
                GlideApp.with(context).load(imageUrl).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.mipmap.backdrop_placeholder_foreground).into(ivPoster);
            }
            else {
                imageUrl = movie.getPosterPath();
                GlideApp.with(context).load(imageUrl).transform(new RoundedCornersTransformation(10, 1)).placeholder(R.mipmap.backdrop_placeholder_foreground).into(ivPoster);
                //Glide.with(context).load(imageUrl).placeholder(R.mipmap.poster_placeholder_foreground).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(ivPoster);
            }
            //Glide.with(context).load(imageUrl).into(ivPoster);
        }

        @Override
        public void onClick(View view) {
            // gets the item position
            int position = getAdapterPosition();

            // make sure the position actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position (won't work if class is static)
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler and use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // finally, show the activity
                context.startActivity(intent);

            }
        }
    }
}
