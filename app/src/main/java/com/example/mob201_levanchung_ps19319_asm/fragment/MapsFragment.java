package com.example.mob201_levanchung_ps19319_asm.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.animation.AccelerateDecelerateInterpolator;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mob201_levanchung_ps19319_asm.MainActivity;
import com.example.mob201_levanchung_ps19319_asm.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    FloatingActionButton map_type_FAB;
    ConstraintLayout map_type_selection;
    View map_type_default_background, map_type_satellite_background, map_type_terrain_background;
    ImageButton map_type_default, map_type_satellite, map_type_terrain;
    TextView textView13, map_type_default_text, map_type_satellite_text, map_type_terrain_text;
    SearchView svSearchLocation;
    int num = 0;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_branch_map);
        map_type_FAB = view.findViewById(R.id.map_type_FAB);
        map_type_selection = view.findViewById(R.id.map_type_selection);
        map_type_default_background = view.findViewById(R.id.map_type_default_background);
        map_type_satellite_background = view.findViewById(R.id.map_type_satellite_background);
        map_type_terrain_background = view.findViewById(R.id.map_type_terrain_background);
        map_type_default = view.findViewById(R.id.map_type_default);
        map_type_satellite = view.findViewById(R.id.map_type_satellite);
        map_type_terrain = view.findViewById(R.id.map_type_terrain);
        textView13 = view.findViewById(R.id.textView13);
        map_type_default_text = view.findViewById(R.id.map_type_default_text);
        map_type_satellite_text = view.findViewById(R.id.map_type_satellite_text);
        map_type_terrain_text = view.findViewById(R.id.map_type_terrain_text);
        svSearchLocation = view.findViewById(R.id.svSearchLocation);

        svSearchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = svSearchLocation.getQuery().toString();

                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList.size() == 0){
                        Toast.makeText(getContext(), "Không tìm thấy " +query, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng UCA = new LatLng(10.606838, 106.666038);
        mMap.addMarker(new MarkerOptions().position(UCA).title("HOUSE")).showInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UCA, 17));

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(getContext(), "Turn on permisson Location please!", Toast.LENGTH_LONG).show();
        }
        mMap.setPadding(0,940,5,0);


        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
            map_type_satellite_background.setVisibility(View.VISIBLE);
            map_type_satellite_text.setTextColor(Color.BLUE);
        } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
            map_type_terrain_background.setVisibility(View.VISIBLE);
            map_type_terrain_text.setTextColor(Color.BLUE);
        } else {
            map_type_default_background.setVisibility(View.VISIBLE);
            map_type_default_text.setTextColor(Color.BLUE);
        }

        map_type_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) {
                    showMapType(true);
                } else {
                    showMapType(false);
                }

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (map_type_FAB.getVisibility() == View.INVISIBLE) {
                    Animator animation = ViewAnimationUtils.createCircularReveal(map_type_selection,
                            map_type_selection.getWidth() - (map_type_FAB.getWidth() / 2),
                            map_type_FAB.getHeight() / 2,
                            map_type_FAB.getWidth() / 2f,
                            Float.parseFloat(String.valueOf(map_type_selection.getWidth())));
                    animation.setDuration(200);
//                animation.setInterpolator((TimeInterpolator) new AccelerateDecelerateInterpolator());

                    animation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            map_type_selection.setVisibility(View.VISIBLE);
                        }
                    });

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            map_type_FAB.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    animation.start();
                }
            }
        });

        map_type_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_type_default_background.setVisibility(View.VISIBLE);
                map_type_satellite_background.setVisibility(View.INVISIBLE);
                map_type_terrain_background.setVisibility(View.INVISIBLE);
                map_type_default_text.setTextColor(Color.BLUE);
                map_type_satellite_text.setTextColor(Color.parseColor("#808080"));
                map_type_terrain_text.setTextColor(Color.parseColor("#808080"));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        map_type_satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_type_default_background.setVisibility(View.INVISIBLE);
                map_type_satellite_background.setVisibility(View.VISIBLE);
                map_type_terrain_background.setVisibility(View.INVISIBLE);
                map_type_default_text.setTextColor(Color.parseColor("#808080"));
                map_type_satellite_text.setTextColor(Color.BLUE);
                map_type_terrain_text.setTextColor(Color.parseColor("#808080"));
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        map_type_terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_type_default_background.setVisibility(View.INVISIBLE);
                map_type_satellite_background.setVisibility(View.INVISIBLE);
                map_type_terrain_background.setVisibility(View.VISIBLE);
                map_type_default_text.setTextColor(Color.parseColor("#808080"));
                map_type_satellite_text.setTextColor(Color.parseColor("#808080"));
                map_type_terrain_text.setTextColor(Color.BLUE);
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
    }

    private void showMapType(boolean bool) {
        if (bool == true) {
            Animator animation = ViewAnimationUtils.createCircularReveal(map_type_selection,
                    map_type_selection.getWidth() - (map_type_FAB.getWidth() / 2),
                    map_type_FAB.getHeight() / 2,
                    map_type_FAB.getWidth() / 2f,
                    Float.parseFloat(String.valueOf(map_type_selection.getWidth())));
            animation.setDuration(200);
//                animation.setInterpolator((TimeInterpolator) new AccelerateDecelerateInterpolator());

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    map_type_selection.setVisibility(View.VISIBLE);
                }
            });
            animation.start();
            map_type_FAB.setVisibility(View.VISIBLE);
            num = 1;
        } else {
            Animator animation = ViewAnimationUtils.createCircularReveal(map_type_selection,
                    0,
                    0,
                    0,
                    0);
            animation.setDuration(200);
//                animation.setInterpolator((TimeInterpolator) new AccelerateDecelerateInterpolator());

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    map_type_selection.setVisibility(View.INVISIBLE);
                }
            });
            animation.start();
            map_type_FAB.setVisibility(View.VISIBLE);
            num = 0;
        }
    }


}