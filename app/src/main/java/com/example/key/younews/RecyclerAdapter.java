package com.example.key.younews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Key on 21.02.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<News> mNews;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView timeText;
        public TextView titleText;
        public TextView urlText;

        public ViewHolder(View itemView) {
           super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.textView2);
            titleText = (TextView)itemView.findViewById(R.id.textView);
            urlText = (TextView)itemView.findViewById(R.id.textView3);

        }


    }
    public RecyclerAdapter( List<News> list) {
       this.mNews = list;
    }

    // Часткоове рішення проблеми непрацює коректно
    // Потрібно переробити

    public List<News> swapList(List<News> list) {

        List<News> oldList = mNews;
        this.mNews = list;
        if (list != null) {
            this.notifyDataSetChanged();
        }
        return oldList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder mHolder = new ViewHolder(mView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {




            News myNews = mNews.get(position);

            holder.timeText.setText(myNews.getData());
            holder.titleText.setText(myNews.getTitle());
            holder.urlText.setText(myNews.getSection());

    }

    @Override
    public int getItemCount() {
        return  mNews.size();
    }
}
