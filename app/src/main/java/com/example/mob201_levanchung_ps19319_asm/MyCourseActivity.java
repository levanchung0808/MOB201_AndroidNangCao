package com.example.mob201_levanchung_ps19319_asm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mob201_levanchung_ps19319_asm.adapter.GetAllCourseAdapter;
import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.KhoaHoc;
import com.example.mob201_levanchung_ps19319_asm.services.GetAllCourseServices;

import java.util.ArrayList;

public class MyCourseActivity extends AppCompatActivity {

    ArrayList<KhoaHoc> arrKhoaHoc = new ArrayList<>();
    DbHelper dbHelper;
    ListView listViewAllCourseRegister;
    ImageView ivBack;
    Button btnCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        listViewAllCourseRegister = findViewById(R.id.listViewAllCourseRegister);
        ivBack = findViewById(R.id.ivBack);
        btnCapture = findViewById(R.id.btnCapture);
        dbHelper = new DbHelper(this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //đăng ký IntentFilter cho 2 cái service
        IntentFilter filterGetAllCourse = new IntentFilter("GetAllCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseReceiverRegister, filterGetAllCourse);

        IntentFilter filterRegisterCourse = new IntentFilter("unRegisterAndRegisterCourseServices");
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCourseReceiverRegister, filterRegisterCourse);

        Intent intent = new Intent(this, GetAllCourseServices.class);
        //thay đổi mssv phù hợp với chức năng đăng nhập/đăng ký
        intent.putExtra("maSV",LoginActivity.USER);
        intent.putExtra("isMine",false);
        intent.setAction("GetAllCourseServices");
        startService(intent);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDetailCourse();
            }
        });
    }

    private final BroadcastReceiver getAllCourseReceiverRegister = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String action = intent.getStringExtra("action");
                switch (action){
                    case "GetAllCourseServices":
                    case "unRegisterAndRegisterCourseServices":
                        arrKhoaHoc.clear();
                        ArrayList<KhoaHoc> alCourseRegister = (ArrayList<KhoaHoc>) intent.getSerializableExtra("allCourseRegister");

                        for(KhoaHoc khdk : alCourseRegister){
                            khdk.setCheck(true);
                            arrKhoaHoc.add(khdk);
                        }


                        GetAllCourseAdapter adapter = new GetAllCourseAdapter(arrKhoaHoc, MyCourseActivity.this, dbHelper);
                        listViewAllCourseRegister.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getAllCourseReceiverRegister);
    }

    private void ShareDetailCourse(){
        String content = "Đã chia sẻ những khoá học đã đăng ký\n\n";
        for(KhoaHoc khdk : arrKhoaHoc){
            content = content.concat(khdk.getTenKH())
                    .concat("\n Lịch học: ").concat(khdk.getLichHoc())
                    .concat("\n Lịch thi: ").concat(khdk.getLichThi())
                    .concat("\n------------------------------------\n");
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_SUBJECT,"Chia sẽ khoá học");
        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent,"Chia sẽ nội dung thông qua"));
    }
}