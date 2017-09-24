package org.drunkcode.madbike.ui.profile.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.common.dialogs.options.DialogListener;
import org.drunkcode.madbike.common.dialogs.options.DialogManager;
import org.drunkcode.madbike.common.notification.NotificationManager;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.profile.model.ChangePasswordModel;
import org.drunkcode.madbike.ui.profile.presenter.ProfilePresenter;
import org.drunkcode.madbike.ui.profile.response.ChangePasswordResponse;
import org.drunkcode.madbike.ui.profile.response.ProfileResponse;
import org.drunkcode.madbike.ui.profile.view.ProfileView;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements DialogListener, ProfileView {

    private DialogManager dialogManager;
    private LoadingManager loadingManager;
    private ProfilePresenter profilePresenter;
    private NotificationManager notificationManager;

    @InjectView(R.id.nameTextView)
    TextView nameTextView;
    @InjectView(R.id.emailTextView)
    TextView emailTextView;
    @InjectView(R.id.moneyTextView)
    TextView moneyTextView;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(android.R.id.empty)
    TextView emptyTextView;
    private Dialog d;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.profile);
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initManagers();
        AnalyticsManager.getInstance().trackContentView("Profile");
        setResult(CommonStatusCodes.CANCELED);
        try {
            profilePresenter.getProfileData();
        } catch (Throwable throwable) {
            scrollView.setVisibility(View.GONE);
            emptyTextView.setText(getString(R.string.error_generic));
        }
    }

    private void initManagers() {
        dialogManager = new DialogManager(this, this);
        loadingManager = new LoadingManager(this);
        profilePresenter = new ProfilePresenter(this);
        notificationManager = new NotificationManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        profilePresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        profilePresenter.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(CommonStatusCodes.CANCELED);
                finish();
                return true;
            case R.id.action_logout:
                AnalyticsManager.getInstance().trackLogout();
                dialogManager.showPositiveAndNegativeDialog(R.string.logout_title, R.string.logout_message, android.R.string.ok, android.R.string.cancel);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositivePressed() {
        setResult(CommonStatusCodes.SUCCESS);
        finish();
    }

    @Override
    public void onNegativePressed() {
        dialogManager.hideDialog();
    }

    @Override
    public void onNeutralPressed() {

    }

    @Override
    public void getProfileResponse(ProfileResponse profileResponse) {
        if (profileResponse.getSuccess() == 1 || profileResponse.getSuccess() == 2) {
            String name = profileResponse.getUserProfileModel()[0].getName();
            name = name.substring(0, 1) + name.substring(1).toLowerCase();
            String firstSurname = profileResponse.getUserProfileModel()[0].getFirstSurname();
            firstSurname = firstSurname.substring(0, 1) + firstSurname.substring(1).toLowerCase();
            String secondSurname = profileResponse.getUserProfileModel()[0].getSecondSurname();
            secondSurname = secondSurname.substring(0, 1) + secondSurname.substring(1).toLowerCase();
            String completeName = name + " " + firstSurname + " " + secondSurname;
            nameTextView.setText(completeName);
            emailTextView.setText(profileResponse.getUserProfileModel()[0].getEmail());
        }
        switch (profileResponse.getSuccess()) {
            case 1:
                moneyTextView.setText(profileResponse.getUserProfileModel()[0].getMoney() + " €");
                break;
            case 2:
                moneyTextView.setTextColor(Color.RED);
                moneyTextView.setText(getString(R.string.no_money_showed));
                break;
            default:
                //TODO:CONTROL ERROR
                break;
        }
    }

    @Override
    public void getChangePasswordResponse(ChangePasswordResponse changePasswordResponse) {
        switch (changePasswordResponse.getSuccess()) {
            case 1:
                Preferences.getInstance(this).setIdAuth(changePasswordResponse.getIdAuth());
                new AlertDialog.Builder(this).setTitle(R.string.change_title_ok).setMessage(R.string.change_message_ok).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
                break;
            default:
                d.dismiss();
                notificationManager.showMessage(getString(R.string.no_change_password));
                break;
        }
    }

    @Override
    public void showLoading(int idTitle, int idMessage) {
        loadingManager.showLoadingDialog(idTitle, idMessage);
    }

    @Override
    public void hideLoading() {
        loadingManager.hideLoading();
    }

    @Override
    public void showError() {

    }

    @OnClick(R.id.changePasswordButton)
    public void onChangePasswordPressed() {
        AnalyticsManager.getInstance().trackChangePassword();
        d = new Dialog(ProfileActivity.this);
        d.setContentView(R.layout.dialog_change_password);
        final TextInputLayout oldTextInputLayout = (TextInputLayout) d.findViewById(R.id.oldInputLayout);
        final EditText oldEditText = (EditText) d.findViewById(R.id.oldEditText);
        final TextInputLayout newTextInputLayout = (TextInputLayout) d.findViewById(R.id.newInputLayout);
        final EditText newEditText = (EditText) d.findViewById(R.id.newEditText);
        final TextInputLayout repeatTextInputLayout = (TextInputLayout) d.findViewById(R.id.repeatInputLayout);
        final EditText repeatEditText = (EditText) d.findViewById(R.id.repeatEditText);
        Button sendButton = (Button) d.findViewById(R.id.sendChangeButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNoEmpty(oldEditText, oldTextInputLayout) && checkNoEmpty(repeatEditText, repeatTextInputLayout) && checkNoEmpty(newEditText, newTextInputLayout) && checkEquals(newEditText, repeatEditText, repeatTextInputLayout)) {
                    try {
                        ChangePasswordModel changePasswordModel = new ChangePasswordModel(oldEditText.getText().toString(), newEditText.getText().toString());
                        profilePresenter.changePassword(changePasswordModel);
                        d.dismiss();
                    } catch (Throwable throwable) {
                        notificationManager.showMessage(getString(R.string.error_generic_send));
                        d.dismiss();
                    }
                }
            }
        });
        d.show();
    }

    private boolean checkEquals(EditText newEditText, EditText repeatEditText, TextInputLayout repeatInputLayout) {
        if (newEditText.getText().toString().contentEquals(repeatEditText.getText().toString())) {
            return true;
        } else {
            setErrorStyle(repeatEditText, repeatInputLayout, R.string.error_not_equals);
            return false;
        }
    }

    private boolean checkNoEmpty(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            setErrorStyle(editText, textInputLayout, R.string.error_no_password);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void setErrorStyle(EditText editText, TextInputLayout textInputLayout, int idMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(idMessage));
        requestFocus(editText);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
