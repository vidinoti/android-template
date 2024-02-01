package com.vidinoti.tabapp;

import com.vidinoti.vdarsdk.MapBoxConfig;
import com.vidinoti.vdarsdk.MapBoxFragment;

public class MapFragment extends MapBoxFragment {

    @Override
    public MapBoxConfig getConfiguration() {
        return new MapBoxConfig("put mapbox token here");
    }

}
