package com.example.beadando;

public class User {

    private String felID;

    private String nev;

    private String telfon;

    public User( String felID, String nev,String telfon) {
        this.felID = felID;
        this.nev = nev;
        this.telfon = telfon;
    }

    public String getFelID() {
        return felID;
    }

    public String getNev() {
        return nev;
    }

    public String getTelfon() {
        return telfon;
    }
}
