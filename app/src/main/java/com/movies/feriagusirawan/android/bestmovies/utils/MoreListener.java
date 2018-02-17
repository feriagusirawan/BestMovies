package com.movies.feriagusirawan.android.bestmovies.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class MoreListener extends RecyclerView.OnScrollListener {
    public static String TAG = MoreListener.class.getSimpleName();

    private int previousTotal = 0; // jumlah total item dalam kumpulan data terakhir
    private boolean loading = true; // true untuk atau jika kita masih menunggu data terakhir untuk dimuat.
    private int visibleThreshold = 5; // jumlah item minimum yang ada, posisi gulir scroll anda saat ini belum memuat banyak.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager linearLayoutManager;

    public MoreListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = linearLayoutManager.getItemCount();
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // akhir sudah tercapai

            // lakukan sesuatu/kerjakan sesuatu
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
