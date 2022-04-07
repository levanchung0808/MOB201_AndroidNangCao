package com.example.mob201_levanchung_ps19319_asm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.mob201_levanchung_ps19319_asm.R;

import java.io.File;
import java.io.FileOutputStream;

public class SocialFragment extends Fragment {

    ImageView imgShare;
    TextView txtImage;
    Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        EditText edtContent = view.findViewById(R.id.edtContent);
        Button btnShare = view.findViewById(R.id.btnShare);
        LinearLayout linearShare = view.findViewById(R.id.linearShare);
        txtImage = view.findViewById(R.id.txtImage);
        imgShare = view.findViewById(R.id.imgShare);

        linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                chooseImage.launch(i);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Đã chia sẻ");

                if (bitmap != null) {
                    Uri uri = getmageToShare(bitmap);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/png");
                } else {
                    intent.setType("text/plain");
                }

                startActivity(Intent.createChooser(intent, "Chia sẻ nội dung thông qua"));
            }
        });

        return view;
    }

    ActivityResultLauncher<Intent> chooseImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            imgShare.setImageURI(selectedImageUri);
                            txtImage.setText("Lựa chọn lại hình ảnh");

                            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgShare.getDrawable();
                            bitmap = bitmapDrawable.getBitmap();
                        }
                    }
                }
            });

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getContext().getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(getContext(), "com.dinhnt.asm_androidnc", file);
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}
