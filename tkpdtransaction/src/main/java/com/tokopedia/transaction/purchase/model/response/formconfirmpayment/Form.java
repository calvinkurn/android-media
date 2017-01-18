package com.tokopedia.transaction.purchase.model.response.formconfirmpayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 20/06/2016.
 */
public class Form {
    private static final String TAG = Form.class.getSimpleName();

    @SerializedName("use_otp")
    @Expose
    private Integer useOtp;
    @SerializedName("browser_ie")
    @Expose
    private String browserIe;
    @SerializedName("msisdn_verified")
    @Expose
    private String msisdnVerified;
    @SerializedName("datetime")
    @Expose
    private Datetime datetime;
    @SerializedName("bank_account")
    @Expose
    private List<BankAccount> bankAccount = new ArrayList<BankAccount>();
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("sysbank_account")
    @Expose
    private List<SysBankAccount> sysbankAccount = new ArrayList<SysBankAccount>();
    @SerializedName("method")
    @Expose
    private List<Method> method = new ArrayList<Method>();
    @SerializedName("token")
    @Expose
    private String token;

    public Integer getUseOtp() {
        return useOtp;
    }

    public void setUseOtp(Integer useOtp) {
        this.useOtp = useOtp;
    }

    public String getBrowserIe() {
        return browserIe;
    }

    public void setBrowserIe(String browserIe) {
        this.browserIe = browserIe;
    }

    public String getMsisdnVerified() {
        return msisdnVerified;
    }

    public void setMsisdnVerified(String msisdnVerified) {
        this.msisdnVerified = msisdnVerified;
    }

    public Datetime getDatetime() {
        return datetime;
    }

    public void setDatetime(Datetime datetime) {
        this.datetime = datetime;
    }

    public List<BankAccount> getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(List<BankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<SysBankAccount> getSysbankAccount() {
        return sysbankAccount;
    }

    public void setSysbankAccount(List<SysBankAccount> sysbankAccount) {
        this.sysbankAccount = sysbankAccount;
    }

    public List<Method> getMethod() {
        return method;
    }

    public List<Method> getMethod(boolean removeId5) {
        for (int i = 0; i < method.size(); i++) {
            if (method.get(i).getMethodId().equals("5") && removeId5) {
                method.remove(i);
            }
        }
        return method;
    }

    public void setMethod(List<Method> method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
