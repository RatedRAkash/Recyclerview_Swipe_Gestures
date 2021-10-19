package com.example.recyclerview_swipe_gestures;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements Filterable {

    private static final String TAG = "RecyclerAdapter";
    int count = 0;

    //Class er instanceVariables BEGIN
    List<String> moviesList;
    List<String> moviesListAll; //Filter kora chara Movie er list
    private RecyclerViewClickInterface recyclerViewClickInterface;
    //Class er instanceVariables END


    public RecyclerAdapter(List<String> moviesList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.moviesList = moviesList;
        this.moviesListAll = new ArrayList<>(moviesList); //"new" operator nah diya emni this.moviesListAll=moviesList dile... khali REFERENCE pass huito fole... moviesList change korle pore moviesListAll oo change hoya jabe... tai NEW operator use kore amra complete NEW Object create korlam
        System.out.println(moviesListAll);
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(TAG, "onCreateViewHolder: " + count++);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textView.setText(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    //FILTER er Method
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //"performFiltering" Method BACKGROUD Thread ee kaaj kore
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty())
            {
                filteredList.addAll(moviesListAll);
            }
            else
            {
                for(String movie: moviesListAll)
                {
                    if(movie.toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //"publishResults" Method UI Thread ee kaaj kore
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            moviesList.clear();
            moviesList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };
    //FILTER Method END



    //MYVIEWHOLDER CLASS BEGIN
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, rowCountTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

//                    moviesList.remove(getAdapterPosition()); // Amader created LIST theke Movie ta delete korlam
//                    notifyItemRemoved(getAdapterPosition()); //Eita mane holo Recycler View er theke ROW ta delete kore dilam
                    recyclerViewClickInterface.onLongItemClick(getAdapterPosition());

                    return true; //Item ta nidristio POSITION ee tai "True" return kortase
                }
            });

        }


    }
    //MYVIEWHOLDER CLASS END


}
