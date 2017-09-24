package org.drunkcode.madbike.utils;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.drunkcode.madbike.ui.home.fragment.MapFragment;

import java.util.List;

/**
 * Created by mun0n on 21/9/16.
 */

public class MADBikeMultiplePermissionListener implements MultiplePermissionsListener {

    private final MapFragment mapFragment;

    public MADBikeMultiplePermissionListener(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
            mapFragment.showPermissionGranteAdviseBike(response.getPermissionName());
        }

        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
            mapFragment.showPermissionDeniedAdviseBike(response.getPermissionName(), response.isPermanentlyDenied());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                   PermissionToken token) {
        mapFragment.showPermissionRationaleAdviseBike(token);
    }
}


