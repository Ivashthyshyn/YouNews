package com.example.key.younews;

import android.support.v7.widget.DialogTitle;

/**
 * Created by Key on 21.02.2017.
 */

public class News {
        private String myTitle;
        private String myData;
        private String myUrl;
        private String mySection;
    public News(String Title, String Data, String Url, String Section){
                myTitle = Title;
                myData = Data;
                myUrl  = Url;
                mySection = Section;
    }

    public String getTitle(){return myTitle;}
    public String getData(){return myData;}
    public String getUrl(){return myUrl;}
    public String getSection(){return mySection;}
}
