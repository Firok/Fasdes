package com.woystech.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by firok on 7/24/2016.
 */
public class HorizontalItemDecorator extends RecyclerView.ItemDecoration{

    private final int horizontalSpaceWidth;

    public HorizontalItemDecorator(int horizontalSpaceWidth) {
        this.horizontalSpaceWidth = horizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = outRect.right = horizontalSpaceWidth;
    }
}
