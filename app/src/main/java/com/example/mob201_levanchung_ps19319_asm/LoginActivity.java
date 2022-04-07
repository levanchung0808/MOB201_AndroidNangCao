package com.example.mob201_levanchung_ps19319_asm;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mob201_levanchung_ps19319_asm.database.DbHelper;
import com.example.mob201_levanchung_ps19319_asm.model.SinhVien;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilTenDangNhap, tilMatKhau;
    TextInputEditText edtTenDangNhap, edtMatKhau;
    CheckBox chkLuuMatKhau;
    Button btnDangNhap;
    TextView tvDangKy;
    LoginButton login_button;
    CallbackManager callbackManager;

    DbHelper dbHelper;

    public static String USER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tilTenDangNhap = findViewById(R.id.tilTenDangNhap);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuMatKhau = findViewById(R.id.chkLuuMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvDangKy = findViewById(R.id.tvDangKy);
        login_button = findViewById(R.id.login_button);

        //xin quyền location
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        //check connect network
        if(!isConnectNetwork(LoginActivity.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Kết nối mạng để load RSS")
                    .setCancelable(false)
                    .setPositiveButton("Kết nối", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        readRememberUser();

        dbHelper = new DbHelper(this);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _user = edtTenDangNhap.getText().toString();
                String _pass = edtMatKhau.getText().toString();

                SinhVien sv = new SinhVien(_user, _pass);
                if (dbHelper.checkLogin(sv)) {
                    Toasty.success(LoginActivity.this, R.string.success_message, Toasty.LENGTH_SHORT, true).show();
                    rememberUser();
                    USER = _user;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toasty.error(LoginActivity.this, R.string.error_message, Toasty.LENGTH_SHORT, true).show();
                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code – khi đăng nhập thành công
                AccessToken accessToken = loginResult.getAccessToken();
                getUserProfile(accessToken);

                Toasty.success(LoginActivity.this, R.string.success_message, Toasty.LENGTH_SHORT, true).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code – khi người dùng hủy đăng nhập
            }

            @Override
            public void onError(FacebookException exception) {
                // App code – khi đăng nhập thất bại
            }
        });

        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean isConnectNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    private void rememberUser(){
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        String _user = edtTenDangNhap.getText().toString();
        String _password = edtMatKhau.getText().toString();
        boolean _cbLuu = chkLuuMatKhau.isChecked();
        if(!_cbLuu){
            edit.clear();
        }else{
            edit.putString("USERNAME",_user);
            edit.putString("PASSWORD",_password);
            edit.putBoolean("REMEMBER",_cbLuu);
        }
        edit.commit();
    }

    private void readRememberUser(){
        SharedPreferences pref = getSharedPreferences("USER_FILE",MODE_PRIVATE);
        String user = pref.getString("USERNAME","");
        String pass = pref.getString("PASSWORD","");
        boolean cbLuu = pref.getBoolean("REMEMBER",false);
        edtTenDangNhap.setText(user);
        edtMatKhau.setText(pass);
        chkLuuMatKhau.setChecked(cbLuu);
    }

    private void getUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    USER = email;
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.e("getUserProfile",name + " - " + email + " - " + image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}