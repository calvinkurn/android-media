package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.view.ICartAddressChoiceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoicePresenter extends BaseDaggerPresenter<ICartAddressChoiceView>
        implements ICartAddressChoicePresenter {

    private List<ShipmentRecipientModel> recipientAddresses = new ArrayList<>();
    private ShipmentRecipientModel selectedRecipientAddress;

    @Override
    public void attachView(ICartAddressChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadAddresses() {
        List<ShipmentRecipientModel> models = DummyCreator.createDummyShipmentRecipients();
        recipientAddresses.clear();
        recipientAddresses.addAll(models);
        getView().renderRecipientData();
    }

    @Override
    public List<ShipmentRecipientModel> getRecipientAddresses() {
        return recipientAddresses;
    }

    @Override
    public void setSelectedRecipientAddress(ShipmentRecipientModel model) {
        this.selectedRecipientAddress = model;
    }

    @Override
    public ShipmentRecipientModel getSelectedRecipientAddress() {
        return selectedRecipientAddress;
    }

    private static class DummyCreator {

        private static List<ShipmentRecipientModel> createDummyShipmentRecipients() {
            List<ShipmentRecipientModel> models = new ArrayList<>();
            ShipmentRecipientModel addressOne = new ShipmentRecipientModel();
            addressOne.setPrimerAddress(true);
            addressOne.setId("1");
            addressOne.setAddressIdentifier("Utama");
            addressOne.setRecipientName("John Doe");
            addressOne.setRecipientAddressDescription("Alamat Kantor");
            addressOne.setRecipientAddress("Jl. Prof. Dr. Satrio Kav. 11 No. 111, Setiabudi, Jakarta Selatan");
            addressOne.setRecipientPhoneNumber("080989999");
            addressOne.setDestinationDistrictId("2283");
            addressOne.setDestinationDistrictName("Setiabudi");
            addressOne.setTokenPickup("Tokopedia%2BKero:juMixO/k%2ButV%2BcQ4pVNm3FSG1pw%3D");
            addressOne.setUnixTime("1515753331");
            models.add(addressOne);

            ShipmentRecipientModel addressTwo = new ShipmentRecipientModel();
            addressTwo.setId("2");
            addressTwo.setPrimerAddress(false);
            addressTwo.setAddressIdentifier("");
            addressTwo.setRecipientName("John Doe");
            addressTwo.setRecipientAddressDescription("Alamat Rumah");
            addressTwo.setRecipientAddress("Jl. Ir. Sukarno No. 21, Tebet, Jakarta Selatan");
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
