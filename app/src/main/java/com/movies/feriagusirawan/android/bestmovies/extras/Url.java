package com.movies.feriagusirawan.android.bestmovies.extras;

import com.movies.feriagusirawan.android.bestmovies.BuildConfig;


public class Url {

    // Main URL
    public static final String BASE_URL = "http://api.themoviedb.org/3";
    public static final String PARAM_DISC = "/discover";
    public static final String PARAM_MOV = "/movie";
    public static final String PARAM_QUESTION_MARK = "?";
    public static final String PARAM_PAGE_NO = "page=";
    public static final String PARAM_SORT = "&sort_by=";
    public static final String PARAM_ADULT = "&include_adult=";
    public static final String PARAM_API = "&api_key=";
    public static final String KEY = BuildConfig.API_KEY;

    // Images URL
    public static final String POSTERS_URL = "http://image.tmdb.org/t/p/";

    // Trailers Params
    public static final String PARAM_TR_API = "?api_key=";
    public static final String PARAM_TRAIL = "/videos";
        public static final String PARAM_THUMB = "/default.jpg";

    // Reviews Params
    public static final String PARAM_REVS = "/reviews";

    // YouTube URL
    public static final String VID_URL = "https://www.youtube.com/watch?v=";
    public static final String THUMB_URL = "http://img.youtube.com/vi/";
}
