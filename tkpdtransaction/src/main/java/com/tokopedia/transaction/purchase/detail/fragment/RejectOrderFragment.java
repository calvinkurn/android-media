package com.tokopedia.transaction.purchase.detail.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/2/18. Tokopedia
 */

public class RejectOrderFragment extends TkpdFragment {

    private static final String ORDER_ID_ARGUMENT = "ORDER_ID_ARGUMENT";

    private RejectOrderFragmentListener listener;

    public static RejectOrderFragment createFragment(String orderId) {
        RejectOrderFragment fragment = new RejectOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_ARGUMENT, orderId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RejectOrderFragmentListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RejectOrderFragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reject_order_fragment, container, false);
        TextView reason1 = view.findViewById(R.id.reason_1);
        TextView reason2 = view.findViewById(R.id.reason_2);
        TextView reason3 = view.findViewById(R.id.reason_3);
        TextView reason4 = view.findViewById(R.id.reason_4);
        TextView reason5 = view.findViewById(R.id.reason_5);
        TextView reason6 = view.findViewById(R.id.reason_6);
        reason1.setOnClickListener(onReasonClickedListener());
        reason2.setOnClickListener(onReasonClickedListener());
        reason3.setOnClickListener(onReasonClickedListener());
        reason4.setOnClickListener(onReasonClickedListener());
        reason5.setOnClickListener(onReasonClickedListener());
        reason6.setOnClickListener(onReasonClickedListener());
        return view;
    }

    private View.OnClickListener onReasonClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReject(
                        ((TextView) view).getText().toString(),
                        getArguments().getString(ORDER_ID_ARGUMENT)
                );

            }
        };
    }

    public interface RejectOrderFragmentListener {
        void onReject(String reason, String orderId);
    }
}
