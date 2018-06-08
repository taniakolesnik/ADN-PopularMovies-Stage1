package uk.co.taniakolesnik.adn_popularmovies_part_1;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.taniakolesnik.adn_popularmovies_part_1.Utils.MovieAsyncTaskLoader;
import uk.co.taniakolesnik.adn_popularmovies_part_1.Utils.MovieRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    //please insert your API key here
    private static final String API_KEY_VALUE="";

    private static final String API_AUTHORITY="api.themoviedb.org";

    private String preference;
    private static final int LOADER_ID = 1;
    private MovieRecyclerViewAdapter adapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (preference == null){
            preference = getString(R.string.linkPreference_popular);
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
        adapter = new MovieRecyclerViewAdapter(this, new ArrayList<Movie>());

        recyclerListView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.popular_menu_item:
                preference=getString(R.string.linkPreference_popular);
                setTitle(getString(R.string.popularMovies_pageName));
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
            case R.id.topRated_menu_item:
                preference=getString(R.string.linkPreference_top_rated);
                setTitle(getString(R.string.topRatedMovies_pageName));
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        String url = makeUrl(preference);
        Log.i(TAG, "onCreateLoader url :" + url);
        return new MovieAsyncTaskLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            adapter.updateAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }

    private String makeUrl(String preference) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(API_AUTHORITY)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(preference)
                .appendQueryParameter(getString(R.string.api_key), API_KEY_VALUE)
                .build();
        return builder.toString();
    }

}
