package com.movies.feriagusirawan.android.bestmovies.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import com.movies.feriagusirawan.android.bestmovies.R;
import com.movies.feriagusirawan.android.bestmovies.activities.DetailActivity;
import com.movies.feriagusirawan.android.bestmovies.activities.MainActivity;
import com.movies.feriagusirawan.android.bestmovies.frags.DetailFragment;
import com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment;
import com.movies.feriagusirawan.android.bestmovies.models.Movie;
import com.movies.feriagusirawan.android.bestmovies.utils.DBHelper;

import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.movies.feriagusirawan.android.bestmovies.extras.Url.POSTERS_URL;
import static com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment.adapter;
import static com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment.fav;
import static com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment.gridView;
import static com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment.setAdapter;
import static com.movies.feriagusirawan.android.bestmovies.frags.MoviesFragment.up;


public class CoreAdapter extends RecyclerView.Adapter<CoreAdapter.PosterHolder> {

    public static List<Movie> movies;
    private Context context;
    private Activity activity;
    private DBHelper helper;

    public CoreAdapter(List<Movie> movies, Context context, Activity activity) {
        CoreAdapter.movies = movies;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public PosterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        helper = new DBHelper(v.getContext());
        return new PosterHolder(v);
    }

    @Override
    public void onBindViewHolder(final PosterHolder holder, final int position) {
        holder.fav.setVisibility(VISIBLE);
        holder.unFav.setVisibility(GONE);
        holder.fav.invalidate();
        holder.unFav.invalidate();
        final Movie movie = movies.get(position);
        String posterUrl = POSTERS_URL + "w500" + movie.getPoster();

        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .displayer(new FadeInBitmapDisplayer(1500))
                .resetViewBeforeLoading(true)
                .showImageOnFail(R.drawable.no_poster)
                .showImageForEmptyUri(R.drawable.no_poster)
                .build();

        ImageLoader.getInstance()
                .displayImage(posterUrl,
                        holder.poster, mOptions,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                Palette p = Palette.from(loadedImage).generate();
                                holder.posterTitleBackground.setBackgroundColor(p.getVibrantColor(0));
                                if (p.getVibrantColor(0) == Color.TRANSPARENT) {
                                    holder.posterTitleBackground.setBackgroundColor(p.getMutedColor(0));
                                }
                                holder.posterTitleBackground.getBackground().setAlpha(160);
                            }
                        });

        final String date, year;
        date = movie.getDate();
        if (date.equals("")) {
            year = "Unknown";
        } else {
            year = date.substring(0, 4);
        }
        holder.posterTitle.setText(movie.getTitle());
        holder.posterTitle.setSelected(true);
        holder.posterYear.setText(year);
        if (position >= 25) {
            up.setVisible(true);
        } else {
            up.setVisible(false);
        }

        up.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MoviesFragment.gridView.smoothScrollToPosition(0);
                return true;
            }
        });

        final ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(165);

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fav.setVisibility(GONE);
                holder.unFav.startAnimation(animation);
                holder.unFav.setVisibility(VISIBLE);

                helper.insertRow(movie.getPoster(), movie.getBackdrop(), movie.getId(),
                        movie.getTitle(), movie.getDate(), movie.getVoteAvr(),
                        movie.getOverview());
            }
        });

        holder.unFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unFav.setVisibility(GONE);
                holder.fav.startAnimation(animation);
                holder.fav.setVisibility(VISIBLE);

                helper.deleteRow(movie.getId());

                if (fav.isChecked()) {
                    movies.remove(position);
                    setAdapter(adapter);
                    gridView.scrollToPosition(position - 1);
                    adapter.notifyItemRemoved(position - 1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        boolean ifExist = helper.ifExist(movie.getId());
        if (ifExist) {
            holder.unFav.setVisibility(VISIBLE);
            holder.fav.setVisibility(GONE);
        } else {
            holder.unFav.setVisibility(GONE);
            holder.fav.setVisibility(VISIBLE);
        }

        holder.gridCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration conf = v.getResources().getConfiguration();
                if (conf.smallestScreenWidthDp >= 600) {
                    DetailFragment frag = new DetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie", movie);
                    bundle.putInt("pos", position);
                    frag.setArguments(bundle);
                    activity.getFragmentManager().beginTransaction()
                            .replace(R.id.detailFrameContainer, frag).commit();
                    MainActivity.cornStamp.setVisibility(GONE);
                } else {
                    Intent data = new Intent(context, DetailActivity.class);
                    String title = movie.getTitle();
                    String overview = movie.getOverview();

                    final String date, year;
                    date = movie.getDate();
                    if (date.equals("")) {
                        year = "Unknown";
                    } else {
                        year = date.substring(0, 4);
                    }
                    if (title.equals("")) {
                        title = "Not available.";
                    } else {
                        title = movie.getTitle();
                    }
                    if (overview.equals("")) {
                        overview = "No overview found.";
                    } else {
                        overview = movie.getOverview();
                    }

                    data.putExtra("poster", movie.getPoster());
                    data.putExtra("backdrop", movie.getBackdrop());
                    data.putExtra("title", title);
                    data.putExtra("overview", overview);
                    data.putExtra("date", year);
                    data.putExtra("vote", movie.getVoteAvr());
                    data.putExtra("id", movie.getId());
                    data.putExtra("position", position);

                    if (conf.smallestScreenWidthDp < 600) {
                        if (SDK_INT >= LOLLIPOP) {
                            ActivityOptions options =
                                    ActivityOptions.makeSceneTransitionAnimation(
                                            (Activity) context, holder.dummyView, "");
                            context.startActivity(data, options.toBundle());
                        } else {
                            context.startActivity(data);
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class PosterHolder extends RecyclerView.ViewHolder {
        CardView gridCard;
        ImageView poster, fav, unFav;
        View posterTitleBackground, dummyView;
        TextView posterTitle, posterYear;

        public PosterHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.gridPoster);
            posterTitle = (TextView) itemView.findViewById(R.id.posterTitle);
            gridCard = (CardView) itemView.findViewById(R.id.gridCard);
            posterTitleBackground = itemView.findViewById(R.id.posterTitleBackground);
            fav = (ImageView) itemView.findViewById(R.id.posterFav);
            unFav = (ImageView) itemView.findViewById(R.id.posterUnFav);
            posterYear = (TextView) itemView.findViewById(R.id.posterYear);
            dummyView = itemView.findViewById(R.id.dummyView);
        }
    }

    public void clear() {
        if (movies != null) {
            int size = movies.size();
            movies.clear();
            notifyItemRangeRemoved(0, size);
        }
    }
}
