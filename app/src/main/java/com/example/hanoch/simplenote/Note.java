package com.example.hanoch.simplenote;

/**
 * Created by Hanoch on 18-Feb-16.
 */
public class Note {

    private long id;
    private String title;
    private String itemsList;
    private String lastUpdate;

    public Note(long id, String title, String itemsList, String lastUpdate) {
        this.id = id;
        this.title = title;
        this.itemsList = itemsList;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemsList() {
        return itemsList;
    }

    public void setItemsList(String itemsList) {
        this.itemsList = itemsList;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
