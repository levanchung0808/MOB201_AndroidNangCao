package com.example.mob201_levanchung_ps19319_asm.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.KhoaHoc;

import java.util.ArrayList;

public class GetAllCourseServices extends IntentService {

    public GetAllCourseServices() {
        super("GetAllCourseServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            DbHelper dbHelper = new DbHelper(getApplicationContext());

            Intent i = new Intent("GetAllCourseServices");
            String action = intent.getAction();
            String maSV = intent.getStringExtra("maSV");
            boolean isMine = intent.getBooleanExtra("isMine",false);

            if(!isMine){
                ArrayList<KhoaHoc> allCourse = dbHelper.getAllCourse();
                i.putExtra("allCourse", allCourse);
            }

            ArrayList<KhoaHoc> allCourseRegister = dbHelper.getAllCourseRegister(maSV);
            i.putExtra("allCourseRegister",allCourseRegister);

            dbHelper.close();

            i.putExtra("action",action);
            i.putExtra("resultCode", Activity.RESULT_OK);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }
}
