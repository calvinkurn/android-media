package com.tokopedia.tkpd.tkpdfeed.feedplus.view.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
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

/**
 * @author by nisie on 5/18/17.
 */

public class ShareBottomDialog {

    private static final int SNACKBAR_DURATION = 3500;
    private static final int SHARE_GOOGLE_REQUEST_CODE = 123;
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final GridView appGrid;
    private final View cancelButton;
    private final CallbackManager callbackManager;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentV4;

    private ShareModel shareModel;
    private ShareAdapter adapter;

    public ShareBottomDialog(Activity activity,
                             CallbackManager callbackManager) {
        this.activity = activity;
        this.fragment = null;
        this.fragmentV4 = null;
        this.dialog = new BottomSheetDialog(activity);
        this.callbackManager = callbackManager;
        this.dialog.setContentView(R.layout.share_review_dialog);
        appGrid = (GridView) this.dialog.findViewById(R.id.grid);
        cancelButton = this.dialog.findViewById(R.id.cancel_but);
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
        this.dialog.setContentView(R.layout.share_review_dialog);
        appGrid = (GridView) this.dialog.findViewById(R.id.grid);
        cancelButton = this.dialog.findViewById(R.id.cancel_but);
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
        this.dialog.setContentView(R.layout.share_review_dialog);
        appGrid = (GridView) this.dialog.findViewById(R.id.grid);
        cancelButton = this.dialog.findViewById(R.id.cancel_but);
        initAdapter();
        setListener();
        setShareList();
    }

    public void setShareModel(ShareModel shareModel) {
        this.shareModel = shareModel;
    }

    private void initAdapter() {
        if (appGrid != null) {
            appGrid.setNumColumns(4);
        }
        adapter = new ShareAdapter(activity);
        appGrid.setAdapter(adapter);
    }

    protected void setShareList() {
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_google_share), "Google+", shareGoogle()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_facebook_share), "Facebook", shareFb()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_line_share), "Line", shareLine()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_whatsapp_share), "Whatsapp", shareWhatsapp()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_twitter_share), "Twitter", shareTwitter()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_message), "SMS", shareSMS()));
        adapter.addItem(new ShareItem(MethodChecker.getDrawable(activity,
                R.drawable.ic_copy_share), "Copy Link", shareCopyLink()));
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener shareSMS() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                if (shareModel.getTitle() != null
                        && !shareModel.getTitle().equals(""))
                    share.putExtra(Intent.EXTRA_SUBJECT, shareModel.getTitle());

                if (shareModel.getUrl() != null
                        && !shareModel.getUrl().equals(""))
                    share.putExtra(Intent.EXTRA_TEXT, shareModel.getUrl());

                activity.startActivity(Intent.createChooser(share, "Share link!"));
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
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage(appName);

        if (shareModel.getUrl() != null
                && !shareModel.getUrl().equals(""))
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareModel.getUrl());

        try {
            activity.startActivity(whatsappIntent);
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

                if (shareModel.getContentMessage() != null
                        && !shareModel.getContentMessage().equals(""))
                    builder.setText(shareModel.getContentMessage());

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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
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
