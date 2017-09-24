package org.drunkcode.madbike.ui.login.model;

import com.google.gson.annotations.SerializedName;

import org.drunkcode.madbike.utils.JSONUtils;

public class UserModel {

    @SerializedName(JSONUtils.DNI)
    private String dni;
    @SerializedName(JSONUtils.NAME)
    private String name;
    @SerializedName(JSONUtils.FIRST_SURNAME)
    private String firstSurname;
    @SerializedName(JSONUtils.SECOND_SURNAME)
    private String secondSurname;
    @SerializedName(JSONUtils.DATE_BIRTH)
    private String dateBirth;
    @SerializedName(JSONUtils.EMAIL)
    private String email;
    @SerializedName(JSONUtils.TELEPHONE)
    private String phoneNumber;
    @SerializedName(JSONUtils.ADDRESS)
    private String address;
    @SerializedName(JSONUtils.TOWN)
    private String town;
    @SerializedName(JSONUtils.PROVINCE)
    private String province;
    @SerializedName(JSONUtils.POSTAL_CODE)
    private String postalCode;
    @SerializedName(JSONUtils.ID_AUTH)
    private String idAuth;

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

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(String idAuth) {
        this.idAuth = idAuth;
    }
}
