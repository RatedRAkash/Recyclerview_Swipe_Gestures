package com.example.recyclerview_swipe_gestures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    List<String> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        moviesList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(moviesList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration); //Item gular majhe Divider anar jonno

        moviesList.add("Iron Man");
        moviesList.add("Incredible Hulk");
        moviesList.add("Iron Man 2");
        moviesList.add("Thor");
        moviesList.add("Captain America");
        moviesList.add("Avenger");
        moviesList.add("Batman");
        moviesList.add("Joker");
        moviesList.add("Spider Man");
        moviesList.add("Iron Man 3");
        moviesList.add("Avengers : Infinity War");
        moviesList.add("Dr. Strange");
        moviesList.add("Avengers : End Game");
        moviesList.add("Black Panther");
        moviesList.add("Vision");
        moviesList.add("What If...");

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                moviesList.add("Black Widow");
                moviesList.add("HawkEye");
                moviesList.add("Thor: Dark World");

                recyclerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false); // This End our Refreshing
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    //Menu Options BEGIN
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //Menu option END





    //SWIPE and DRAG & Drop code
    String deletedMovie = null;
    List<String> archivedMovies = new ArrayList<>();

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        //Drag and Re-order NAH korle first parameter "0" dibo

        //onMove--->Drag and Re-order korar jonno
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            //viewHolder---> jei Item ta amra Dhoira asi taar "viewHolder"
            //target---> jei Item er sathe amra SWAP kortasi sheitar "viewHolder"

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(moviesList,fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

            return false;
        }

        //--------------------------------------------------------------


        //onSwiped--->sudhu Item gula Swap Left and Right er jonno
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedMovie = moviesList.get(position);
                    moviesList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, deletedMovie + " is Removed", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    moviesList.add(position, deletedMovie);
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    String movieName=moviesList.get(position);
                    archivedMovies.add(movieName);
                    moviesList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, movieName + " is Archived", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    archivedMovies.remove(archivedMovies.lastIndexOf(movieName));
                                    moviesList.add(position, movieName);
                                    recyclerAdapter.notifyItemInserted(position);
                                }
                            }).show();

                    break;
            }


        }
    };
    //End SWIPE and Drag & Drop End


    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, moviesList.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(int position) {
        //Toast.makeText(this,moviesList.get(position),Toast.LENGTH_SHORT).show();
//        moviesList.remove(position);
//        recyclerAdapter.notifyItemRemoved(position);
    }
}