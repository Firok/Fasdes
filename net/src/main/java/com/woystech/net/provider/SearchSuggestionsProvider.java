package com.woystech.net.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by firok on 7/24/2016.
 */
public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.woystech.net.provider.SearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
