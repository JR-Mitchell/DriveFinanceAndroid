package com.jrmitchell.drivefinance.utils;

public class UpdateableFile {
    private final String fileId;
    private final String initialContent;

    public UpdateableFile(String fileId, String initialContent) {
        this.fileId = fileId;
        this.initialContent = initialContent;
    }

    public String getContent() {
        return initialContent;
    }

    public void update(String newContent, SuccessFailureCallback<Void> callback) {
        DriveUtils.getSingletonInstance().getFileTextData(fileId)
                .addOnSuccessListener(s -> {
                    if (s.equals(initialContent)) {
                        DriveUtils.getSingletonInstance().setFileTextData(fileId,newContent)
                                .addOnSuccessListener(callback::success)
                                .addOnFailureListener(e -> callback.failure());
                    } else {
                        callback.failure();
                    }
                })
                .addOnFailureListener(e -> callback.failure());
    }
}
