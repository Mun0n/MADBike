package org.drunkcode.madbike.common.dialogs.options;

public interface DialogBuilder {

    void showPositiveDialog(int idTitle, int idMessage, int idPositiveText);

    void showPositiveAndNegativeDialog(int idTitle, int idMessage, int idPositiveText, int idNegativeText);

    void hideDialog();

}
