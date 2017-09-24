package org.drunkcode.madbike.ui.home.model;

/**
 * Created by mun0n on 8/6/16.
 */
public class DataPollution {

    private String title;
    private String body;
    private int level;

    public DataPollution(String title, String body, int level) {
        this.title = title;
        this.body = body;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
