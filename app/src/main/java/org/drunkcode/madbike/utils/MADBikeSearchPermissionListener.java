package org.drunkcode.madbike.utils;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.drunkcode.madbike.ui.search.activity.SearchActivity;

/**
 * Created by mun0n on 25/9/16.
 */

public class MADBikeSearchPermissionListener implements PermissionListener {

    private final SearchActivity searchActivity;

    public MADBikeSearchPermissionListener(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }

    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
        searchActivity.showPermissionGranted(response.getPermissionName());
    }

    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
        searchActivity.showPermissionDenied(response.getPermissionName(), response.isPermanentlyDenied());
    }

    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                             PermissionToken token) {
        searchActivity.showPermissionRationale(token);
    }
}
