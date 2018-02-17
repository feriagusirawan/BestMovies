package com.movies.feriagusirawan.android.bestmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import com.movies.feriagusirawan.android.bestmovies.R;
import com.movies.feriagusirawan.android.bestmovies.models.Trailer;

import java.util.List;

import static com.movies.feriagusirawan.android.bestmovies.extras.Url.PARAM_THUMB;
import static com.movies.feriagusirawan.android.bestmovies.extras.Url.THUMB_URL;
import static com.movies.feriagusirawan.android.bestmovies.extras.Url.VID_URL;
import static com.movies.feriagusirawan.android.bestmovies.frags.DetailFragment.trailerList;

public class TrailersAdapter extends BaseAdapter {

    private List<Trailer> trailers;
    private Context context;
    private LayoutInflater inflater;

    public TrailersAdapter(List<Trailer> trailers, Context context) {
        this.trailers = trailers;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int position) {
        return trailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.video_item, parent, false);
            holder = new ViewHolder();
            holder.tubeThumb = (ImageView) convertView.findViewById(R.id.tubeThumb);
            holder.trailerTitle = (TextView) convertView.findViewById(R.id.trailerTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Trailer trailer = trailers.get(position);
        String thumbUrl = THUMB_URL + trailer.getKey() + PARAM_THUMB;
        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .displayer(new FadeInBitmapDisplayer(1500))
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.vid_hover)
                .showImageOnFail(R.drawable.vid_hover)
                .build();

        ImageLoader.getInstance()
                .displayImage(thumbUrl, holder.tubeThumb, mOptions);
        holder.tubeThumb.setImageResource(R.drawable.vid_hover);
        for (int i = 0; i < trailers.size(); i++) {
            holder.trailerTitle.setText(trailer.getName());
        }
        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String vidUrl = VID_URL + trailers.get(position).getKey();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(vidUrl)));
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView tubeThumb;
        TextView trailerTitle;
    }
}
