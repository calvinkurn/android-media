package com.tokopedia.seller.gmsubscribe.data.mapper;

import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GMCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.exception.GMCheckoutCheckException;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeCheckoutMapper implements Func1<GMCheckoutServiceModel, GMCheckoutDomainModel> {
    @Override
    public GMCheckoutDomainModel call(GMCheckoutServiceModel gmCheckoutServiceModel) {
        try {
            return mapServiceToDomain(gmCheckoutServiceModel);
        } catch (Exception e) {
            throw new GMCheckoutCheckException("Unsuported URL toppay");
        }
    }

    private GMCheckoutDomainModel mapServiceToDomain(GMCheckoutServiceModel gmCheckoutServiceModel) throws Exception {
        if(gmCheckoutServiceModel.getError() != null){
            throw new GMCheckoutCheckException("Unsuported URL toppay");
        }
        GMCheckoutDomainModel domainModel = new GMCheckoutDomainModel();
        domainModel.setPaymentUrl(gmCheckoutServiceModel.getPaymentURL());
        String parameterUnencoded = decodeUrl(gmCheckoutServiceModel.getParameter1());
        domainModel.setParameter(parameterUnencoded);
        domainModel.setCallbackUrl(gmCheckoutServiceModel.getCallbackurl());
        domainModel.setPaymentId(Integer.valueOf(gmCheckoutServiceModel.getPaymentId()));
        return domainModel;
    }

    private String decodeUrl(String in)    {
        String working = in;
        int index;
        index = working.indexOf("\\u");
        while(index > -1)
        {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\u");
        }
        return working;
    }
}
