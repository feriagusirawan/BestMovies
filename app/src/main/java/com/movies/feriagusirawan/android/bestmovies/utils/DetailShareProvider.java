package com.movies.feriagusirawan.android.bestmovies.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;

import com.movies.feriagusirawan.android.bestmovies.R;


public class DetailShareProvider extends ShareActionProvider {

    private final Context mContext;

    public DetailShareProvider(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateActionView() {
        ActivityChooserView chooserView =
                (ActivityChooserView) super.onCreateActionView();

        // Set your drawable here
        Drawable icon =
                mContext.getResources().getDrawable(R.drawable.action_share);
        chooserView.setExpandActivityOverflowButtonDrawable(icon);

        return chooserView;
    }
}
