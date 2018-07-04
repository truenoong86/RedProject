package com.example.trueno.redproject.homefragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trueno.redproject.CashActivity;
import com.example.trueno.redproject.EditProfileActivity;
import com.example.trueno.redproject.PlaceAutocompleteAdapter;
import com.example.trueno.redproject.R;
import com.example.trueno.redproject.RideDetailsActivity;
import com.example.trueno.redproject.models.PassengerRequest;
import com.example.trueno.redproject.models.PlaceInfo;
import com.example.trueno.redproject.models.Services;
import com.example.trueno.redproject.services.FetchAddressIntentService;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static java.lang.Double.parseDouble;

/**
 * A simple {@link Fragment} subclass.
 */

public class Home extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private int radius = 1;
    private Boolean driverFound = false;
    private Boolean requestBol = false;
    private String driverFoundId;
    private int promoDeduction = 0;
    private Marker mDriverMarker, mFoundDriverMarker;
    private DatabaseReference requestListRef;
    android.support.v7.widget.Toolbar close;
    LatLng pickupLocation;
    LatLng userLocation;
    ListView lvServices;
    private List<Services> mServicesList;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Boolean mLocationPermissionGranted;
    View mapView;
    LinearLayout cashLayout, remarksLayout, promoLayout, findingDriver;
    AutoCompleteTextView acCurrentLocation, acDestination;
    android.support.v7.widget.CardView afterChoosingLocation, singleLineCard, cvDriverInfo, cvAccepted;
    TextView tvDriverName, tvViewDetails, tvServiceType, tvServicePrice, tvPromo, tvRemarks;
    TextView tvPromoText,tvRemarksText;
    EditText etRemarks, etPromo;
    Button btnProceed, btnConfirmBooking, btnCancel, btnConfirm;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private PlaceInfo mPlace;
    private static final LatLngBounds SINGAPORE = new LatLngBounds(
            new LatLng(-35.0, 138.58), new LatLng(-34.9, 138.61));

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;

    boolean getDriversAroundStarted = false;

    List<Marker>markerList = new ArrayList<Marker>();

    private AddressResultReceiver mResultReceiver;

    ImageView ivPhone, ivMessage;

    public final class Constants {
        public static final int SUCCESS_RESULT = 0;
        public static final int FAILURE_RESULT = 1;
        public static final String PACKAGE_NAME =
                "com.google.android.gms.location.sample.locationaddress";
        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
        public static final String RESULT_DATA_KEY = PACKAGE_NAME +
                ".RESULT_DATA_KEY";
        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
                ".LOCATION_DATA_EXTRA";
    }

    public Home() {
        // Required empty public constructor
    }

    String userRemarks = "";
    int userPromo = 0;
    String userPaymentPref = "Cash";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();

        ListView servicesListView = (ListView) view.findViewById(R.id.lvServices);

        tvServiceType = (TextView) view.findViewById(R.id.tvServiceType);
        tvServicePrice = (TextView) view.findViewById(R.id.tvServicePrice);
        tvRemarks = (TextView) view.findViewById(R.id.tvRemarks);
        tvPromo = (TextView) view.findViewById(R.id.tvPromo);
        acCurrentLocation = (AutoCompleteTextView) view.findViewById(R.id.currentLocation);
        acDestination = (AutoCompleteTextView) view.findViewById(R.id.destination);
        cashLayout = (LinearLayout) view.findViewById(R.id.cashLayout);
        remarksLayout = (LinearLayout) view.findViewById(R.id.remarksLayout);
        promoLayout = (LinearLayout) view.findViewById(R.id.promoLayout);
        findingDriver = (LinearLayout) view.findViewById(R.id.findingDriver);
        afterChoosingLocation = (android.support.v7.widget.CardView) view.findViewById(R.id.afterChoosingLocation);
        singleLineCard = (android.support.v7.widget.CardView) view.findViewById(R.id.singleLineCard);
        cvDriverInfo = (android.support.v7.widget.CardView) view.findViewById(R.id.cvDriverInfo);
        cvAccepted = (android.support.v7.widget.CardView) view.findViewById(R.id.cvAccepted);
        btnProceed = (Button) view.findViewById(R.id.btnProceed);
        mServicesList = new ArrayList<>();
        mapFragment.getMapAsync(this);

        tvPromoText = view.findViewById(R.id.tvPromoText);
        tvRemarksText = view.findViewById(R.id.tvRemarksText);

        //Waiting for pickup navbar
        ivPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("trip")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String passengerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if(snapshot.child("passengerID").getValue() == passengerID){
                                        final String driverID = snapshot.getKey();
                                        FirebaseDatabase.getInstance().getReference().child("user").child("driver")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            if(driverID == snapshot.getKey()){
                                                                String phoneNo = snapshot.child("phone_number").getValue().toString();
                                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNo, null));
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });



                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

            }
        });


        singleLineCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_services, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                lvServices = (ListView) mView.findViewById(R.id.lvServices);


                ArrayList<Services> servicesList = new ArrayList<>();
                final String[] services = {"Tow (Accident)", "Tow (Breakdown)", "Tyre Mending", "Spare Tyre Replacement", "Battery Jump Start", "Battery Replacement", "Others"};
                final String[] day_price = {"60.00","165.00","45.00","55.00","55.00","55.00","70.00"};
                final String[] night_price = {"70.00","175.00","55.00","65.00","65.00","65.00","80.00"};
                for(int i=0;i<services.length; i++){
                    servicesList.add(new Services(services[i],day_price[i],night_price[i]));
                }
                Log.i("String services",servicesList+"");
                lvServices.setAdapter(new CustomListAdapter(getContext(), servicesList));

                lvServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView tvType = getActivity().findViewById(R.id.tvServiceType);
                        tvType.setText(services[i]);
                        if(checkIsDay()){
                            tvServicePrice.setText(day_price[i]);
                        } else {
                            tvServicePrice.setText(night_price[i]);
                            Double dayPrice = Double.parseDouble(day_price[i]);
                            Double discountedPrice = dayPrice-userPromo;
                            tvServicePrice.setText(discountedPrice.toString());
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        cashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), CashActivity.class));
                TextView tvPref = getView().findViewById(R.id.tvPaymentPref);
                Intent i = new Intent(getActivity(), CashActivity.class);
                i.putExtra("paymentPref", tvPref.getText().toString());
                startActivityForResult(i, 1005);
            }
        });

        remarksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_remarks, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                close = (android.support.v7.widget.Toolbar) dialog.findViewById(R.id.close);
                etRemarks = (EditText) dialog.findViewById(R.id.etRemarks);
                btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);

                etRemarks.setText(userRemarks);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvRemarks.setText(etRemarks.getText().toString());
                        if(etRemarks.getText().toString().length() > 0){
                            userRemarks = etRemarks.getText().toString();
                            tvRemarksText.setTextColor(Color.GREEN);
                        } else {
                            userRemarks = "";
                            tvRemarksText.setTextColor(Color.BLACK);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        promoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_promo, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                etPromo = (EditText) dialog.findViewById(R.id.etPromo);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvPromo.setText(etPromo.getText().toString());

                        if (tvPromo.getText().toString().equalsIgnoreCase("5OFF")) {
                            promoDeduction = 5;
                            userPromo = 5;
                            tvPromoText.setText("5OFF");
                            Toast.makeText(getContext(), "$5 discount coupon", Toast.LENGTH_SHORT).show();
                        } else if (tvPromo.getText().toString().equalsIgnoreCase("10OFF")) {
                            promoDeduction = 10;
                            userPromo = 10;
                            tvPromoText.setText("10OFF");
                            Toast.makeText(getContext(), "$10 discount coupon", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Invalid coupon", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_confirm_booking, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btnConfirmBooking = dialog.findViewById(R.id.btnConfirmBooking);

                final TextView tvVehicleNumber,tvVehicleMakeModel, tvFrom, tvTo, tvEditDetails;
                tvVehicleNumber = mView.findViewById(R.id.tvVehicleNumber);
                tvVehicleMakeModel = mView.findViewById(R.id.tvVehicleMakeModel);
                tvFrom = mView.findViewById(R.id.tvFrom);
                tvTo = mView.findViewById(R.id.tvTo);
                tvEditDetails = mView.findViewById(R.id.tvEditDetails);

                String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child("passenger").child(passengerId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            String make = dataSnapshot.child("vehicleMake").getValue().toString();
                            String model = dataSnapshot.child("vehicleModel").getValue().toString();
                            tvVehicleMakeModel.setText(make + " " + model);
                            tvVehicleNumber.setText(dataSnapshot.child("vehicleNumber").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                tvFrom.setText(acCurrentLocation.getText().toString());
                tvTo.setText(acDestination.getText().toString());
                tvEditDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getActivity(), EditProfileActivity.class);
                        i.putExtra("come from","btnProceed");
                        dialog.dismiss();
                        startActivityForResult(i,1006);

                    }
                });

                close = (android.support.v7.widget.Toolbar) dialog.findViewById(R.id.close);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findingDriver.setVisibility(View.VISIBLE);

                        pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        getClosestDriver();

                        dialog.dismiss();
                    }
                });
            }
        });

        acDestination.requestFocus();
        showKeyBoard();

        return view;
    }


    class CustomListAdapter extends ArrayAdapter<Services> {

        public CustomListAdapter(Context context, ArrayList<Services> services) {
            super(context, R.layout.custom_row, services);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater servicesInflater = LayoutInflater.from(getContext());
            View customView = servicesInflater.inflate(R.layout.custom_row, parent, false);

            Services singleServiceItem = getItem(position);

            TextView tvServices = (TextView) customView.findViewById(R.id.tvServices);
            tvServices.setText(singleServiceItem.getService());

            TextView tvPrices = (TextView) customView.findViewById(R.id.tvPrice);
            if(checkIsDay()){
                tvPrices.setText("$"+singleServiceItem.getDay_price());

            } else {
                tvPrices.setText("$"+singleServiceItem.getNight_price());
            }

            return customView;
        }
    }

    private void getClosestDriver() {
        Log.d("found test","test");
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("availableDriver");

        GeoFire geoFire = new GeoFire(driverLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.i("firebase called11","onKeyEntered");
                if (!driverFound) {
                    driverFound = true;
                    driverFoundId = key;

                    Log.d("Found driver", driverFoundId);

                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverFoundId);
                    String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("passengerRideId", passengerId);
                    driverRef.updateChildren(map);
                    try {
                        getDriverLocation();
                    } catch(MalformedURLException e) {

                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius++;
                    Log.d("testradius: ",radius+"");
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getDriverLocation() throws IOException {
        Log.d("getDriverLocation", driverFoundId);

        requestListRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest").child(driverFoundId).child("l");
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference passUserToDriverRef = FirebaseDatabase.getInstance().getReference().child("user").child("passenger");

        final String location = acDestination.getText().toString();
        Geocoder gc = new Geocoder(getContext());
        List<Address> list = gc.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();

        final double destinationLat = add.getLatitude();
        final double destinationLng = add.getLongitude();

        passUserToDriverRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PassengerRequest passengerRequestList = dataSnapshot.getValue(PassengerRequest.class);
                String name = passengerRequestList.getName();
                String vehicleNumber = passengerRequestList.getVehicleNumber();
                String vehicleModel = passengerRequestList.getVehicleModel();
                String pickupName = acCurrentLocation.getText().toString();
                Double pickupLatitude = mLastLocation.getLatitude();
                Double pickupLongitude = mLastLocation.getLongitude();
                String destinationName = acDestination.getText().toString();
                Double destinationLatitude = destinationLat;
                Double destinationLongitude = destinationLng;
                String serviceType = tvServiceType.getText().toString();
                String status = "pending";
                Double servicePrice = parseDouble(tvServicePrice.getText().toString());
                String remarks = tvRemarks.getText().toString();
                String promo = tvPromo.getText().toString();

                PassengerRequest insertPassengerRequest = new PassengerRequest(name, vehicleNumber, vehicleModel, pickupName, pickupLatitude, pickupLongitude, destinationName, destinationLatitude, destinationLongitude, serviceType, status, servicePrice, remarks, promo);
                mRef.child(driverFoundId).setValue(insertPassengerRequest);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference().child("availableDriver").child(driverFoundId).child("l");
        driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;

                    if(map.get(0) != null) {
                        locationLat = parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null) {
                        locationLng = parseDouble(map.get(1).toString());
                    }

                    final LatLng driverLatLng = new LatLng(locationLat, locationLng);
                    if(mDriverMarker != null) {
                        mDriverMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);

                    findingDriver.setVisibility(View.GONE);
                    singleLineCard.setVisibility(View.GONE);

                    btnProceed.setText("CANCEL BOOKING");


                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.dialog_found_driver, null);

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    close = (android.support.v7.widget.Toolbar) dialog.findViewById(R.id.close);
                    tvViewDetails = (TextView) dialog.findViewById(R.id.tvViewDetails);
                    btnCancel = (Button) dialog.findViewById(R.id.btnCancelBooking);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference cancelBookingRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest").child(driverFoundId);
                            cancelBookingRef.removeValue();
                            btnProceed.setVisibility(View.GONE);
                            cvAccepted.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    });

                    tvViewDetails.setText(Html.fromHtml("<u>View Details</u> "));

                    tvViewDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), RideDetailsActivity.class));
                        }
                    });

                    DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverFoundId);
                    mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            tvDriverName = (TextView) dialog.findViewById(R.id.tvDriverName);

                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                if (map.get("name")!=null) {
                                    tvDriverName.setText(map.get("name").toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    final DatabaseReference requestStatusRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest").child(driverFoundId).child("status");
                    final DatabaseReference nextDriverRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest").child(driverFoundId);

                    requestStatusRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                            final String status = dataSnapshot.getValue(String.class);

                            nextDriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshott) {
                                    if (status != null) {
                                        if (status.equals("rejected")) {
                                            Log.d("found test", "test");
                                            DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("availableDriver");
                                            driverFound = false;

                                            GeoFire geoFire = new GeoFire(driverLocation);
                                            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
                                            geoQuery.removeAllListeners();
                                            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                @Override
                                                public void onKeyEntered(String key, GeoLocation location) {
                                                    Log.i("called", "onKeyEntered");
                                                    if (!driverFound) {

                                                        Log.e("DATA" ," SnAPSHOt"+dataSnapshott.getKey()+"  drifer foound : "+driverFoundId);

                                                        if(dataSnapshott.getKey() != driverFoundId) {
                                                            Log.e("DATA ","NOT SAME");
                                                            driverFound = true;
                                                            driverFoundId = key;

                                                            Log.d("Found driver", driverFoundId);

                                                            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("user").child("driver").child(driverFoundId);
                                                            String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                            HashMap map = new HashMap();
                                                            map.put("passengerRideId", passengerId);
                                                            driverRef.updateChildren(map);
                                                            try {
                                                                getDriverLocation();
                                                            } catch (MalformedURLException e) {

                                                            } catch (IOException e) {

                                                            }
                                                        } else {
                                                            Log.e("DATA ","SAME");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onKeyExited(String key) {

                                                }

                                                @Override
                                                public void onKeyMoved(String key, GeoLocation location) {

                                                }

                                                @Override
                                                public void onGeoQueryReady() {
                                                    if (!driverFound) {
                                                        radius++;
                                                        Log.d("testradius: ", radius + "");
                                                        getClosestDriver();
                                                    }
                                                }

                                                @Override
                                                public void onGeoQueryError(DatabaseError error) {

                                                }
                                            });

                                        findingDriver.setVisibility(View.VISIBLE);
                                        } else if (status.equals("accepted")) {
                                            cvAccepted.setVisibility(View.VISIBLE);

                                            mMap.clear();
                                            markerList.clear();
                                            mFoundDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your driver").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_image)));
                                        }
                                    } else {
                                        Log.d("null", "This is null");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    btnProceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference cancelBookingRef = FirebaseDatabase.getInstance().getReference().child("passengerRequest").child(driverFoundId);
                            cancelBookingRef.removeValue();
                            btnProceed.setVisibility(View.GONE);
                            cvAccepted.setVisibility(View.GONE);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showKeyBoard() {
        Log.i("Called","showKeyBoard()");
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        mgr.showSoftInput(acDestination, InputMethodManager.SHOW_IMPLICIT);

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(acDestination, 0);

    }

    private void init() {
        Log.i("Called","init()");
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        acCurrentLocation = (AutoCompleteTextView) getView().findViewById(R.id.currentLocation);
        acDestination = (AutoCompleteTextView) getView().findViewById(R.id.destination);

        acDestination.setOnItemClickListener(mAutocompleteClickListener);

        AutocompleteFilter filter =
                new AutocompleteFilter.Builder().setCountry("SG").build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), Places.getGeoDataClient(getActivity()),
                SINGAPORE, filter);

        mGeoDataClient = Places.getGeoDataClient(getActivity());
        mPlaceDetectionClient= Places.getPlaceDetectionClient(getActivity());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        acDestination.setAdapter(mPlaceAutocompleteAdapter);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.

                // Turn on the My Location layer and the related control on the map.
                //updateLocationUI();
                getLocationPermission();

                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref =  database.getReference().child("availableDriver");

                //onChildAdded,onChildMoved,onChildChanged == Adding new availableDriver;
                //onChildChanged = change long or lat of availableDriver;
                //onChildRemoved = removing data
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.i("onChildAdded",dataSnapshot.toString());
                        if(dataSnapshot.exists()){

                            double locationLat = 0;
                            double locationLng = 0;

                            if(dataSnapshot.child("l").child("0").getValue() != null) {
                                locationLat = Double.parseDouble(dataSnapshot.child("l").child("0").getValue().toString());
                            }
                            if(dataSnapshot.child("l").child("1").getValue() != null) {
                                locationLng = Double.parseDouble(dataSnapshot.child("l").child("1").getValue().toString());
                            }

                            Log.i("LatLng",locationLat+" "+locationLng);

                            final GeoFire geoFire = new GeoFire(ref);
                            geoFire.setLocation(dataSnapshot.getKey(), new GeoLocation(locationLat, locationLng));
//
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.i("onChildChanged",dataSnapshot.toString());


                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("onChildRemoved",dataSnapshot.toString());
                        if (dataSnapshot.exists()) {

                            final GeoFire geoFire = new GeoFire(ref);
                            geoFire.removeLocation(dataSnapshot.getKey());

                        }


                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.i("onChildMoved",dataSnapshot.toString());

                        if(dataSnapshot.exists()){

                            final GeoFire geoFire = new GeoFire(ref);

                            double locationLat = 0;
                            double locationLng = 0;

                            if(dataSnapshot.child("l").child("0").getValue() != null) {
                                locationLat = Double.parseDouble(dataSnapshot.child("l").child("0").getValue().toString());
                            }
                            if(dataSnapshot.child("l").child("1").getValue() != null) {
                                locationLng = Double.parseDouble(dataSnapshot.child("l").child("1").getValue().toString());
                            }

                            Log.i("LatLng",locationLat+" "+locationLng);

                            geoFire.removeLocation(dataSnapshot.getKey());

                            geoFire.setLocation(dataSnapshot.getKey(), new GeoLocation(locationLat, locationLng));

//
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("onCancelled","cancelled");

                    }
                });

            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Called","onMapReady()");
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("error","No permissions");
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

        init();
    }


    private void updateLocationUI() {
        Log.i("Called","updateLocationUI()");
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        Log.i("Called","getLocationPermission()");
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.i("Called","onRequestPermissionResult()");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        Log.i("Called","getDeviceLocation()");
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    mLastLocation = location;
                                    Log.i("mLastLocation",location+"");
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(location.getLatitude(), location.getLongitude()), 17.0f  )
                                    );
                                    fetchAddress();

                                    if(!getDriversAroundStarted){
                                        displayDriversAround();
                                        displayCurrentDriversAround();
                                    }
                                } else {
                                    Log.i("mLastLocation","mLastLocation is null");
                                }
                            }
                        });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private synchronized void buildGoogleApiClient() {
        Log.i("Called","buildGoogleApiClient()");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Called","onLocationChanged()");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("Called","onConnected()");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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
            Log.i("Called","AdapterView.OnItemClickListener()");
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getStatus().isSuccess()) {
                                final Place myPlace = places.get(0);
                                LatLng queriedLocation = myPlace.getLatLng();
                                Log.v("Latitude is", "" + queriedLocation.latitude);
                                Log.v("Longitude is", "" + queriedLocation.longitude);
                            }
                            places.release();
                        }
                    });

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            afterChoosingLocation.setVisibility(View.VISIBLE);
            singleLineCard.setVisibility(View.VISIBLE);
            btnProceed.setVisibility(View.VISIBLE);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback  = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult (@NonNull PlaceBuffer places) {
            Log.i("Called","ResultCallback");
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Called","onPause()");

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Called","onStop()");
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    private void displayCurrentDriversAround(){
        FirebaseDatabase.getInstance().getReference().child("availableDriver")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DatabaseReference idRef = (FirebaseDatabase.getInstance().getReference("availableDriver"));
                        Log.i("idRef",snapshot.getValue().toString());

                        if(snapshot.exists()){

                            double locationLat = 0;
                            double locationLng = 0;

                            if(snapshot.child("l").child("0").getValue() != null) {
                                locationLat = Double.parseDouble(snapshot.child("l").child("0").getValue().toString());
                            }
                            if(snapshot.child("l").child("1").getValue() != null) {
                                locationLng = Double.parseDouble(snapshot.child("l").child("1").getValue().toString());
                            }

                            LatLng completeLatLng = new LatLng(locationLat,locationLng);
                            Log.i("LatLng",locationLat+" "+locationLng);

                            final GeoFire geoFire = new GeoFire(idRef);
                             geoFire.setLocation(snapshot.getKey(), new GeoLocation(locationLat, locationLng));
//                            mMap.addMarker(new MarkerOptions()`
//                                    .position(completeLatLng)
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//
//                            );
                        }


                        Log.i("idRef",snapshot.getValue().toString());

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }

    //constantly listens for setLocation() to be called
    //when setLocation is called, onKeyEntered will be called in this method
    //which is in charge of adding the marker
    //radius can be change in the geoQuery below to indicate range
    private void displayDriversAround(){

        Log.i("called","started displayDriversAround");
        getDriversAroundStarted = true;

        DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference("/availableDriver");
        userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        final GeoFire geoFire = new GeoFire(driverLocationRef);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(userLocation.latitude, userLocation.longitude), 1000);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.i("firebase called","onKeyEntered");
                //For loop to check if marker exists alr
                for(Marker markerIt:markerList){
                    if(markerIt.getTag().equals(key)){
                        return;
                    }
                }
                LatLng driverLocation = new LatLng(location.latitude,location.longitude);

                Marker mDriverMarker = mMap.addMarker(new MarkerOptions()
                        .position(driverLocation)
                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.car_image )))
                );


                //Tags the id of the driver in the marker, Remove after testing
                mDriverMarker.setTag(key);

                markerList.add(mDriverMarker);
            }

            @Override
            public void onKeyExited(String key) {
                Log.i("firebase called","onKeyExited");
                for(Marker markerIt : markerList){
                    if(markerIt.getTag().equals(key)){
                        markerIt.remove();;
                        markerList.remove(markerIt);
                        return;
                    }
                }
            }


            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                Log.i("firebase called","onKeyMoved");
                for(Marker markerIt : markerList){
                    if(markerIt.getTag().equals(key)){
                         markerIt.setPosition(new LatLng(location.latitude,location.longitude));
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {
                Log.i("firebase called","onGeoQueryReady()");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.i("firebase called",error.toString());

            }
        });
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "Null";
            }

            Log.i("mAddressOutput",mAddressOutput);
            acCurrentLocation.setText(mAddressOutput);

        }
    }

    public void fetchAddress(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("error","No permissions");
            return;
        } else {

            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            mLastLocation = location;

                            // In some rare cases the location returned can be null
                            if (mLastLocation == null) {
                                return;
                            }

                            if (!Geocoder.isPresent()) {
                                Toast.makeText(getActivity(),
                                        "No Geocoder",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Start service and update UI to reflect new location
                            startIntentService();
                        }
                    });
        }

    }

    public Boolean checkIsDay(){

        int dayFrom = 700;
        int dayTo = 2300;

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        Log.i("date now!",c.toString());
        c.setTime(date);
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        Log.i("time now!",t+"");
        boolean isDay = dayTo > dayFrom && t >= dayFrom && t <= dayTo || dayTo < dayFrom && (t >= dayFrom || t <= dayTo);
        return isDay;
    }

    protected void startIntentService() {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getContext().startService(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005) {
            if(resultCode == RESULT_OK) {
                String paymentPref = data.getStringExtra("paymentPref");
                TextView tvPref = getView().findViewById(R.id.tvPaymentPref);
                userPaymentPref = paymentPref;
                tvPref.setText(paymentPref);
            }
        } else if (requestCode == 1006) {
            btnProceed.performClick();
        }
    }
}