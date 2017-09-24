package org.drunkcode.madbike.utils;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.drunkcode.madbike.ui.home.fragment.MapFragment;

/**
 * Created by mun0n on 22/2/16.
 */
public class MADBikePermissionListener implements PermissionListener{

    private final MapFragment mapFragment;

    public MADBikePermissionListener(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
        mapFragment.showPermissionGranted(response.getPermissionName());
    }

    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
        mapFragment.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
    }

    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                             PermissionToken token) {
        mapFragment.showPermissionRationale(token);
    }
}
