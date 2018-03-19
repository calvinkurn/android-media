package com.tokopedia.seller.product.edit.view.holder;

import android.view.View;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.utils.ScoringProductHelper;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;


/**
 * Created by zulfikarrahman on 4/12/17.
 */

public class ProductScoreViewHolder {

    public interface Listener {
        void onDetailProductScoringClicked();
        void updateProductScoring();
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

    public void renderData(ProductViewModel model, ValueIndicatorScoreModel valueIndicatorScoreModel) {
        if (model == null) {
            return;
        }
        valueIndicatorScoreModel.setHasVideo(CommonUtils.checkCollectionNotNull(model.getProductVideo()));
        valueIndicatorScoreModel.setLengthProductName(model.getProductName().length());
        valueIndicatorScoreModel.setVariantActive(model.hasVariant());
        valueIndicatorScoreModel.setImageCount(model.getImageCount());
        valueIndicatorScoreModel.setImageResolution(model.getMinimumImageResolution());
        valueIndicatorScoreModel.setFreeReturnStatus(model.isProductFreeReturn());
        valueIndicatorScoreModel.setStockStatus(
                (model.hasVariant() && model.getProductVariant().isLimitedStock()) || model.getProductStock() > 0);
        valueIndicatorScoreModel.setLengthDescProduct(model.getProductDescription().length());
        listener.updateProductScoring();
    }

    public void setValueProductScoreToView(DataScoringProductView dataScoringProductView) {
        valueScore.setText(dataScoringProductView.getTotalScoringProductView().getValueScoreProduct());
        valueScore.setTextColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), valueScore.getContext()));
        progressValueScore.setMax(dataScoringProductView.getTotalScoringProductView().getMaxScore());
        progressValueScore.setProgress(dataScoringProductView.getTotalScoringProductView().getCountScoreProduct());
        progressValueScore.setProgressColor(ScoringProductHelper.getColorOfScore(dataScoringProductView.getTotalScoringProductView().getColor(), progressValueScore.getContext()));
    }

}
