package com.tokopedia.seller.product.view.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.utils.ScoringProductHelper;
import com.tokopedia.seller.product.view.fragment.ProductAddView;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;

/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreViewHolder {

    private TextView valueScore;
    private ImageView imageInfo;
    private RoundCornerProgressBar progressValueScore;

    private ProductAddView productAddView;

    private final Context context;

    public ProductScoreViewHolder(View view, final ProductAddView productAddView) {
        this.context = view.getContext();
        valueScore = (TextView) view.findViewById(R.id.text_score);
        imageInfo = (ImageView) view.findViewById(R.id.info_button);
        this.productAddView = productAddView;
        progressValueScore = (RoundCornerProgressBar) view.findViewById(R.id.progress_value_score);
        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productAddView.moveToProductScoringDetail();
            }
        });
    }

    public void setValueProductScoreToView(DataScoringProductView dataScoringProductView) {
        valueScore.setText(dataScoringProductView.getTotalScoringProductView().getValueScoreProduct());
        valueScore.setTextColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), context));
        progressValueScore.setMax(dataScoringProductView.getTotalScoringProductView().getMaxScore());
        progressValueScore.setProgress(dataScoringProductView.getTotalScoringProductView().getCountScoreProduct());
        progressValueScore.setProgressColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), context));
    }

}
