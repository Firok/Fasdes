package com.woystech.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by firok on 7/24/2016.
 */
public class VerticalItemDecorator extends RecyclerView.ItemDecoration{

    private final int verticalSpaceWidth;

    public VerticalItemDecorator(int verticalSpaceWidth) {
        this.verticalSpaceWidth = verticalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = outRect.bottom = verticalSpaceWidth;
    }
}
