package com.tokopedia.topads.sdk.network;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public enum HttpMethod {
	GET("GET"), POST("POST");

	private String description;

	private HttpMethod(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}
