package com.tokopedia.seller.product.edit.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.utils.ScoringProductHelper;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.IndicatorScoreView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public class IndicatorScoringAdapter extends RecyclerView.Adapter<IndicatorScoringAdapter.ViewHolder> {

    List<IndicatorScoreView> indicatorScoreViews;
    Context context;

    public IndicatorScoringAdapter(Context context) {
        this.context = context;
        indicatorScoreViews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_score_indicator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(indicatorScoreViews.get(position));
    }

    @Override
    public int getItemCount() {
        return indicatorScoreViews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleScoringIndicator;
        RoundCornerProgressBar progressScoring;
        LinearLayout containerDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            titleScoringIndicator = (TextView) itemView.findViewById(R.id.title_indicator_score);
            progressScoring = (RoundCornerProgressBar) itemView.findViewById(R.id.progress_indicator_score);
            containerDescription = (LinearLayout) itemView.findViewById(R.id.container_description);
        }

        public void bindView(IndicatorScoreView indicatorScoreView) {
            titleScoringIndicator.setText(indicatorScoreView.getNameIndicator());
            progressScoring.setProgress(indicatorScoreView.getScore());
            progressScoring.setMax(indicatorScoreView.getMaxScoreIndicator());
            progressScoring.setProgressColor(ScoringProductHelper.getColorOfScore(indicatorScoreView.getIndicatorColor(),
                    context));

            containerDescription.removeAllViews();
            for(String desc: indicatorScoreView.getIndicatorDescs()){
                View itemViewDesc = LayoutInflater.from(context).
                        inflate(R.layout.item_product_score_description_indicator, containerDescription, false);
                ImageView imageViewDesc = (ImageView) itemViewDesc.findViewById(R.id.image_desc_score);
                TextView descText = (TextView) itemViewDesc.findViewById(R.id.content_desc_product_score);

                if(indicatorScoreView.getIndicatorDescs().size() > 1){
                    imageViewDesc.setVisibility(View.VISIBLE);
                }else{
                    imageViewDesc.setVisibility(View.GONE);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    descText.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
                }else{
                    descText.setText(Html.fromHtml(desc));
                }

                containerDescription.addView(itemViewDesc);
            }
        }
    }

    public void addData(List<IndicatorScoreView> indicatorScoreViews){
        this.indicatorScoreViews.clear();
        this.indicatorScoreViews.addAll(indicatorScoreViews);
        notifyDataSetChanged();
    }
}
