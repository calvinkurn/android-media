package com.tokopedia.seller.topads.keyword.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.Window;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordAddDetailFragment;

import java.util.ArrayList;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordAddDetailActivity extends BaseActivity
        implements TopAdsKeywordAddDetailFragment.OnSaveKeywordListener {

    public static final String EXTRA_GROUP_NAME = "grp_nm";
    public static final String EXTRA_KEYWORD_NAME = "key_nm";
    public static final String EXTRA_SERVER_COUNT = "server_count";
    public static final String EXTRA_MAX_WORDS = "max_words";
    public static final String EXTRA_LOCAL_WORDS = "lcl_wrds";

    public static final String RESULT_WORDS = "rslt_wrds";
    public static final int DEFAULT_MAX_WORDS = 50;

    private String groupName;
    private String keywordName;
    private int serverCount;
    private int maxWords;
    private ArrayList<String> localWords = new ArrayList<>();

    public static void start(Activity activity, int requestCode,
                             String groupName,
                             String keywordName,
                             int serverCount, int maxWords,
                             ArrayList<String> localWords) {
        Intent intent = createIntent(activity, groupName, keywordName, serverCount, maxWords, localWords);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Context context, int requestCode,
                             String groupName,
                             String keywordName,
                             int serverCount, int maxWords,
                             ArrayList<String> localWords) {
        Intent intent = createIntent(context, groupName, keywordName, serverCount, maxWords, localWords);
        fragment.startActivityForResult(intent, requestCode);
    }

    private static Intent createIntent(Context context,
                                       String groupName,
                                       String keywordName,
                                       int serverCount,
                                       int maxWords,
                                       ArrayList<String> localWords) {
        Intent intent = new Intent(context, TopAdsKeywordAddDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GROUP_NAME, groupName);
        bundle.putString(EXTRA_KEYWORD_NAME, keywordName);
        bundle.putInt(EXTRA_SERVER_COUNT, serverCount);
        bundle.putInt(EXTRA_MAX_WORDS, maxWords);
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
            fragmentTransaction.replace(R.id.container,
                    TopAdsKeywordAddDetailFragment.newInstance(serverCount, maxWords, localWords)
                    , TopAdsKeywordAddDetailFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    private void retrieveFromIntent() {
        Intent intent = getIntent();
        groupName = intent.getStringExtra(EXTRA_GROUP_NAME);
        keywordName = intent.getStringExtra(EXTRA_KEYWORD_NAME);
        serverCount = intent.getIntExtra(EXTRA_SERVER_COUNT, 0);
        maxWords = intent.getIntExtra(EXTRA_MAX_WORDS, DEFAULT_MAX_WORDS);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        // check if user already make a changes by adding or deleting
        // if so, when backpressed, show dialog if user really want to delete the change or save the changes
        TopAdsKeywordAddDetailFragment fragment = (TopAdsKeywordAddDetailFragment) getSupportFragmentManager().findFragmentByTag(TopAdsKeywordAddDetailFragment.TAG);
        if (fragment!= null && fragment.isButtonSaveEnabled()) {
            AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.dialog_cancel_title))
                    .setMessage(getString(R.string.dialog_cancel_message))
                    .setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TopAdsKeywordAddDetailActivity.super.onBackPressed();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
