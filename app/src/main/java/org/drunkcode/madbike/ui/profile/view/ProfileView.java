package org.drunkcode.madbike.ui.profile.view;

import org.drunkcode.madbike.base.BaseView;
import org.drunkcode.madbike.ui.profile.response.ChangePasswordResponse;
import org.drunkcode.madbike.ui.profile.response.ProfileResponse;

public interface ProfileView extends BaseView{

    void getProfileResponse(ProfileResponse profileResponse);

    void getChangePasswordResponse(ChangePasswordResponse changePasswordResponse);
}
