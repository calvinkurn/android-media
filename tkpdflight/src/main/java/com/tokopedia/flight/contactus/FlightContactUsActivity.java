package com.tokopedia.flight.contactus;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.contactus.model.FlightContactUsPassData;

/**
 * @author by alvarisi on 1/8/18.
 */

public class FlightContactUsActivity extends BaseActivity implements FlightContactUsListener {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final String CANCEL_SOLUTION_ID = "1377";
    private FlightContactUsPassData passData;

    public static Intent createContactUsIntent(Activity activity,
                                               String orderId,
                                               String message) {
        return getCallintIntent(
                activity,
                CANCEL_SOLUTION_ID,
                orderId,
                activity.getString(R.string.flight_contact_us_cancel_desc),
                activity.getString(R.string.flight_contact_us_cancel_attc),
                message,
                activity.getString(R.string.flight_contact_us_cancel_toolbar));
    }

    public static Intent getCallintIntent(Activity activity,
                                          String solutionId,
                                          String orderId,
                                          String descriptionTitle,
                                          String attachmentTitle,
                                          String description,
                                          String toolbarTitle) {
        Intent intent = new Intent(activity, FlightContactUsActivity.class);
        FlightContactUsPassData passData = new FlightContactUsPassData();
        passData.setSolutionId(solutionId);
        passData.setOrderId(orderId);
        passData.setDescriptionTitle(descriptionTitle);
        passData.setAttachmentTitle(attachmentTitle);
        passData.setDescription(description);
        passData.setToolbarTitle(toolbarTitle);
        intent.putExtra(EXTRA_PASS_DATA, attachmentTitle);
        return intent;
    }

    public static Intent createContactUsIntent(String orderId, int status) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        passData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_contact_us);
        if (getApplication() instanceof FlightModuleRouter) {
            Fragment fragment = ((FlightModuleRouter) getApplication()).getContactUsFragment(passData, this);
            getFragmentManager().beginTransaction()
                    .replace(R.id.parent_view, fragment)
                    .commit();
        }
    }

    @Override
    public void onFinishCreateTicket() {
        Toast.makeText(this, R.string.flight_contact_us_finish_message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
