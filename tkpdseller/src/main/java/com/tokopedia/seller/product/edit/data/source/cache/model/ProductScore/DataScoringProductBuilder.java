
package com.tokopedia.seller.product.edit.data.source.cache.model.ProductScore;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;

public class DataScoringProductBuilder {
    private Context context;
    private Gson gson;

    public DataScoringProductBuilder (@ApplicationContext Context context, Gson gson){
        this.context = context;
        this.gson = gson;
    }

    public DataScoringProduct build(){
        String jsonIndicatorScoreProduct = loadJSONFromAsset();
        return gson.fromJson(jsonIndicatorScoreProduct, JsonScoringProduct.class).getDataScoringProduct();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("indicator_score_product.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
