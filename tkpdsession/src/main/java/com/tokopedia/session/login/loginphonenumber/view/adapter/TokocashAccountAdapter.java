package com.tokopedia.session.login.loginphonenumber.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public class TokocashAccountAdapter extends RecyclerView.Adapter<TokocashAccountAdapter.ViewHolder> {

    private ChooseTokocashAccount.View viewListener;
    private ArrayList<AccountTokocash> list;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        TextView email;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            mainView = itemView.findViewById(R.id.main_view);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewListener.onSelectedTokocashAccount(list.get(getAdapterPosition()));
                }
            });
        }
    }

    public static TokocashAccountAdapter createInstance(ChooseTokocashAccount.View
                                                                viewListener, ArrayList<AccountTokocash> listAccount) {
        return new TokocashAccountAdapter(viewListener, listAccount);
    }

    public TokocashAccountAdapter(ChooseTokocashAccount.View viewListener, ArrayList<AccountTokocash> listAccount) {
        this.list = listAccount;
        this.viewListener = viewListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TokocashAccountAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tokocash_account_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.avatar, list.get(position).getAvatarUrl());
        holder.name.setText(MethodChecker.fromHtml(list.get(position).getName()));
        holder.email.setText(list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
