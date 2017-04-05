package com.example.key.younews;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Key on 21.02.2017.
 *
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    /**
     * List for news data
     */
    private List<News> mNews;
    /**
     * use context to intent Url
     */
    public Context context;

    /**
     * ViewHolder instance for multiple display  timeText, titleText, mySection, and information myUrl
     * also containing listener events are pressing
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView timeText;
        public TextView titleText;
        public TextView mySection;
        public String myUrl;
        public IMyViewHolderClicks mListener;

        // constructor of ViewHolder
        public ViewHolder(View itemView, IMyViewHolderClicks listener) {
           super(itemView);
            // listener events
            mListener = listener;
            // initializes variables field
            timeText = (TextView) itemView.findViewById(R.id.textView2);
            titleText = (TextView)itemView.findViewById(R.id.textView);
            mySection = (TextView)itemView.findViewById(R.id.textView3);
            // String Url information
            myUrl = null;
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View v) {
            mListener.onPressed(myUrl);
            }

        /**
         *  interface to store and transfer data with events listener
         */

        public static interface IMyViewHolderClicks {
            public void onPressed(String url);

        }
    }

    /**
     * constructor of RecyclerAdapter
     * @param list  is loaded  a list of news using download manager
     */
    public RecyclerAdapter(List<News> list) {
       this.mNews = list;


    }

    /**
     * get news list to display
     * @param list is loaded via LoaderManager news list
     */

    public void swapList(List<News> list) {

        this.mNews = list;
        if (list != null) {
            this.notifyDataSetChanged();
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        context = mView.getContext();

        ViewHolder mHolder = new ViewHolder(mView, new ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onPressed(String url) {
                Log.i("RecyclerView", "You count url " + url);
                //
               Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
            context.startActivity(intent);
            }
        });

        return mHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            News myNews = mNews.get(position);
            String time = myNews.getData();
            String [] newsTime = time.split("T");

            holder.timeText.setText(newsTime[0]);
            holder.titleText.setText(myNews.getTitle());
            holder.mySection.setText(myNews.getSection());
            holder.myUrl = myNews.getUrl();
    }

    @Override
    public int getItemCount() {
        return  mNews.size();
    }


}
