package com.example.mob201_levanchung_ps19319_asm.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.KhoaHoc;

import java.util.ArrayList;

public class unRegisterAndRegisterCourseServices extends IntentService {

    public unRegisterAndRegisterCourseServices() {
        super("unRegisterAndRegisterCourseServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            DbHelper dbHelper = new DbHelper(getApplicationContext());

            Intent i = new Intent("unRegisterAndRegisterCourseServices");
            String action = intent.getAction();
            String maSV = intent.getStringExtra("maSV");
            int maKH = intent.getIntExtra("maKH",-1);
            boolean isRegister = intent.getBooleanExtra("isRegister",false);

            if(isRegister){
                dbHelper.registerCourse(maSV,maKH); //đăng ký
            }else{
                dbHelper.unRegisterCourse(maSV, maKH); //huỷ đăng ký
            }

            ArrayList<KhoaHoc> allCourse = dbHelper.getAllCourse();
            ArrayList<KhoaHoc> allCourseRegister = dbHelper.getAllCourseRegister(maSV);
            dbHelper.close();
            i.putExtra("allCourse",allCourse);
            i.putExtra("allCourseRegister",allCourseRegister);
            i.putExtra("action",action);
            i.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }
}
