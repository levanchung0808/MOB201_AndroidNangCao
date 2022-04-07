package com.example.mob201_levanchung_ps19319_asm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mob201_levanchung_ps19319_asm.LoginActivity;
import com.example.mob201_levanchung_ps19319_asm.R;
import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.KhoaHoc;
import com.example.mob201_levanchung_ps19319_asm.services.unRegisterAndRegisterCourseServices;

import java.util.ArrayList;

public class GetAllCourseAdapter extends BaseAdapter {

    ArrayList<KhoaHoc> arrKhoaHoc = new ArrayList<>();
    Context context;
    DbHelper dbHelper;

    public GetAllCourseAdapter(ArrayList<KhoaHoc> arrKhoaHoc, Context context, DbHelper dbHelper) {
        this.arrKhoaHoc = arrKhoaHoc;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return arrKhoaHoc.size();
    }

    @Override
    public Object getItem(int position) {
        return arrKhoaHoc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrKhoaHoc.get(position).getMaKH();
    }

    class ViewOfItem{
        TextView txtTenKH, txtNoiDungKH;
        Button btnKH;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewOfItem viewOfItem;
        if(convertView == null){
            viewOfItem = new ViewOfItem();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            convertView = inflater.inflate(R.layout.item_course,null);
            viewOfItem.txtTenKH = convertView.findViewById(R.id.txtTenKH);
            viewOfItem.txtNoiDungKH = convertView.findViewById(R.id.txtNoiDungKH);
            viewOfItem.btnKH = convertView.findViewById(R.id.btnKH);

            convertView.setTag(viewOfItem);
        }else{
            viewOfItem = (ViewOfItem) convertView.getTag();
        }

        viewOfItem.txtTenKH.setText(arrKhoaHoc.get(position).getTenKH());
        String noiDung = arrKhoaHoc.get(position).getLichHoc() + " | Thi ngày " + arrKhoaHoc.get(position).getLichThi();
        viewOfItem.txtNoiDungKH.setText(noiDung);

        if(arrKhoaHoc.get(position).isCheck()){
            viewOfItem.btnKH.setText("Huỷ đăng ký khoá học");
            viewOfItem.btnKH.setBackgroundColor(Color.parseColor("#BF381D"));
        }else{
            viewOfItem.btnKH.setText("Đăng ký khoá học");
            viewOfItem.btnKH.setBackgroundColor(Color.parseColor("#1884D6"));
        }

        viewOfItem.btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, unRegisterAndRegisterCourseServices.class);
                //thay đổi maSV phù hợp với chức năng đăng nhập/đăng ký
                intent.putExtra("maSV", LoginActivity.USER);
                intent.putExtra("maKH",arrKhoaHoc.get(position).getMaKH());

                //đăng ký khoá học - true | huỷ đăng ký khoá học - false
                intent.putExtra("isRegister", !arrKhoaHoc.get(position).isCheck());

                intent.setAction("unRegisterAndRegisterCourseServices");
                context.startService(intent);
            }
        });

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_list_view);
        convertView.startAnimation(animation);
        return convertView;
    }
}
