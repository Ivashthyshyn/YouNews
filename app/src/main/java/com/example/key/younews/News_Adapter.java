package com.example.key.younews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Key on 21.02.2017.
 */

public class News_Adapter extends ArrayAdapter<News> {
    public News_Adapter(Context context, List<News> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        View myView = convertView;
        if (myView==null){
            myView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }
        News bildNews = getItem(position);

        TextView timeText = (TextView)myView.findViewById(R.id.textView2);
        timeText.setText(bildNews.getData());

        TextView titleText = (TextView)myView.findViewById(R.id.textView);
        titleText.setText(bildNews.getTitle());

        TextView urlText = (TextView)myView.findViewById(R.id.textView3);
        urlText.setText(bildNews.getSection());



        return myView;
    }
}
