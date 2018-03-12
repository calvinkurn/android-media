package com.tokopedia.discovery.imagesearch.domain.usecase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class SearchImageAsyncTask extends AsyncTask<byte[], Void, NewImageSearchResponse> {

    private Context context;
//    private TkpdProgressDialog tkpdProgressDialog;

    public SearchImageAsyncTask(Context context) {
        this.context = context;
//        tkpdProgressDialog = new TkpdProgressDialog(context, 1);
    }


    @Override
    protected NewImageSearchResponse doInBackground(byte[]... params) {
        try {
            // set aliyun accessKeyId and secret
            /*IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", "LTAIgYEAAiMej0WK",
                    "unN3GIXParljB7J7rPrxD3I47NHhtY");
            // add endpoint, no need to modify
            DefaultProfile.addEndpoint("ap-southeast-1", "ap-southeast-1",
                    "IDST", "idst.ap-southeast-1.aliyuncs.com");

            DefaultAcsClient client = new DefaultAcsClient(profile);*/

            IClientProfile profile = DefaultProfile.getProfile("ap-southeast-1", "LTAIUeEWSvia1KkW",
                    "eJLV3PJCCEn7sqf5vVrIzaESTfsNdm");
            // add endpoint, no need to modify
            DefaultProfile.addEndpoint("ap-southeast-1", "ap-southeast-1",
                    "ImageSearch", "imagesearch.ap-southeast-1.aliyuncs.com");

            IAcsClient client = new DefaultAcsClient(profile);

//            RoaSearchRequest request = new RoaSearchRequest();
//            request.setS(0);
//            request.setN(30);
//            request.setInstanceName("productsearch01");

//            request.setHttpContent(Base64.encode(params[0], Base64.DEFAULT), null, FormatType.RAW);

            SearchItemRequestLocal request = new SearchItemRequestLocal();
            request.setNum(10);
            request.setStart(0);
            request.setCatId("0");
            request.setInstanceName("productsearch01");
            request.setSearchPicture(params[0]);

            /*String encodedString = new String(Base64.encodeBase64(params[0]));
            String safeString = encodedString.replace('+', '-').replace('/', '_');*/

            /*request.setHttpContent(Base64.encode(params[0], Base64.DEFAULT), "UTF-8", FormatType.RAW);
            request.setAcceptFormat(FormatType.JSON);*/

            if (!request.buildPostContent()) {
                System.out.println("build post content failed.");
                return new NewImageSearchResponse();
            }

            NewImageSearchResponse response = client.getAcsResponse(request);



//            HttpResponse resp = client.doAction(request);
//            System.out.println(resp.getUrl());
//            String cont = new String(resp.getHttpContent());
//            Log.e("ImageSearch Res:", cont);
//            System.out.println(cont);
//            Gson gson = new Gson();
//            Type type = new TypeToken<NewImageSearchResponse>() {
//            }.getType();
//
//            return gson.fromJson(cont, type);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(NewImageSearchResponse result) {
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

            ((DiscoveryActivity) context).onHandleImageSearchResponse(result);
            /*Intent intent = new Intent(context, ImageSearchResultActivity.class);
            intent.putExtra("Response",result);
            context.startActivity(intent);
            tkpdProgressDialog.dismiss();*/
        }
    }

    private static String buildContent(Map<String, String> kv) {
        String meta = "";
        String body = "";
        int start = 0;

        String value;
        for (Iterator i$ = kv.entrySet().iterator(); i$.hasNext(); start += value.length()) {
            Map.Entry<String, String> entry = (Map.Entry) i$.next();
            value = (String) entry.getValue();
            if (meta.length() > 0) {
                meta = meta + "#";
            }

            meta = meta + (String) entry.getKey() + "," + start + "," + (start + value.length());
            body = body + value;
        }

        return meta + "^" + body;
    }

    @Override
    protected void onPreExecute() {
//        tkpdProgressDialog.showDialog();
        /*progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        infoView.setVisibility(View.VISIBLE);*/
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
