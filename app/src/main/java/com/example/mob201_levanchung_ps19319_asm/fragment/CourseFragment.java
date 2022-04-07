package com.example.mob201_levanchung_ps19319_asm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mob201_levanchung_ps19319_asm.ListCourseActivity;
import com.example.mob201_levanchung_ps19319_asm.MyCourseActivity;
import com.example.mob201_levanchung_ps19319_asm.R;

public class CourseFragment extends Fragment {
    CardView cvRegister, cvCourse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course,container,false);

        cvRegister = view.findViewById(R.id.cvRegister);
        cvCourse = view.findViewById(R.id.cvCourse);

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListCourseActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        cvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCourseActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().overridePendingTransition(R.anim.anim_activity_enter, R.anim.anim_activity_exit);
    }
}
