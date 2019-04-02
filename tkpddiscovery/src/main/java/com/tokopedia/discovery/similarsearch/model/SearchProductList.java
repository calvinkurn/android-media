package com.tokopedia.discovery.similarsearch.model;

import com.google.gson.annotations.SerializedName;

public class SearchProductList{

	@SerializedName("similar_products_image_search")
	private SimilarProductsImageSearch similarProductsImageSearch;

	public void setSimilarProductsImageSearch(SimilarProductsImageSearch similarProductsImageSearch){
		this.similarProductsImageSearch = similarProductsImageSearch;
	}

	public SimilarProductsImageSearch getSimilarProductsImageSearch(){
		return similarProductsImageSearch;
	}

	@Override
 	public String toString(){
		return 
			"SearchProductList{" + 
			"similar_products_image_search = '" + similarProductsImageSearch + '\'' + 
			"}";
		}
}