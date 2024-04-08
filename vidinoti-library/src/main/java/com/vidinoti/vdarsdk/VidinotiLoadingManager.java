package com.vidinoti.vdarsdk;

public class VidinotiLoadingManager {

    public interface LoadingInterface {
        void setLoadingViewVisible(boolean visible);
    }

    private int loadingCounter = 0;
    private double loadingPercentage = 1.0;

    private final LoadingInterface loadingInterface;

    public VidinotiLoadingManager(LoadingInterface loadingInterface) {
        this.loadingInterface = loadingInterface;
    }

    public void startLoading() {
        loadingCounter++;
        updateVisibility();
    }

    public void stopLoading() {
        loadingCounter--;
        if (loadingCounter < 0) {
            loadingCounter = 0;
        }
        updateVisibility();
    }

    public void setProgress(double percent) {
        loadingPercentage = Math.min(percent, 1.0);
        updateVisibility();
    }

    private void updateVisibility() {
        loadingInterface.setLoadingViewVisible(loadingCounter > 0 || loadingPercentage < 1.0);
    }

}
