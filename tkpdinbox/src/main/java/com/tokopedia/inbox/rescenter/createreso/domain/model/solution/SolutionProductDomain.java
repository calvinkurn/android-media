package com.tokopedia.inbox.rescenter.createreso.domain.model.solution;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProductDomain {

    private String name;
    private int price;
    private SolutionProductImageDomain image;

    public SolutionProductDomain(String name, int price, SolutionProductImageDomain image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SolutionProductImageDomain getImage() {
        return image;
    }

    public void setImage(SolutionProductImageDomain image) {
        this.image = image;
    }

}
