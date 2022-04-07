package com.example.mob201_levanchung_ps19319_asm.model;

public class SinhVien {
    private String username;
    private String password;

    public SinhVien() {
    }

    public SinhVien(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
