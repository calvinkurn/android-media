package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class CurrentSolutionDomain {

    private int id;
    private String name;
    private String message;
    private String identifier;
    private SolutionProblemAmountDomain amount;

    public CurrentSolutionDomain(int id, String name, String message, String identifier, SolutionProblemAmountDomain amount) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.identifier = identifier;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SolutionProblemAmountDomain getAmount() {
        return amount;
    }

    public void setAmount(SolutionProblemAmountDomain amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
