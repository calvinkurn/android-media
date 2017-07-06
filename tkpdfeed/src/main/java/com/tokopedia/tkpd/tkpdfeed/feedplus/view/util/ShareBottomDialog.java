package com.tokopedia.tkpd.tkpdfeed.feedplus.view.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.ShareAdapter;
import com.tokopedia.core.inboxreputation.model.ShareItem;
import com.tokopedia.core.util.ClipboardHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.ShareFeedAdapter;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public class ShareBottomDialog {

    private static final int SNACKBAR_DURATION = 3500;
    private static final int SHARE_GOOGLE_REQUEST_CODE = 123;
    private static final String CHECK_NOW = " cek sekarang di :\n";
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final RecyclerView appGrid;
    private final CallbackManager callbackManager;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentV4;

    private ShareModel shareModel;
    private ShareFeedAdapter adapter;
    private ArrayList<ShareItem> list;

    public ShareBottomDialog(Activity activity,
                             CallbackManager callbackManager) {
        this.activity = activity;
        this.fragment = null;
        this.fragmentV4 = null;
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(activity);
        initAdapter();
        setListener();
        setShareList();
    }

    public ShareBottomDialog(Fragment fragment, CallbackManager callbackManager) {
        this.fragment = fragment;
        this.fragmentV4 = null;
        this.activity = fragment.getActivity();
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(fragment.getActivity());
        initAdapter();
        setListener();
        setShareList();
    }

    public ShareBottomDialog(android.support.v4.app.Fragment fragment, CallbackManager callbackManager) {
        this.fragment = null;
        this.fragmentV4 = fragment;
        this.activity = fragment.getActivity();
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_feed_dialog);
        appGrid = (RecyclerView) this.dialog.findViewById(R.id.grid);
        initVar(fragment.getActivity());
        initAdapter();
        setListener();
        setShareList();
    }

    private void initVar(Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        appGrid.setLayoutManager(layoutManager);

        list = new ArrayList<>();
    }

    public void setShareModel(ShareModel shareModel) {
        this.shareModel = shareModel;
    }

    private void initAdapter() {
        adapter = new ShareFeedAdapter();
        appGrid.setAdapter(adapter);
    }

    protected void setShareList() {

        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_g), "Google+", shareGoogle()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_fb), "Facebook", shareFb()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_line), "Line", shareLine()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_wa), "Whatsapp", shareWhatsapp()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_twitter), "Twitter", shareTwitter()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_sms), "SMS", shareSMS()));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_copy), "Copy Link", shareCopyLink()));
        adapter.setList(list);
    }

    private View.OnClickListener shareSMS() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareText = shareModel.getContentMessage() + " cek sekarang di :\n" + shareModel.getUrl();

                Intent smsIntent = MethodChecker.getSmsIntent(activity, shareText);
                activity.startActivity(smsIntent);

            }
        };
    }

    private View.OnClickListener shareTwitter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.twitter.android");

            }
        };
    }

    private View.OnClickListener shareWhatsapp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.whatsapp");
            }
        };
    }

    private void shareToApp(String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(appName);

        String shareText = shareModel.getContentMessage() + CHECK_NOW + shareModel.getUrl();

        if (shareModel.getUrl() != null
                && !shareModel.getUrl().equals(""))
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(activity,
                    activity.getString(R.string.error_apps_not_installed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener shareLine() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("jp.naver.line.android");

            }
        };
    }

    private View.OnClickListener shareGoogle() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusShare.Builder builder = new PlusShare.Builder(activity);

                builder.setType("text/plain");

                String shareText = shareModel.getContentMessage() + CHECK_NOW + shareModel.getUrl();

                if (shareModel.getContentMessage() != null
                        && !shareModel.getContentMessage().equals(""))
                    builder.setText(shareText);

                if (shareModel.getUrl() != null && !shareModel.getUrl().equals(""))
                    builder.setContentUrl(Uri.parse(shareModel.getUrl()));

                if (fragment != null)
                    fragment.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                else if (fragmentV4 != null)
                    fragmentV4.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);
                else
                    activity.startActivityForResult(builder.getIntent(), SHARE_GOOGLE_REQUEST_CODE);

            }
        };
    }

    private void setListener() {
    }

    protected View.OnClickListener shareCopyLink() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                ClipboardHandler.CopyToClipboard((Activity) activity, shareModel.getUrl());
                Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected View.OnClickListener shareFb() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                ShareDialog shareDialog;

                if (fragment != null)
                    shareDialog = new ShareDialog(fragment);
                else if (fragmentV4 != null)
                    shareDialog = new ShareDialog(fragmentV4);
                else
                    shareDialog = new ShareDialog(activity);

                shareDialog.registerCallback(callbackManager, new
                        FacebookCallback<Sharer.Result>() {
                            @Override
                            public void onSuccess(Sharer.Result result) {
                                dismissDialog();
                            }

                            @Override
                            public void onCancel() {
                                Log.i("facebook", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.i("facebook", "onError: " + error);
                                SnackbarManager.make(activity, error.toString(), SNACKBAR_DURATION).show();
                                dismissDialog();
                            }
                        });

                if (ShareDialog.canShow(ShareLinkContent.class)) {

                    ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
                    if (shareModel.getTitle() != null && !shareModel.getTitle().equals(""))
                        builder.setContentTitle(shareModel.getTitle());

                    if (shareModel.getContentMessage() != null && !shareModel.getContentMessage().equals(""))
                        builder.setContentDescription(shareModel.getContentMessage());

                    if (shareModel.getImageUrl() != null && !shareModel.getImageUrl().equals(""))
                        builder.setImageUrl(Uri.parse(shareModel.getImageUrl()));

                    if (shareModel.getUrl() != null && !shareModel.getUrl().equals(""))
                        builder.setContentUrl(Uri.parse(shareModel.getUrl()));

                    ShareLinkContent linkContent = builder.build();

                    shareDialog.show(linkContent);
                }
            }
        };
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
