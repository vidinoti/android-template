package com.vidinoti.vdarsdk;

public interface VidinotiSynchronizationProgressListener {

    /**
     * Called for indicating the synchronization progress
     * @param progress the progress percentage (a value between 0 and 100)
     */
    void onSyncProgress(int progress);

    /**
     * Called if an error happened during the synchronization
     * @param error the error message
     */
    void onSyncError(String error);
}
