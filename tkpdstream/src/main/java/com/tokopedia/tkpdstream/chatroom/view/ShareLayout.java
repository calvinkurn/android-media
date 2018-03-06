package com.tokopedia.tkpdstream.chatroom.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;

import java.util.ArrayList;

/**
 * @author by StevenFredian on 2/26/18.
 */

public class ShareLayout {

    private static final int SNACKBAR_DURATION = 3500;
    private static final int SHARE_GOOGLE_REQUEST_CODE = 123;
    private static final String CHECK_NOW = " cek sekarang di :\n";
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final RecyclerView appGrid;
    private final CallbackManager callbackManager;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentV4;
    private final StreamAnalytics analytics;

    private ShareData shareModel;
    private ShareFeedAdapter adapter;
    private ArrayList<ShareItem> list;

    private String urlLink;

    public ShareLayout(android.support.v4.app.Fragment fragment, CallbackManager callbackManager, String channelUrl, StreamAnalytics analytics) {
        this.fragment = null;
        this.fragmentV4 = fragment;
        this.activity = fragment.getActivity();
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_groupchat_dialog);
        appGrid = this.dialog.findViewById(R.id.grid);
        urlLink = channelUrl;
        this.analytics = analytics;
        initVar(fragment.getActivity());
        initAdapter();
        setListener();
        setShareList();
    }

    private void initVar(Context context) {
        String link = "https://tokopedia.com/groupchat/{channel_url}";
        urlLink = link.replace("{channel_url}", urlLink);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        appGrid.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        this.dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
    }

    public void setShareModel(ShareData shareModel) {
        this.shareModel = shareModel;
    }

    private void initAdapter() {
        adapter = new ShareFeedAdapter();
        appGrid.setAdapter(adapter);
    }

    protected void setShareList() {

        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_g), activity.getString(R.string.share_gplus), shareGoogle(activity.getString(R.string.share_gplus))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_fb), activity.getString(R.string.share_fb), shareFb(activity.getString(R.string.share_fb))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_twitter), activity.getString(R.string.share_twitter), shareTwitter(activity.getString(R.string.share_twitter))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_line), activity.getString(R.string.share_line), shareLine(activity.getString(R.string.share_line))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_wa), activity.getString(R.string.share_wa), shareWhatsapp(activity.getString(R.string.share_wa))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_sms), activity.getString(R.string.share_sms), shareSMS(activity.getString(R.string.share_sms))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_copy), activity.getString(R.string.share_copy), shareCopyLink(activity.getString(R.string.share_copy))));
        list.add(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_btn_other), activity.getString(R.string.share_others), shareOthers(activity.getString(R.string.share_others))));
        adapter.setList(list);
    }



    private View.OnClickListener shareOthers(final String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, urlLink);
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
                analytics.eventClickShareChannel(string);
            }
        };
    }

    private View.OnClickListener shareSMS(final String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = MethodChecker.getSmsIntent(activity, urlLink);
                activity.startActivity(smsIntent);
                analytics.eventClickShareChannel(string);
            }
        };
    }

    private View.OnClickListener shareTwitter(String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.twitter.android");
            }
        };
    }

    private View.OnClickListener shareWhatsapp(String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("com.whatsapp");
            }
        };
    }

    private void shareToApp(final String appName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(appName);

        if (!TextUtils.isEmpty(urlLink))
            intent.putExtra(Intent.EXTRA_TEXT, urlLink);

        try {
            activity.startActivity(intent);
            analytics.eventClickShareChannel(appName);
        } catch (android.content.ActivityNotFoundException ex) {

            Toast.makeText(activity,
                    activity.getString(R.string.error_apps_not_installed),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private View.OnClickListener shareLine(String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToApp("jp.naver.line.android");
            }
        };
    }

    private View.OnClickListener shareGoogle(final String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                analytics.eventClickShareChannel(string);
                PlusShare.Builder builder = new PlusShare.Builder(activity);

                builder.setType("text/plain");

                if (!TextUtils.isEmpty(urlLink))
                    builder.setText(urlLink);

                if (!TextUtils.isEmpty(urlLink))
                    builder.setContentUrl(Uri.parse(urlLink));

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

    protected View.OnClickListener shareCopyLink(String string) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Activity.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Tokopedia", urlLink);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected View.OnClickListener shareFb(final String string) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();

                final ShareDialog shareDialog;

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

                    if (shareModel != null && !TextUtils.isEmpty(urlLink)) {
                        ShareLinkContent.Builder linkBuilder = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(urlLink));

                        if (!TextUtils.isEmpty(shareModel.getName())) {
                            linkBuilder.setContentTitle(shareModel.getName());
                        }
                        if (!TextUtils.isEmpty(shareModel.getTextContent(activity))) {
                            linkBuilder.setContentDescription(shareModel.getTextContent(activity));
                        }
                        if (!TextUtils.isEmpty(shareModel.getDescription())) {
                            linkBuilder.setQuote(shareModel.getDescription());
                        }
                        if (!TextUtils.isEmpty(shareModel.getImgUri())) {
                            linkBuilder.setImageUrl(Uri.parse(shareModel.getImgUri()));
                        }
                        ShareLinkContent linkContent = linkBuilder.build();
                        shareDialog.show(linkContent);
                        analytics.eventClickShareChannel(string);
                    }
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
