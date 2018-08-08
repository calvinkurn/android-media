package com.tokopedia.ride.ontrip.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.ride.R;
import com.tokopedia.ride.common.ride.domain.model.PendingPayment;
import com.tokopedia.ride.completetrip.view.PendingFareChooserActivity;
import com.tokopedia.ride.completetrip.view.viewmodel.TokoCashProduct;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TopupTokoCashChangeDestination extends BaseActivity {

    private static final String EXTRA_PENDING_PAYMENT = "EXTRA_PENDING_PAYMENT";
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    private static final int TOKOCASH_PRODUCT_REQUEST_CODE = 101;

    private PendingPayment pendinPayment;
    private Toolbar toolbar;
    private DigitalCheckoutPassData passData;
    private TextView tokocashSelectedProductTextView;
    private String requestId;

    public static Intent getCallingIntent(Activity activity, PendingPayment pendingPayment, String requestId) {
        Intent intent = new Intent(activity, TopupTokoCashChangeDestination.class);
        intent.putExtra(EXTRA_PENDING_PAYMENT, pendingPayment);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_toko_cash_change_destination);
        pendinPayment = getIntent().getParcelableExtra(EXTRA_PENDING_PAYMENT);
        requestId = getIntent().getStringExtra(EXTRA_REQUEST_ID);
        setupVariable();
        setupViewListener();
    }

    private void setupVariable() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tokocashSelectedProductTextView = (TextView) findViewById(R.id.tv_tokocash_selected_product);

        passData = new DigitalCheckoutPassData();
        passData.setCategoryId(pendinPayment.getCategoryId());
        passData.setOperatorId(pendinPayment.getOperatorId());
        Map<String, String> maps = splitQuery(Uri.parse(pendinPayment.getTopupUrl()));
        if (maps.get(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN) != null)
            passData.setUtmCampaign(maps.get(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN));
        if (maps.get(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER) != null)
            passData.setClientNumber(maps.get(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
        if (maps.get(DigitalCheckoutPassData.PARAM_UTM_SOURCE) != null)
            passData.setUtmSource(maps.get(DigitalCheckoutPassData.PARAM_UTM_SOURCE));
        if (maps.get(DigitalCheckoutPassData.PARAM_UTM_CONTENT) != null)
            passData.setUtmContent(maps.get(DigitalCheckoutPassData.PARAM_UTM_CONTENT));
        if (maps.get(DigitalCheckoutPassData.PARAM_IS_PROMO) != null)
            passData.setIsPromo(maps.get(DigitalCheckoutPassData.PARAM_IS_PROMO));
        if (maps.get(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT) != null)
            passData.setInstantCheckout(maps.get(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT));


        if (pendinPayment.getTopUpOptions() != null && pendinPayment.getTopUpOptions().size() > 0) {
            TokoCashProduct product = pendinPayment.getTopUpOptions().get(0);
            tokocashSelectedProductTextView.setText(product.getTitle());
            passData.setProductId(product.getId());
        }
    }

    private void setupViewListener() {
        //setup toolbar
        if (toolbar != null) {
            toolbar.setTitle(R.string.change_destination_insufficient_balance_screen_title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }

        //setup click events
        findViewById(R.id.layout_tokocash_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFareLayout();
            }
        });

        findViewById(R.id.btn_topup_tokocash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionTopupTokocash();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (resultCode == IDigitalModuleRouter.PAYMENT_SUCCESS) {
                    setResult(IDigitalModuleRouter.PAYMENT_SUCCESS);
                    finish();
                } else {
                    NetworkErrorHelper.showSnackbar(this, getString(R.string.complete_trip_payment_failed));
                }
                break;
            case TOKOCASH_PRODUCT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    TokoCashProduct product = data.getParcelableExtra(PendingFareChooserActivity.EXTRA_PRODUCT);
                    tokocashSelectedProductTextView.setText(product.getTitle());
                    passData.setProductId(product.getId());
                }
                break;
        }
    }

    private Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException | NullPointerException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
    }

    private void actionFareLayout() {
        List<TokoCashProduct> products = pendinPayment.getTopUpOptions();
        startActivityForResult(PendingFareChooserActivity.getCallingIntent(this, products), TOKOCASH_PRODUCT_REQUEST_CODE);
    }

    private void actionTopupTokocash() {
        if (passData == null) return;
        if (getApplication() instanceof IDigitalModuleRouter) {
            passData.setIdemPotencyKey(generateIdEmpotency());
            IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getApplication();
            startActivityForResult(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(passData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    private String generateIdEmpotency() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return String.format("%s_%s", requestId, token.isEmpty() ? timeMillis : token);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_TOPUP_TOKOCASH_CHANGE_DESTINATION;
    }
}
