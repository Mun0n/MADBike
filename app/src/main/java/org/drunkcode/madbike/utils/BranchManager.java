package org.drunkcode.madbike.utils;

import android.content.Context;
import android.content.Intent;

import org.drunkcode.madbike.MADBikeApplication;
import org.drunkcode.madbike.ui.favorite.activity.FavoriteActivity;
import org.drunkcode.madbike.ui.home.activity.HomeActivity;
import org.drunkcode.madbike.ui.station.activity.StationActivity;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.LinkProperties;

public class BranchManager {

    private static final String SHARE_PARAM = "share_link";
    private static final String REFERRAL_PARAM = "referral";
    private static final String MADBIKE_LINK = "madbike_link";
    private static final String DESKTOP_URL = "$desktop_url";
    private static final String CANNONICAL_ID = "$canonical_identifier";

    private final Context applicationContext;
    private String DEEPLINK = "madbike://[[TYPE]]?id=[[ID]]";
    private String DESKTOP_URL_LINK = "https://www.madbikeapp.com/stations/[[STATION]]";

    public BranchManager(MADBikeApplication instance) {
        this.applicationContext = instance;
    }

    public void manageBranchLink(HomeActivity homeActivity, BranchUniversalObject branchUniversalObject) {
        if (branchUniversalObject.getMetadata().containsKey("+match_guaranteed") && Boolean.parseBoolean(branchUniversalObject.getMetadata().get("+match_guaranteed"))) {
            if (branchUniversalObject.getMetadata().containsKey("madbike_link") || branchUniversalObject.getMetadata().containsKey("madbike_link")) {
                String madbikeLink = branchUniversalObject.getMetadata().get("madbike_link");
                if (madbikeLink == null) {
                    madbikeLink = branchUniversalObject.getMetadata().get("madbike_link");
                }
                String id = madbikeLink.substring(madbikeLink.indexOf("=") + 1);
                Intent stationIntent = new Intent(homeActivity, StationActivity.class);
                stationIntent.putExtra(FavoriteActivity.EXTRA_STATION_ID, id);
                homeActivity.startActivity(stationIntent);

            }
        }
    }

    private static class Singleton {
        private static final BranchManager INSTANCE = new BranchManager(MADBikeApplication.getInstance());
    }

    public static BranchManager getInstance() {
        return BranchManager.Singleton.INSTANCE;
    }

    public void createStationLink(String id, Branch.BranchLinkCreateListener listener) {
        BranchUniversalObject branchUniversalObject =
                new BranchUniversalObject().setCanonicalIdentifier(id);

        String finalLink = DEEPLINK.replace("[[TYPE]]", "stations");
        String desktopLink = DESKTOP_URL_LINK.replace("[[STATION]]", id);
        finalLink = finalLink.replace("[[ID]]", id);

        LinkProperties linkProperties = new LinkProperties().setChannel(SHARE_PARAM)
                .setFeature(REFERRAL_PARAM)
                .addControlParameter(MADBIKE_LINK, finalLink).addControlParameter(DESKTOP_URL, desktopLink).addControlParameter(CANNONICAL_ID, desktopLink);
        branchUniversalObject.generateShortUrl(applicationContext, linkProperties, listener);
    }
}
