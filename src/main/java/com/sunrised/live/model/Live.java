package com.sunrised.live.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Live {
    private final StringProperty livePageUrl;
    private final StringProperty path;
    private final StringProperty saveName;
    private final StringProperty status;

    /**
     * Default constructor.
     */
    public Live() {
        this(null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param livePageUrl
     * @param path
     * @Param saveName
     */
    public Live(String livePageUrl, String path, String saveName, String status) {
        this.livePageUrl = new SimpleStringProperty(livePageUrl);
        this.path = new SimpleStringProperty(path);
        this.saveName = new SimpleStringProperty(saveName);
        this.status = new SimpleStringProperty(status);
    }

    public String getLivePageUrl() {
        return livePageUrl.get();
    }

    public StringProperty livePageUrlProperty() {
        return livePageUrl;
    }

    public void setLivePageUrl(String livePageUrl) {
        this.livePageUrl.set(livePageUrl);
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getSaveName() {
        return saveName.get();
    }

    public StringProperty saveNameProperty() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName.set(saveName);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
