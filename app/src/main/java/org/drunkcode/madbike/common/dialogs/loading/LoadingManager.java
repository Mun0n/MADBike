package org.drunkcode.madbike.common.dialogs.loading;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingManager implements LoadingBuilder {

    private Context context;
    ProgressDialog progressDialog;

    public LoadingManager(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    public void showLoadingDialog(int idTitle, int idMessage) {
        progressDialog.setTitle(context.getString(idTitle));
        progressDialog.setMessage(context.getString(idMessage));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.hide();
    }
}
