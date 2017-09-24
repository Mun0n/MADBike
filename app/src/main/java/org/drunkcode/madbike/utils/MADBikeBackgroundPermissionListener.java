package org.drunkcode.madbike.utils;

import android.os.Handler;
import android.os.Looper;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;

import org.drunkcode.madbike.ui.home.fragment.MapFragment;

/**
 * Created by mun0n on 21/9/16.
 */

public class MADBikeBackgroundPermissionListener extends MADBikePermissionListener {

    private Handler handler = new Handler(Looper.getMainLooper());

    public MADBikeBackgroundPermissionListener(MapFragment mapFragment) {
        super(mapFragment);
    }

    @Override
    public void onPermissionGranted(final PermissionGrantedResponse response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MADBikeBackgroundPermissionListener.super.onPermissionGranted(response);
            }
        });
    }

    @Override
    public void onPermissionDenied(final PermissionDeniedResponse response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MADBikeBackgroundPermissionListener.super.onPermissionDenied(response);
            }
        });
    }

    @Override
    public void onPermissionRationaleShouldBeShown(final PermissionRequest permission,
                                                   final PermissionToken token) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                MADBikeBackgroundPermissionListener.super.onPermissionRationaleShouldBeShown(
                        permission, token);
            }
        });
    }
}


