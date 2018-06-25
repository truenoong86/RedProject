package com.example.trueno.redproject.homefragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.trueno.redproject.CashActivity;
import com.example.trueno.redproject.PlaceAutocompleteAdapter;
import com.example.trueno.redproject.R;
import com.example.trueno.redproject.models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    View mapView;
    LinearLayout cashLayout, remarksLayout, promoLayout;
    AutoCompleteTextView currentLocation, destination;
    android.support.v7.widget.CardView afterChoosingLocation, singleLineCard, multiLineCard;
    Button btnProceed;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds SINGAPORE = new LatLngBounds(
            new LatLng(-35.0, 138.58), new LatLng(-34.9, 138.61));

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        currentLocation = (AutoCompleteTextView) view.findViewById(R.id.currentLocation);
        cashLayout = (LinearLayout) view.findViewById(R.id.cashLayout);
        remarksLayout = (LinearLayout) view.findViewById(R.id.remarksLayout);
        promoLayout = (LinearLayout) view.findViewById(R.id.promoLayout);
        afterChoosingLocation = (android.support.v7.widget.CardView) view.findViewById(R.id.afterChoosingLocation);
        singleLineCard = (android.support.v7.widget.CardView) view.findViewById(R.id.singleLineCard);
        multiLineCard = (android.support.v7.widget.CardView) view.findViewById(R.id.multiLineCard);
        btnProceed = (Button) view.findViewById(R.id.btnProceed);
        mapFragment.getMapAsync(this);

        singleLineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiLineCard.setVisibility(View.VISIBLE);
            }
        });

        cashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CashActivity.class));
            }
        });

        remarksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_remarks, null);

                mBuilder.setView(mView);
            }
        });

        promoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_promo, null);

                mBuilder.setView(mView);
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_confirm_booking, null);

                mBuilder.setView(mView);
            }
        });

        return view;
    }

    private void init() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        currentLocation = (AutoCompleteTextView) getView().findViewById(R.id.currentLocation);
        destination = (AutoCompleteTextView) getView().findViewById(R.id.destination);

        destination.setOnItemClickListener(mAutocompleteClickListener);

        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("SG").build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), Places.getGeoDataClient(getActivity(), null),
                SINGAPORE, filter);

        destination.setAdapter(mPlaceAutocompleteAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 30);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        float la = (float) location.getLatitude() ;
        float lo = (float) location.getLongitude();

        LatLng TutorialsPoint = new LatLng(la, lo);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        init();
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*
    -------------------------------Google places API autocomplete suggestions-------------------------------
    */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            afterChoosingLocation.setVisibility(View.VISIBLE);
            singleLineCard.setVisibility(View.VISIBLE);
            btnProceed.setVisibility(View.VISIBLE);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
        if(!places.getStatus().isSuccess()){
            places.release();
            return;
        }
        final Place place = places.get(0);

        try {
            mPlace = new PlaceInfo();
            mPlace.setName(place.getName().toString());
            mPlace.setAddress(place.getAddress().toString());
            mPlace.setPhoneNumber(place.getPhoneNumber().toString());
            mPlace.setId(place.getId());
            mPlace.setWebsiteUri(place.getWebsiteUri());
            mPlace.setLatlng(place.getLatLng());
            mPlace.setRating(place.getRating());
            mPlace.setAttributions(place.getAttributions().toString());
        } catch (NullPointerException e) {

        }

        LatLng moveCameraToDestination = new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moveCameraToDestination));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(place.getViewport().getCenter().latitude, place.getViewport().getCenter().longitude))
                .title(place.getName().toString()));
        places.release();
        }
    };

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            currentLocation.setText(locationAddress);
        }
    }
}
