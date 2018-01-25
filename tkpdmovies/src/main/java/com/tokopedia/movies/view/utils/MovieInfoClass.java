package com.tokopedia.movies.view.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by naveengoyal on 12/29/17.
 */

public class MovieInfoClass {

    @SerializedName("director")
    private String directorName;

    @SerializedName("language")
    private String movieLanguage;

    @SerializedName("starring")
    private String starring;

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public void setMovieLanguage(String movieLanguage) {
        this.movieLanguage = movieLanguage;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }
}
