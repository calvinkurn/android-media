package com.tokopedia.otp.cotp.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.session.R;

import java.util.ArrayList;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationMethodAdapter extends RecyclerView.Adapter<VerificationMethodAdapter
        .ViewHolder> {

    private final SelectVerification.View viewListener;
    private ArrayList<MethodItem> list;

    public VerificationMethodAdapter(ArrayList<MethodItem> list, SelectVerification.View viewListener) {
        this.list = list;
        this.viewListener = viewListener;
    }

    public static VerificationMethodAdapter createInstance(ArrayList<MethodItem> list,
                                                           SelectVerification.View
                                                                   viewListener) {
        return new VerificationMethodAdapter(list, viewListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView methodText;
        ImageView icon;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            methodText = itemView.findViewById(R.id.method_text);
            mainView = itemView.findViewById(R.id.main_view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verification_method_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(holder.icon, list.get(position)
                .getIconResId());
        holder.methodText.setText(MethodChecker.fromHtml(list.get(position).getMethodText()));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onMethodSelected(list.get(position).getType());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
