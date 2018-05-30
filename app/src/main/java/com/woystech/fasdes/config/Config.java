package com.woystech.fasdes.config;

/**
 * Created by firok on 7/24/2016.
 */
public interface Config {
    // Google Console APIs developer key
    // Replace this key with your's
    String DEVELOPER_KEY = "AIzaSyABYoczeHg4XABx_jMRfv-CqmA2YMsIY4A";
    String API_KEY = "746bcc0040f68b8af9d569f27443901f";

    //DISCOVER MOVIES PARAMS
    String SORT_BY ="sort_by";
    String SORT_BY_KEY ="vote_average.desc";
    String WITH_GENRES = "with_genres";
    String PAGE = "page";
    String SIZE ="size";
    String START ="start";
    int DEFAULT_SIZE =20;
    String EMAIL ="email";
    String PASSWORD ="password";
}
