package com.woystech.common;

import android.Manifest;

/**
 * Created by Yohan on 03-04-2016.
 */
public interface ApplicationConstants {

    //Currency
    static final String CURRENT_CURRENCY="XOF";
    static final int REQUEST_CODE_NETWORK = 2006;

    static final String[] SHARE_PERMISSIONS = { Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};

}
