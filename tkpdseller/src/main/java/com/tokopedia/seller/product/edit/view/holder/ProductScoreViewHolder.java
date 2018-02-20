package com.tokopedia.seller.product.edit.view.holder;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.utils.ScoringProductHelper;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreViewHolder extends ProductViewHolder {

    public interface Listener {
        void onDetailProductScoringClicked();
    }

    private TextView valueScore;
    private RoundCornerProgressBar progressValueScore;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ProductScoreViewHolder(View view, Listener listener) {
        View productScoringView = view.findViewById(R.id.relative_layout_product_scoring);
        valueScore = (TextView) view.findViewById(R.id.text_score);
        progressValueScore = (RoundCornerProgressBar) view.findViewById(R.id.progress_value_score);
        productScoringView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductScoreViewHolder.this.listener.onDetailProductScoringClicked();
            }
        });

        setListener(listener);
    }

    public void setValueProductScoreToView(DataScoringProductView dataScoringProductView) {
        valueScore.setText(dataScoringProductView.getTotalScoringProductView().getValueScoreProduct());
        valueScore.setTextColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), valueScore.getContext()));
        progressValueScore.setMax(dataScoringProductView.getTotalScoringProductView().getMaxScore());
        progressValueScore.setProgress(dataScoringProductView.getTotalScoringProductView().getCountScoreProduct());
        progressValueScore.setProgressColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), progressValueScore.getContext()));
    }

    @Override
    public Pair<Boolean, String> isDataValid() {
        return new Pair<>(true, "");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

    }
}
