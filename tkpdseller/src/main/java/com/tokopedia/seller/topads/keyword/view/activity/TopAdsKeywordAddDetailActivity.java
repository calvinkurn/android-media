package com.tokopedia.seller.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.myproduct.fragment.ImageGalleryAlbumFragment;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.activity.ProductAddActivity;
import com.tokopedia.seller.product.view.fragment.ProductAddFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddDetailFragment;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordListFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordAddDetailActivity extends BaseActivity
    implements TopAdsKeywordAddDetailFragment.OnSaveKeywordListener{

    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_KEYWORD_NAME = "key_nm";
    public static final String EXTRA_SERVER_WORDS = "server_wrds";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    public static final String RESULT_WORDS = "rslt_wrds";

    private String groupName;
    private String keywordName;
    private ArrayList<String> serverWords = new ArrayList<>();
    private ArrayList<String> localWords = new ArrayList<>();

    public static void start(Activity activity, int requestCode,
                             String groupName,
                             String keywordName,
                             ArrayList<String> serverWords,
                             ArrayList<String> localWords) {
        Intent intent = createIntent(activity, groupName, keywordName, serverWords, localWords);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             String groupName,
                             String keywordName,
                             ArrayList<String> serverWords,
                             ArrayList<String> localWords) {
        Intent intent = createIntent(context, groupName, keywordName, serverWords, localWords);
        fragment.startActivityForResult(intent,requestCode);
    }

    private static Intent createIntent(Context context,
                                       String groupName,
                                       String keywordName,
                                       ArrayList<String> serverWords,
                                       ArrayList<String> localWords) {
        Intent intent = new Intent(context, TopAdsKeywordAddDetailFragment.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GROUP_NAME, groupName);
        bundle.putString(EXTRA_KEYWORD_NAME, keywordName);
        bundle.putStringArrayList(EXTRA_SERVER_WORDS, serverWords);
        bundle.putStringArrayList(EXTRA_LOCAL_WORDS, localWords);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        retrieveFromIntent();
        getSupportActionBar().setTitle(keywordName);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, TopAdsKeywordAddDetailFragment.newInstance(serverWords, localWords)
                    , TopAdsKeywordAddDetailFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    private void retrieveFromIntent (){
        Intent intent = getIntent();
        groupName = intent.getStringExtra(EXTRA_GROUP_NAME);
        keywordName = intent.getStringExtra(EXTRA_KEYWORD_NAME);
        serverWords = intent.getStringArrayListExtra(EXTRA_SERVER_WORDS);
        localWords = intent.getStringArrayListExtra(EXTRA_LOCAL_WORDS);
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void onSaveClicked(ArrayList<String> keyWordsList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RESULT_WORDS, keyWordsList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
