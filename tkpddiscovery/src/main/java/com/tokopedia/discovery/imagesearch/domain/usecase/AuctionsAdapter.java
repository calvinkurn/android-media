package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.discovery.R;

import java.util.List;

/**
 * Created by sachinbansal on 1/10/18.
 */

class AuctionsAdapter extends RecyclerView.Adapter<AuctionsAdapter.MyViewHolder> {
    private List<Auction> auctionList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        private TextView productIdTextView, categoryIdTextView, finalScoreTextView;
        private View layout;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img_product);
            productIdTextView = (TextView) view.findViewById(R.id.tv_product_id);
            categoryIdTextView = (TextView) view.findViewById(R.id.tv_cat_id);
            finalScoreTextView = (TextView) view.findViewById(R.id.tv_score);
            layout = view;
        }
    }


    public AuctionsAdapter(Context context, List<Auction> moviesList) {
        this.auctionList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_auction, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Auction auction = auctionList.get(position);

        holder.productIdTextView.setText("Product Id = " + auction.getItemId());
        holder.categoryIdTextView.setText("Category Id = " + auction.getCatId());
        holder.finalScoreTextView.setText("Cust Content Score = " + auction.getCustContent());

        Glide.with(context)
                .load("http://img-poc.oss-ap-southeast-1.aliyuncs.com/" + auction.getPicName())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tokopedia://product/" + auction.getItemId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return auctionList.size();
    }
}
