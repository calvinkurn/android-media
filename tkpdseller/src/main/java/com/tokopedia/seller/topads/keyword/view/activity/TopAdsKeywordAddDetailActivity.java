package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddDetailFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordListFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordAddDetailActivity extends BaseActivity {

    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_KEYWORD_NAME = "key_nm";
    public static final String EXTRA_SERVER_WORDS = "server_wrds";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    private String groupName;
    private String keywordName;
    private ArrayList<String> serverWords = new ArrayList<>();
    private ArrayList<String> localWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            retrieveFromIntent();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, TopAdsKeywordAddDetailFragment.newInstance(groupName,keywordName, serverWords, localWords)
                    , TopAdsKeywordAddDetailFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    private void retrieveFromIntent (){
        Intent intent = getIntent();
        if (intent == null){
            // TODO remove this, just for test
            groupName = "Group Test";
            keywordName = "Broad Match";
            ArrayList<String> serverArrayList = new ArrayList<>();
            serverArrayList.add("Satu");
            serverArrayList.add("Dua");
            serverArrayList.add("Tiga");
            serverArrayList.add("Empat");
            serverArrayList.add("Lima");
            serverWords = serverArrayList;
            ArrayList<String> localArrayList = new ArrayList<>();
            serverArrayList.add("Enam");
            serverArrayList.add("Tujuh");
            serverArrayList.add("Delapan");
            serverArrayList.add("Sembilan");
            serverArrayList.add("Sepuluh");
            serverArrayList.add("Sebelas");
            localWords = localArrayList;
        } else {
            groupName = intent.getStringExtra(EXTRA_GROUP_NAME);
            keywordName = intent.getStringExtra(EXTRA_KEYWORD_NAME);
            serverWords = intent.getStringArrayListExtra(EXTRA_SERVER_WORDS);
            localWords = intent.getStringArrayListExtra(EXTRA_LOCAL_WORDS);
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
