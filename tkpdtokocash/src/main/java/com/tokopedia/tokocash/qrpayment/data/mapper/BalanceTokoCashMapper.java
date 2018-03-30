package com.tokopedia.tokocash.qrpayment.data.mapper;

import com.tokopedia.anals.ConsumerDrawerData;
import com.tokopedia.tokocash.anals.GetTokocashQuery;
import com.tokopedia.tokocash.qrpayment.presentation.model.ActionBalance;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class BalanceTokoCashMapper implements Func1<GetTokocashQuery.Data, BalanceTokoCash> {

    @Inject
    public BalanceTokoCashMapper() {
    }

    @Override
    public BalanceTokoCash call(GetTokocashQuery.Data balanceTokoCashEntity) {
        if (balanceTokoCashEntity != null && balanceTokoCashEntity.wallet() != null) {
            BalanceTokoCash balanceTokoCash = new BalanceTokoCash();

            GetTokocashQuery.Data.Wallet wallet = balanceTokoCashEntity.wallet();

            if (wallet.action() != null) {
                ActionBalance actionBalance = new ActionBalance();
                actionBalance.setApplinks(wallet.action().applinks());
                actionBalance.setLabelAction(wallet.action().text());
                actionBalance.setRedirectUrl(wallet.action().redirect_url());
                actionBalance.setVisibility(wallet.action().visibility());
                balanceTokoCash.setActionBalance(actionBalance);
            }

            balanceTokoCash.setApplinks(wallet.applinks());
            balanceTokoCash.setBalance(wallet.balance());
            balanceTokoCash.setHoldBalance(wallet.hold_balance());
            balanceTokoCash.setLink(wallet.linked() ? 1 : 0);
            balanceTokoCash.setRawBalance(wallet.rawBalance());
            balanceTokoCash.setRawHoldBalance(wallet.raw_hold_balance());
            balanceTokoCash.setRawThreshold(wallet.rawBalance());
            balanceTokoCash.setRawTotalBalance(wallet.raw_total_balance());
            balanceTokoCash.setRedirectUrl(wallet.redirect_url());
            //balanceTokoCash.setThreshold(balanceTokoCashEntity.getThreshold());
            balanceTokoCash.setTitleText(wallet.text());
            balanceTokoCash.setTotalBalance(wallet.total_balance());

            ArrayList<String> abTags = new ArrayList<>();
            if (wallet.ab_tags() != null) {
                int index = 0;
                for (GetTokocashQuery.Data.Ab_tag abtag : wallet.ab_tags()) {
                    abTags.add(abtag.toString());
                    index++;
                }
            }
            balanceTokoCash.setAbTags(abTags);

            return balanceTokoCash;
        }
        throw new RuntimeException("Error");
    }
}
