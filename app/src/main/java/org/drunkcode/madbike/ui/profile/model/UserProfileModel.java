package org.drunkcode.madbike.ui.profile.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class UserProfileModel {

    @SerializedName(JSONUtils.DNI)
    private String dni;
    @SerializedName(JSONUtils.NAME)
    private String name;
    @SerializedName(JSONUtils.FIRST_SURNAME)
    private String firstSurname;
    @SerializedName(JSONUtils.SECOND_SURNAME)
    private String secondSurname;
    @SerializedName(JSONUtils.EMAIL)
    private String email;
    @SerializedName(JSONUtils.MONEY)
    private String money;
    @SerializedName(JSONUtils.STATE)
    private String state;
    @SerializedName(JSONUtils.ACTIVATE)
    private String activate;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }
}
