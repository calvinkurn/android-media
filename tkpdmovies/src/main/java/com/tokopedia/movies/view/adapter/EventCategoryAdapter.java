package com.tokopedia.movies.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.moshi.Json;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.movies.R;
import com.tokopedia.movies.view.activity.EventDetailsActivity;
import com.tokopedia.movies.view.utils.CurrencyUtil;
import com.tokopedia.movies.view.utils.MovieInfoClass;
import com.tokopedia.movies.view.utils.Utils;
import com.tokopedia.movies.view.viewmodel.CategoryItemsViewModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ashwanityagi on 16/11/17.
 */

public class EventCategoryAdapter extends RecyclerView.Adapter<EventCategoryAdapter.ViewHolder> {

    private List<CategoryItemsViewModel> categoryItems;
    private Context context;

    public EventCategoryAdapter(Context context, List<CategoryItemsViewModel> categoryItems) {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieTitle;
        public TextView movieLanguage;
        public TextView movieDimenson;
        public ImageView movieImage;
        public TextView moviePrice;
        public LinearLayout movieGenreLayout;
        int index;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieLanguage = (TextView) itemView.findViewById(R.id.movie_language);
            movieDimenson = (TextView) itemView.findViewById(R.id.movie_dimension);
            movieImage = (ImageView) itemView.findViewById(R.id.img_movie);
            moviePrice = (TextView) itemView.findViewById(R.id.tv_movie_price);
            movieGenreLayout = (LinearLayout) itemView.findViewById(R.id.movie_genre_layout);


        }

        public void setIndex(int position) {
            this.index = position;
        }

        public int getIndex() {
            return this.index;
        }

    }

    @Override
    public int getItemCount() {
        if (categoryItems != null) {
            return categoryItems.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_event_category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.movieTitle.setText("" + categoryItems.get(position).getDisplayName());
        holder.moviePrice.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(categoryItems.get(position).getSalesPrice()));
        if (!Utils.isNullOrEmpty(categoryItems.get(position).getCustomText2())) {
            holder.movieLanguage.setText(Utils.parseCustomText3(categoryItems.get(position).getCustomText2()).getMovieLanguage());
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 0, 0, 0);
        List<TextView> list = addTextViews(Utils.getGenres(categoryItems.get(position).getGenre()));

        holder.movieGenreLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            holder.movieGenreLayout.addView(list.get(i), params);
        }
        holder.movieDimenson.setText(Utils.movieDimension(categoryItems.get(position).getDisplayName()));

        holder.setIndex(position);

        ImageHandler.loadImageThumbs(context, holder.movieImage, categoryItems.get(position).getThumbnailApp());

        CategoryItemViewListener listener = new CategoryItemViewListener(holder);

        holder.itemView.setOnClickListener(listener);
    }

    class CategoryItemViewListener implements View.OnClickListener {

        ViewHolder mViewHolder;

        public CategoryItemViewListener(ViewHolder holder) {
            this.mViewHolder = holder;
        }

        @Override
        public void onClick(View view) {
            Intent detailsIntent = new Intent(context, EventDetailsActivity.class);
            detailsIntent.putExtra("homedata", categoryItems.get(mViewHolder.getIndex()));
            context.startActivity(detailsIntent);
        }
    }

    public String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

//    private MovieInfoClass parseCustomText3(String parseJson) {
//        Log.d("Naveen","Json String  to be Parsed is " + parseJson);
//        String parsedJson  = parseJson.replaceAll("\\\\", "");
//        Log.d("Naveen","Parsed Json String is " + parsedJson);
//        Gson gson = new Gson();
//        MovieInfoClass movieInfo = gson.fromJson(parsedJson, MovieInfoClass.class);
//        return movieInfo;
//    }
//
//    private List<String> getGenres(String genre) {
//        List<String> genreList = new ArrayList<>();
//
//        String[] temp = genre.split("\\|");
//        for (int i=0; i<temp.length; i++) {
//            Log.d("Naveen", " temp String in list is " + temp[i]);
//            genreList.add(temp[i]);
//        }
//
//        return genreList;
//    }

    private List<TextView> addTextViews(List<String> genreList) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        List<TextView> textViewList = new ArrayList<>();
        for (int i = 0; i < genreList.size(); i++) {
            String s = genreList.get(i);
            TextView tv = new TextView(context);
            tv.setId(i);
            tv.setText(s);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
            tv.setPadding(8, 8, 8, 8);
            tv.setBackgroundResource(R.drawable.genre_textview_background);
            tv.setLayoutParams(params);
            textViewList.add(tv);
        }

        return textViewList;
    }
}
