package com.jrmitchell.drivefinance.utils;

public abstract class UpdateableFile {
    private final String fileId;
    private final String initialContent;
    protected boolean success = false;

    public UpdateableFile(String fileId, String initialContent) {
        this.fileId = fileId;
        this.initialContent = initialContent;
    }

    public String getContent() {
        return initialContent;
    }

    public boolean success() {
        return success;
    }

    public abstract void update(String newContent);
}
