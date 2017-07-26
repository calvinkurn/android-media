package com.tokopedia.seller.goldmerchant.statistic.domain.interactor;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.CompositeUseCase;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetBuyerGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetKeyword;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetPopularProduct;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetProductGraph;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetShopCategory;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.model.graph.GetTransactionGraph;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.KeywordModel;
import com.tokopedia.seller.goldmerchant.statistic.domain.model.empty.GMEmptyModel;
import com.tokopedia.seller.goldmerchant.statistic.view.model.GMTransactionGraphMergeModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 7/26/17.
 */

public class GMStatEmptyUseCase extends CompositeUseCase<GMEmptyModel> {

    private AssetManager assetManager;
    private Gson gson;
    private GMStatRepository repository;

    @Inject
    public GMStatEmptyUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                              AssetManager assetManager, Gson gson, GMStatRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.assetManager = assetManager;
        this.gson = gson;
        this.repository = repository;
    }


    @Override
    public Observable<GMEmptyModel> createObservable(RequestParams requestParams) {
        GMEmptyModel gmEmptyModel = new GMEmptyModel();

        GMTransactionGraphMergeModel transactionGraph = repository.getTransactionGraph(gson.fromJson(readJson("get_transaction_graph_empty_state.json", assetManager), GetTransactionGraph.class));
        GetProductGraph getProductGraph = gson.fromJson(readJson("get_product_graph_empty_state.json", assetManager), GetProductGraph.class);
        GetPopularProduct getPopularProduct = gson.fromJson(readJson("popular_product_empty_state.json", assetManager), GetPopularProduct.class);
        GetBuyerGraph getBuyerGraph = gson.fromJson(readJson("buyer_graph_empty_state.json", assetManager), GetBuyerGraph.class);
        KeywordModel keywordModel = new KeywordModel();
        keywordModel.setShopCategory(gson.fromJson(readJson("shop_category_empty.json", assetManager), GetShopCategory.class));
        List<GetKeyword> getKeywords = new ArrayList<>();
        getKeywords.add(gson.fromJson(readJson("search_keyword.json", assetManager), GetKeyword.class));
        keywordModel.setKeywords(getKeywords);

        gmEmptyModel.productGraph = getProductGraph;
        gmEmptyModel.transactionGraph = transactionGraph;
        gmEmptyModel.popularProduct = getPopularProduct;
        gmEmptyModel.buyerGraph = getBuyerGraph;
        gmEmptyModel.keywordModel = keywordModel;

        return Observable.just(gmEmptyModel);
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
        }
        return total;
    }
}
