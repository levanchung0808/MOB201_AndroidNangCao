package com.example.mob201_levanchung_ps19319_asm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.mob201_levanchung_ps19319_asm.R;
import com.example.mob201_levanchung_ps19319_asm.ReadNewsActivity;
import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.TinTuc;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class NewsAdapter extends ArrayAdapter<TinTuc> implements Filterable {

    ArrayList<TinTuc> arrTinTuc;
    ArrayList<TinTuc> arrTinTucFiltered;
    Context context;
    DbHelper dbHelper;
    TextView txtTitle, txtDate;
    RelativeLayout relativeRSS;
    ImageView ivImage;

    public NewsAdapter(ArrayList<TinTuc> arrTinTuc, Context context, DbHelper dbHelper) {
        super(context, 0, arrTinTuc);
        this.arrTinTuc = arrTinTuc;
        this.arrTinTucFiltered = arrTinTuc;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return arrTinTucFiltered.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_news, null);
        }
        TinTuc tinTuc = arrTinTucFiltered.get(position);
        if (tinTuc != null) {
            txtTitle = v.findViewById(R.id.txtTitle);
            txtDate = v.findViewById(R.id.txtDate);
            ivImage = v.findViewById(R.id.ivImage);
            relativeRSS = v.findViewById(R.id.relativeRSS);
//            "Sat, 12 Feb 2022 00:36:15 +0700"
            String date = tinTuc.getDate();
            String str1 = date.substring(5,16);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
            LocalDate date1 = LocalDate.parse(str1, formatter);

            txtTitle.setText(tinTuc.getTitle());
            txtDate.setText(date1.toString());
            Glide.with(context).load(tinTuc.getImg()).override(400, 300).centerCrop().placeholder(R.drawable.error_image).into(ivImage);
        }

        relativeRSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadNewsActivity.class);
                intent.putExtra("linkNews", arrTinTucFiltered.get(position).getLink());
                context.startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = arrTinTuc.size();
                    filterResults.values = arrTinTuc;

                } else {
                    List<TinTuc> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (TinTuc itemsModel : arrTinTuc) {
                        String title = itemsModel.getTitle();
                        if (title.toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel);
                        }else if(removeAccent(title).toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrTinTucFiltered = (ArrayList<TinTuc>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}