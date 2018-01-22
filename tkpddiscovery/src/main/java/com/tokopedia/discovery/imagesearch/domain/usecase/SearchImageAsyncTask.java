package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.network.entity.discovery.ImageSearchResponse;

import java.lang.reflect.Type;

import rx.Observable;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class SearchImageAsyncTask extends AsyncTask<byte[], Void, ImageSearchResponse> {

    private Context context;
    private TkpdProgressDialog tkpdProgressDialog;

    public SearchImageAsyncTask(Context context) {
        this.context = context;
        tkpdProgressDialog = new TkpdProgressDialog(context, 1);
    }


    @Override
    protected ImageSearchResponse doInBackground(byte[]... params) {
        try {
            // set aliyun accessKeyId and secret
            IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", "LTAIgYEAAiMej0WK",
                    "unN3GIXParljB7J7rPrxD3I47NHhtY");
            // add endpoint, no need to modify
            DefaultProfile.addEndpoint("ap-southeast-1", "ap-southeast-1",
                    "IDST", "idst.ap-southeast-1.aliyuncs.com");

            DefaultAcsClient client = new DefaultAcsClient(profile);

            RoaSearchRequest req = new RoaSearchRequest();
            req.setApp("oas_search");
            req.setS(0);
            req.setN(30);

            req.setHttpContent(Base64.encode(params[0], Base64.NO_CLOSE | Base64.NO_WRAP), null, FormatType.RAW);
            long begin = System.currentTimeMillis();

            HttpResponse resp = client.doAction(req);
            long end = System.currentTimeMillis();
            System.out.println("search time(ms):" + (end - begin));

            System.out.println(resp.getUrl());
            String cont = new String(resp.getHttpContent());

            System.out.println(cont);

            Gson gson = new Gson();
            Type type = new TypeToken<ImageSearchResponse>() {
            }.getType();
            ImageSearchResponse imageSearchResponse =  gson.fromJson(cont, type);

            return imageSearchResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ImageSearchResponse result) {
        /*progressBar.setVisibility(View.GONE);
        infoView.setVisibility(View.GONE);

        if (result != null) {
            Log.d("MainActivity", result.getOasSearch().getTrace().getSearch().getId());

            if (result.getOasSearch().getAuctions() != null && result.getOasSearch().getAuctions().size() > 0) {
                mAdapter = new AuctionsAdapter(MainActivity.this, result.getOasSearch().getAuctions());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                erroView.setVisibility(View.GONE);
            } else {
                erroView.setVisibility(View.VISIBLE);
                erroView.setText("No Results Found");
            }
        } else {
            erroView.setText("Error in searching");
            erroView.setVisibility(View.VISIBLE);
        }*/

        if (context != null) {

            Intent intent = new Intent(context, ImageSearchResultActivity.class);
            intent.putExtra("Response",result);
            context.startActivity(intent);
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    protected void onPreExecute() {
        tkpdProgressDialog.showDialog();
        /*progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        infoView.setVisibility(View.VISIBLE);*/
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
