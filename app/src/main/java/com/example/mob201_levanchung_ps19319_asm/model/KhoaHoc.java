package com.example.mob201_levanchung_ps19319_asm.model;

import java.io.Serializable;

public class KhoaHoc implements Serializable {
    private int maKH;
    private String tenKH;
    private String lichHoc;
    private String lichThi;
    private boolean check;

    public KhoaHoc(int maKH, String tenKH, String lichHoc, String lichThi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.lichHoc = lichHoc;
        this.lichThi = lichThi;
    }

    public KhoaHoc(int maKH, String tenKH, String lichHoc, String lichThi, boolean check) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.lichHoc = lichHoc;
        this.lichThi = lichThi;
        this.check = check;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getLichHoc() {
        return lichHoc;
    }

    public void setLichHoc(String lichHoc) {
        this.lichHoc = lichHoc;
    }

    public String getLichThi() {
        return lichThi;
    }

    public void setLichThi(String lichThi) {
        this.lichThi = lichThi;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
