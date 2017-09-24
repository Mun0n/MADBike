package org.drunkcode.madbike.ui.home.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.drunkcode.madbike.R;
import org.drunkcode.madbike.base.BaseFragment;
import org.drunkcode.madbike.base.BaseResponse;
import org.drunkcode.madbike.common.dialogs.loading.LoadingManager;
import org.drunkcode.madbike.common.dialogs.options.DialogListener;
import org.drunkcode.madbike.common.dialogs.options.DialogManager;
import org.drunkcode.madbike.common.notification.NotificationManager;
import org.drunkcode.madbike.ui.home.presenter.IncidencePresenter;
import org.drunkcode.madbike.ui.home.view.IncidenceView;
import org.drunkcode.madbike.utils.tracking.AnalyticsManager;

import butterknife.InjectView;
import butterknife.OnClick;

public class IncidenceFragment extends BaseFragment implements DialogListener, IncidenceView {

    @InjectView(R.id.incidenceTextInputLayout)
    TextInputLayout incidenceTextInputLayout;
    @InjectView(R.id.nameTextInputLayout)
    TextInputLayout nameTextInputLayout;
    @InjectView(R.id.addressTextInputLayout)
    TextInputLayout addressTextInputLayout;
    @InjectView(R.id.stationTextInputLayout)
    TextInputLayout stationTextInputLayout;
    @InjectView(R.id.commentsTextInputLayout)
    TextInputLayout commentsTextInputLayout;

    @InjectView(R.id.incidenceEditText)
    EditText incidenceEditText;
    @InjectView(R.id.nameEditText)
    EditText nameEditText;
    @InjectView(R.id.addressEditText)
    EditText addressEditText;
    @InjectView(R.id.stationEditText)
    EditText stationEditText;
    @InjectView(R.id.commentsEditText)
    EditText commentsEditText;

    private IncidencePresenter incidencePresenter;
    private LoadingManager loadingManager;
    private DialogManager dialogManager;
    private NotificationManager notificationManager;

    public static IncidenceFragment newInstance(Bundle arguments) {
        IncidenceFragment f = new IncidenceFragment();
        if (arguments != null) {
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_incidence;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnalyticsManager.getInstance().trackContentView("Incidence");
        incidencePresenter = new IncidencePresenter(this);
        loadingManager = new LoadingManager(getActivity());
        dialogManager = new DialogManager(getActivity(), this);
        notificationManager = new NotificationManager(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        incidencePresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        incidencePresenter.onPause();
    }

    @OnClick(R.id.sendButton)
    public void onSendPressed() {
        if (checkDataFill()) {

        }
    }

    private boolean checkDataFill() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(nameEditText, nameTextInputLayout, R.string.error_no_name_incidence);
            return false;
        } else {
            nameTextInputLayout.setErrorEnabled(false);
        }
        if (addressEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(addressEditText, addressTextInputLayout, R.string.error_no_address);
            return false;
        } else {
            addressTextInputLayout.setErrorEnabled(false);
        }
        if (stationEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(stationEditText, stationTextInputLayout, R.string.error_no_station);
            return false;
        } else {
            stationTextInputLayout.setErrorEnabled(false);
        }
        if (incidenceEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(incidenceEditText, incidenceTextInputLayout, R.string.error_no_incidence);
            return false;
        } else {
            incidenceTextInputLayout.setErrorEnabled(false);
        }
        if (commentsEditText.getText().toString().trim().isEmpty()) {
            setErrorStyle(commentsEditText, commentsTextInputLayout, R.string.error_no_comment);
            return false;
        } else {
            commentsTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onPositivePressed() {
        dialogManager.hideDialog();
        incidenceEditText.setText("");
        nameEditText.setText("");
        addressEditText.setText("");
        stationEditText.setText("");
        commentsEditText.setText("");
    }

    @Override
    public void onNegativePressed() {

    }

    @Override
    public void onNeutralPressed() {

    }

    @Override
    public void getIncidenceResponse(BaseResponse baseResponse) {
        switch (baseResponse.getSuccess()) {
            case 1:
                dialogManager.showPositiveDialog(R.string.incidence_ok_title, R.string.incidence_ok_message, android.R.string.ok);
                break;
            default:
                notificationManager.showMessage(getString(R.string.incidence_ko));
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

    private void setErrorStyle(EditText editText, TextInputLayout textInputLayout, int idMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(idMessage));
        requestFocus(editText);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
