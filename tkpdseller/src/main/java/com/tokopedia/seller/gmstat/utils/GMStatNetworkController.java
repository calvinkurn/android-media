package com.tokopedia.seller.gmstat.utils;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.OldGMStatRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author normansyahputa on 7/19/17.
 * <p>https://phab.tokopedia.com/w/api/krabs/shopstatistic/</> for more detail
 */

public class GMStatNetworkController {

    public static final int MAXIMUM_CATEGORY = 1;
    private static final int INVALID_PARAM = -1;
    private static final String TAG = "GMStatNetworkController";
    private Gson gson;
    private GMStatRepository repository;

    public GMStatNetworkController(Gson gson, GMStatRepository repository) {
        this.gson = gson;
        this.repository = repository;
    }

    private void showTransactionEmptyView(OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {
        GetTransactionGraph body2 = gson.fromJson(readJson("get_transaction_graph_empty_state.json", assetManager), GetTransactionGraph.class);
        oldGMStatRepository.onSuccessTransactionGraph(repository.getTransactionGraph(body2));
    }

    private void showProductGraphEmpty(OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {
        GetProductGraph body = gson.fromJson(readJson("get_product_graph_empty_state.json", assetManager), GetProductGraph.class);
        oldGMStatRepository.onSuccessProductGraph(body);
    }

    public void fetchDataEmptyState(final OldGMStatRepository oldGMStatRepository, AssetManager assetManager) {

        showProductGraphEmpty(oldGMStatRepository, assetManager);

        showTransactionEmptyView(oldGMStatRepository, assetManager);

        GetPopularProduct body3 = gson.fromJson(readJson("popular_product_empty_state.json", assetManager), GetPopularProduct.class);
        oldGMStatRepository.onSuccessPopularProduct(body3);

        GetBuyerGraph body4 = gson.fromJson(readJson("buyer_graph_empty_state.json", assetManager), GetBuyerGraph.class);
        oldGMStatRepository.onSuccessBuyerGraph(body4);

        KeywordModel keywordModel = new KeywordModel();
        keywordModel.setShopCategory(gson.fromJson(readJson("shop_category_empty.json", assetManager), GetShopCategory.class));

        if (keywordModel.getShopCategory() == null || keywordModel.getShopCategory().getShopCategory() == null || keywordModel.getShopCategory().getShopCategory().isEmpty()) {
            oldGMStatRepository.onSuccessGetShopCategory(keywordModel.getShopCategory());
            return;
        }

        List<GetKeyword> getKeywords = new ArrayList<>();
        getKeywords.add(gson.fromJson(readJson("search_keyword.json", assetManager), GetKeyword.class));
        keywordModel.setKeywords(getKeywords);
        oldGMStatRepository.onSuccessGetKeyword(getKeywords);
    }

    private String readJson(String fileName, AssetManager assetManager) {
        BufferedReader reader = null;
        String total = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                total += mLine;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
            Log.i("MNORMANSYAH", total);
        }
        return total;
    }
}
