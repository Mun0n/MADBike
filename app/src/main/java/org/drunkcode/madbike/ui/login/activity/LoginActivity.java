package org.drunkcode.madbike.ui.login.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseActivity;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.common.notification.NotificationManager;
import org.drunkcode.madbike.preferences.Preferences;
import org.drunkcode.madbike.ui.home.activity.HomeActivity;
import org.drunkcode.madbike.ui.login.model.LoginModel;
import org.drunkcode.madbike.ui.login.model.RecoverModel;
import org.drunkcode.madbike.ui.login.model.UserModel;
import org.drunkcode.madbike.ui.login.presenter.LoginPresenter;
import org.drunkcode.madbike.ui.login.response.LoginResponse;
import org.drunkcode.madbike.ui.login.view.LoginView;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    private static final String DNI = "dni";
    private static final String PASSWORD = "password";
    private static final String CHECK = "check";
    public static final int LOGIN_RESULT = 10001;
    @InjectView(R.id.documentEditText)
    EditText documentEditText;
    @InjectView(R.id.documenttextInputLayout)
    TextInputLayout documenTextInputLayout;
    @InjectView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;
    @InjectView(R.id.passwordEditText)
    public EditText passwordEditText;

    private LoginPresenter loginPresenter;
    private LoadingManager loadingManager;
    private Dialog d;
    private NotificationManager notificationManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.tutorial_toolbar);
    }

    @Override
    protected boolean getActivityHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Login");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        loginPresenter = new LoginPresenter(this);
        loadingManager = new LoadingManager(this);
        notificationManager = new NotificationManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginPresenter.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DNI, documentEditText.getText().toString());
        outState.putString(PASSWORD, passwordEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        documentEditText.setText(savedInstanceState.getString(DNI));
        passwordEditText.setText(savedInstanceState.getString(PASSWORD));
    }

    @OnClick(R.id.registerTextView)
    public void onRegisterPressed() {
        AnalyticsManager.getInstance().trackSignUp();
        String url = "http://u.bicimad.com/inscribete/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.passwordTextView)
    public void onPasswordPressed() {
        d = new Dialog(LoginActivity.this);
        d.setContentView(R.layout.dialog_recover_password);
        final TextInputLayout recoverDocumentTextInputLayout = (TextInputLayout) d.findViewById(R.id.documentInputLayout);
        final EditText recoverDocumentEditText = (EditText) d.findViewById(R.id.documentEditText);
        final TextInputLayout recoverEmailTextInputLayout = (TextInputLayout) d.findViewById(R.id.emailInputLayout);
        final EditText recoverEmailEditText = (EditText) d.findViewById(R.id.emailEditText);
        Button sendButton = (Button) d.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDocument(recoverDocumentEditText, recoverDocumentTextInputLayout) && checkEmail(recoverEmailEditText, recoverEmailTextInputLayout)) {
                    try {
                        d.dismiss();
                        RecoverModel recoverModel = new RecoverModel(recoverDocumentEditText.getText().toString().toUpperCase(), recoverEmailEditText.getText().toString());
                        loginPresenter.doRecoverPassword(recoverModel);
                    } catch (Throwable throwable) {
                        notificationManager.showMessage(getString(R.string.error_generic));
                    }
                }
            }
        });
        d.show();
    }

    private boolean checkEmail(EditText emailEditText, TextInputLayout emailInputLayout) {
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailInputLayout.setErrorEnabled(true);
            emailInputLayout.setError(getString(R.string.error_no_email));
            if (emailEditText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                emailInputLayout.setErrorEnabled(false);
            } else {
                emailInputLayout.setErrorEnabled(true);
                emailInputLayout.setError(getString(R.string.error_no_email));
                if (emailEditText.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                return false;
            }
        }
        return true;
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonPressed() {
        if (checkDocument(documentEditText, documenTextInputLayout) && checkPassword()) {
            try {
                LoginModel l = new LoginModel(documentEditText.getText().toString().toUpperCase(), passwordEditText.getText().toString());
                loginPresenter.doLogin(l);
            } catch (Throwable throwable) {
                //TODO: Control error
            }
        }
    }

    private boolean checkPassword() {
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordTextInputLayout.setErrorEnabled(true);
            passwordTextInputLayout.setError(getString(R.string.error_no_password));
            if (passwordEditText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            return false;
        } else {
            passwordTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean checkDocument(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(R.string.error_no_dni));
            if (editText.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    @Override
    public void getLoginResponse(LoginResponse response) {
        switch (response.getSuccess()) {
            case 1:
                UserModel userModel = response.getUserModel()[0];
                if (userModel != null) {
                    Gson gson = new Gson();
                    Preferences.getInstance(this).setIdAuth(userModel.getIdAuth());
                    Preferences.getInstance(this).setUserData(gson.toJson(userModel));
                    Preferences.getInstance(this).setUserDni(userModel.getDni());
                    Preferences.getInstance(this).setEmail(userModel.getEmail());
                    setResult(Activity.RESULT_OK);
                    AnalyticsManager.getInstance().trackLoginResult(true);
                    finish();
                } else {
                    AnalyticsManager.getInstance().trackLoginResult(false);
                    notificationManager.showMessage(getString(R.string.error_login));
                }
                break;
            default:
                AnalyticsManager.getInstance().trackLoginResult(false);
                notificationManager.showMessage(response.getMessage());
                break;
        }
    }

    @Override
    public void onRecoverResponse(BaseResponse baseResponse) {
        d.dismiss();
        switch (baseResponse.getSuccess()) {
            case 1:
                notificationManager.showMessage(getString(R.string.recover_password_ok));
                break;
            default:
                notificationManager.showMessage(baseResponse.getMessage());
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
}
