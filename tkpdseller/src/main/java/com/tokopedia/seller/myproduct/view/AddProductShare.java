package com.tokopedia.seller.myproduct.view;

import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ConnectionDetector;
import com.tkpd.library.utils.TwitterHandler;
import com.tokopedia.core.R;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.utils.DelegateOnClick;

/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductShare {
    public boolean isFacebookAuth = false;
    public boolean isTwitterAuth = false;

    ConnectionDetector cd;
    DelegateOnClick delegateOnClick;

    RelativeLayout facebookShareBut;
    RelativeLayout twitterShareBut;
    ImageView facebookShare;
    ImageView addProductTwitter;
    CheckBox facebookCheckBut;
    CheckBox twitterCheckBut;
    TextView facebookTextView;
    TextView berbagiTitleTextView;

    public AddProductShare(View view){
        facebookShareBut = (RelativeLayout) view.findViewById(R.id.add_product_facebook_but);
        facebookShareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
                    isFacebookAuth = !isFacebookAuth;
                    facebookCheckBut.setChecked(isFacebookAuth);
                }
            }
        });
        twitterShareBut = (RelativeLayout) view.findViewById(R.id.twitter_but);
        twitterShareBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterCall();
            }
        });
        facebookShare = (ImageView) view.findViewById(R.id.add_product_facebook);
        addProductTwitter = (ImageView) view.findViewById(R.id.add_product_twitter);
        facebookCheckBut = (CheckBox) view.findViewById(R.id.facebook_checkbut);
        facebookCheckBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                butFacebookToggle(checked);
                isFacebookAuth = checked;
            }
        });
        twitterCheckBut = (CheckBox) view.findViewById(R.id.twitter_checkbut);
        twitterCheckBut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                butTwitterToggle(isChecked);
            }
        });
        facebookTextView = (TextView) view.findViewById(R.id.add_product_facebook_text);
        berbagiTitleTextView = (TextView) view.findViewById(R.id.berbagi_title);

        setToShareButton(view);
    }

    private void twitterCall() {
        if (delegateOnClick != null && delegateOnClick instanceof AddProductFragment) {
            cd = new ConnectionDetector(((AddProductFragment) delegateOnClick).getActivity());
            if (cd.isConnectingToInternet()) {
                if (!isTwitterAuth) {
                    TwitterHandler th = ((AddProductFragment) delegateOnClick).th;
                    if (!th.isTwitterLoggedInAlready()) {
                        ((AddProductFragment) delegateOnClick).showDialog();
                    }
                    butTwitterToggle(true);
                    isTwitterAuth = true;
                    twitterCheckBut.setChecked(true);
                } else {
                    butTwitterToggle(false);
                    isTwitterAuth = false;
                    twitterCheckBut.setChecked(false);
                }
            } else {
                cd.showNoConnection();
            }
        }
    }

    public void butFacebookToggle(boolean on){
        if(on){
            facebookShare.setImageResource(R.drawable.facebook_square_blue);
        }else{
            facebookShare.setImageResource(R.drawable.facebook_square);
        }
    }

    public void butTwitterToggle(boolean on){
        if (on){
            addProductTwitter.setImageResource(R.drawable.twitter_square_blue);
        } else{
            addProductTwitter.setImageResource(R.drawable.twitter_square);
        }
    }
    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }

    public void setToShareButton(View view){
        facebookShare.setVisibility(View.GONE);
        twitterShareBut.setVisibility(View.GONE);
        berbagiTitleTextView.setVisibility(View.GONE);
        facebookTextView.setText(view.getResources().getString(R.string.title_share));
        facebookTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);


    }
}
