package com.tokopedia.tokocash.autosweepmf.view.model;

public class AutoSweepDetail extends BaseModel {
    private int accountStatus;
    private double balance;
    private int autoSweepStatus;
    private long amountLimit;
    private String title;
    private String content;
    private String tooltipContent;

    public String getTooltipContent() {
        return tooltipContent;
    }

    public void setTooltipContent(String tooltipContent) {
        this.tooltipContent = tooltipContent;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAutoSweepStatus() {
        return autoSweepStatus;
    }

    public void setAutoSweepStatus(int autoSweepStatus) {
        this.autoSweepStatus = autoSweepStatus;
    }

    public long getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(long amountLimit) {
        this.amountLimit = amountLimit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AutoSweepDetail{" +
                "accountStatus=" + accountStatus +
                ", balance=" + balance +
                ", autoSweepStatus=" + autoSweepStatus +
                ", amountLimit=" + amountLimit +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tooltipContent='" + tooltipContent + '\'' +
                '}';
    }
}
