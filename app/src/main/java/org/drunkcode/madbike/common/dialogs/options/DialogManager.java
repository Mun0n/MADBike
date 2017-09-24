package org.drunkcode.madbike.common.dialogs.options;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogManager implements DialogBuilder, DialogInterface.OnClickListener {

    private final AlertDialog.Builder alertDialogBuilder;
    private DialogListener dialogListener;
    private AlertDialog alertDialog;

    public DialogManager(Context context, DialogListener dialogListener) {
        alertDialogBuilder = new AlertDialog.Builder(context);
        this.dialogListener = dialogListener;
    }

    @Override
    public void showPositiveDialog(int idTitle, int idMessage, int idPositiveText) {
        alertDialogBuilder.setTitle(idTitle).setMessage(idMessage).setPositiveButton(idPositiveText, this);
        alertDialog = alertDialogBuilder.show();
    }

    @Override
    public void showPositiveAndNegativeDialog(int idTitle, int idMessage, int idPositiveText, int idNegativeText) {
        alertDialogBuilder.setTitle(idTitle).setMessage(idMessage).setPositiveButton(idPositiveText, this).setNegativeButton(idNegativeText, this);
        alertDialog = alertDialogBuilder.show();
    }

    @Override
    public void hideDialog() {
        alertDialog.hide();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                dialogListener.onPositivePressed();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogListener.onNegativePressed();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                dialogListener.onNeutralPressed();
                break;
        }
    }
}
