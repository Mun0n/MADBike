package org.drunkcode.madbike.ui.login.model;

public class RecoverModel {

    private String document;
    private String email;

    public RecoverModel() {
    }

    public RecoverModel(String document, String email) {
        this.document = document;
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
