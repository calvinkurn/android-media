package com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat;

/**
 * Created by yoasfs on 10/10/17.
 */

public class ConversationFlagDomain {

    private int system;
    private int solution;

    public ConversationFlagDomain(int system, int solution) {
        this.system = system;
        this.solution = solution;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int systrem) {
        this.system = systrem;
    }

    public int getSolution() {
        return solution;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }
}
