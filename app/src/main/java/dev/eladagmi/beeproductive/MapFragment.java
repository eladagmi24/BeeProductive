package dev.eladagmi.beeproductive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private CallBack_Map callBack_map;
    private GoogleMap map;

    public void setCallBackMap(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.google_map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        return view;
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
        map.addMarker(new MarkerOptions().position(mark).title("Task created here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.104236455127015, 34.87987851707526), 20.0f));
    }
    private void moveCamera (String lat, String lon) {
        LatLng mark = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        map.addMarker(new MarkerOptions().position(mark).title("Task created here"));
        map.moveCamera(CameraUpdateFactory.
                newLatLngZoom(mark,1));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)), 20.0f));


    }
    public void onClicked(String lat, String lon) {
        if(lat != null && lon != null)
            moveCamera(lat, lon);
    }

}
