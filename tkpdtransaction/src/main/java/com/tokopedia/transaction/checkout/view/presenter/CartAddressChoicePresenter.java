package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private static final int ADDRESS_STATUS_DEFAULT = 2;
    private List<RecipientAddressModel> recipientAddresses = new ArrayList<>();
    private RecipientAddressModel selectedRecipientAddress;

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadAddresses() {
        List<RecipientAddressModel> models = DummyCreator.createDummyShipmentRecipients();
        for (RecipientAddressModel model : models) {
            if (model.getAddressStatus() == ADDRESS_STATUS_DEFAULT) {
                model.setSelected(true);
                selectedRecipientAddress = model;
            }
        }
        recipientAddresses.clear();
        recipientAddresses.addAll(models);
        getView().renderRecipientData();
    }

    @Override
    public List<RecipientAddressModel> getRecipientAddresses() {
        return recipientAddresses;
    }

    @Override
    public void setSelectedRecipientAddress(RecipientAddressModel model) {
        this.selectedRecipientAddress = model;
    }

    @Override
    public RecipientAddressModel getSelectedRecipientAddress() {
        return selectedRecipientAddress;
    }

    private static class DummyCreator {

        private static List<RecipientAddressModel> createDummyShipmentRecipients() {
            List<RecipientAddressModel> models = new ArrayList<>();
            RecipientAddressModel addressOne = new RecipientAddressModel();
            addressOne.setAddressStatus(1);
            addressOne.setId("1");
            addressOne.setRecipientName("John Doe");
            addressOne.setAddressName("Alamat Kantor");
            addressOne.setAddressStreet("Jl. Prof. Dr. Satrio Kav. 11 No. 111");
            addressOne.setDestinationDistrictName("Setiabudi");
            addressOne.setAddressCityName("Jakarta Selatan");
            addressOne.setRecipientPhoneNumber("080989999");
            addressOne.setDestinationDistrictId("2283");
            addressOne.setDestinationDistrictName("Setiabudi");
            addressOne.setTokenPickup("Tokopedia%2BKero:juMixO/k%2ButV%2BcQ4pVNm3FSG1pw%3D");
            addressOne.setUnixTime("1515753331");
            models.add(addressOne);

            RecipientAddressModel addressTwo = new RecipientAddressModel();
            addressTwo.setId("2");
            addressTwo.setAddressStatus(2);
            addressTwo.setRecipientName("John Doe");
            addressTwo.setAddressName("Alamat Rumah");
            addressTwo.setAddressStreet("Jl. Ir. Sukarno No. 21");
            addressTwo.setDestinationDistrictName("Tebet");
            addressTwo.setAddressCityName("Jakarta Selatan");
            addressTwo.setRecipientPhoneNumber("080989999");
            addressTwo.setDestinationDistrictId("2283");
            addressTwo.setDestinationDistrictName("tebet");
            addressTwo.setTokenPickup("Tokopedia%2BKero:juMixO/k%2ButV%2BcQ4pVNm3FSG1pw%3D");
            addressTwo.setUnixTime("1515753331");
            models.add(addressTwo);

            return models;
        }

    }
}
