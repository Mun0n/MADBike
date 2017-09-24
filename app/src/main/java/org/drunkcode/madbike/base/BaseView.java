package org.drunkcode.madbike.base;

public interface BaseView {

    void showLoading(int idTitle, int idMessage);

    void hideLoading();

    void showError();
}
