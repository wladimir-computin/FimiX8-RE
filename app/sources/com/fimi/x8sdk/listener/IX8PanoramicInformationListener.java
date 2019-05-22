package com.fimi.x8sdk.listener;

import com.fimi.x8sdk.dataparser.AckPanoramaPhotographType;

public interface IX8PanoramicInformationListener {
    void onPanoramicInformationChange(AckPanoramaPhotographType ackPanoramaPhotographType);
}
