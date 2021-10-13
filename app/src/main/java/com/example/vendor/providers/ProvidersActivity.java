package com.example.vendor.providers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendor.MainActivity;
import com.example.vendor.MyType2;
import com.example.vendor.R;
import com.example.vendor.base.BaseActivity;
import com.example.vendor.database.DbHelper;
import com.example.vendor.database.FeedEntry;
import com.example.vendor.maps.MapsActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ProvidersActivity extends BaseActivity<ProvidersViewModel> {

    @BindView(R.id.editTextName)
    EditText name;
    @BindView(R.id.editTextEmail)
    EditText email;
    @BindView(R.id.editTextPhoneNo)
    EditText phoneNo;
    @BindView(R.id.editTextLocation)
    TextView location;
    @BindView(R.id.cardView4)
    CardView cardView;
    String latitude,longitute,providerAddress;
    int AUTOCOMPLETE_REQUEST_CODE=1;

    String id="";

    @Override
    protected ProvidersViewModel createViewModel() {
        return new ViewModelProvider(this).get(ProvidersViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        ButterKnife.bind(this);
        id=getIntent().getStringExtra("id");
        if (id!=null){
            List<MyType2>list=readDataForUpdate(id);
            if (list!=null){
                if (list.size()!=0) {
                    name.setText(list.get(0).name);
                    email.setText(list.get(0).email);
                    phoneNo.setText(list.get(0).phone);
                    location.setText(list.get(0).location);
                }
            }
        }
        try {
            String apiKey = getString(R.string.google_maps_key);
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
            }
        } catch (Exception ignored) {
        }
    }

    public List<MyType2> readDataForUpdate(String ide)
    {
        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + FeedEntry.Entry.TABLE_PROVIDERS
                +" WHERE "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+ide,null);

        cursor1.moveToFirst();
        List<MyType2> itemIds = new ArrayList<>();
        int count = cursor1.getCount();
        if (count > 0) {
            String names= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME));
            String id = cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID));
            String email=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS));
            String location=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION));
            String phone=cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER));

            String lati= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT));
            String longi= cursor1.getString(cursor1.getColumnIndexOrThrow(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG));

            latitude=lati;
            longitute=longi;
            MyType2 type=new MyType2();
            type.id=id;
            type.email=email;
            type.location=location;
            type.phone=phone;
            type.name=names;
            type.latitude=lati;
            type.longitude=longi;
            itemIds.add(type);
        }

        cursor1.close();
        return itemIds;
    }



    public void updateDatabase(String ide){
        String sname=name.getText().toString();
        String semail=email.getText().toString();
        String sphone=phoneNo.getText().toString();
        String slocation=location.getText().toString();

        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME,sname);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS,semail);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER,sphone);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION,slocation);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT,latitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG,longitute);

        db.update(FeedEntry.Entry.TABLE_PROVIDERS, values, FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = ?", new String[] {ide});
        db.close();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
        /*
        long newRowId=db.delete(FeedEntry.Entry.TABLE_PROVIDERS,FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+"="+id,null);

        db.execSQL("UPDATE table_name SET col_name='new_value' WHERE
                primary_column='primary_id'");

                db.execSQL("UPDATE "+FeedEntry.Entry.TABLE_PROVIDERS+" SET "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID
                        +"="+id+" WHERE "+FeedEntry.Entry.COLUMN_NAME_PROVIDER_ID+" = "+id,null);
        db.update(FeedEntry.Entry.TABLE_PROVIDERS,
                values,
                String.format("%s = ?", "primary_column"),
                new String[]{"primary_id"});


        UPDATE employees
        SET email = LOWER(
                firstname || "." || lastname || "@chinookcorp.com"
        );*/
    }

    @OnClick({R.id.next,R.id.cardView4,R.id.editTextLocation,R.id.backButton})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.next:
                if (validations()){
                    if (id!=null){
                        updateDatabase(id);
                    }else writeData();
                }


                break;
            case R.id.editTextLocation:
            case R.id.cardView4:
                location();
                break;
            case R.id.backButton:
                finish();
                break;
        }
    }
    public void requestFocus(View view){
        if (view!=null && view instanceof EditText){
            view.requestFocus();
        }
    }
    public void animateEditor(EditText editText) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(900);
        TranslateAnimation slider = new TranslateAnimation(0, 20, 0,0 );
        slider.setDuration(1000);
        //slider.setFillAfter(true);
        //slider.setFillAfter(false);
        //slider.setFillBefore(false);
        slider.setInterpolator(new CycleInterpolator(6));
        editText.startAnimation(slider);

    }
    public void animateEditor2(TextView editText) {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(900);
        TranslateAnimation slider = new TranslateAnimation(0, 20, 0,0 );
        slider.setDuration(1000);
        //slider.setFillAfter(true);
        //slider.setFillAfter(false);
        //slider.setFillBefore(false);
        slider.setInterpolator(new CycleInterpolator(6));
        editText.startAnimation(slider);

    }
    public boolean validations(){
        String sname=name.getText().toString();
        String semail=email.getText().toString();
        String sphone=phoneNo.getText().toString();

        String slocation=location.getText().toString();

        Pattern pat=Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        if (sname.matches("")){
            name.setError("name can't be null");
            animateEditor(name);
            requestFocus(name);
            return false;
        }else {
                if (semail.matches("")){
                    email.setError("email can't be null");
                    animateEditor(email);
                    requestFocus(email);
                    return false;
                }else if (!pat.matcher(semail).matches()) {
                    email.setError("enter valid email id");
                    animateEditor(email);
                    requestFocus(email);
                    return false;
                }else {
                    if (sphone.matches("")){
                        phoneNo.setError("Phone no. cant be null");
                        animateEditor(phoneNo);
                        requestFocus(phoneNo);
                        return false;
                    }else {
                        if (slocation.matches("")){
                            location.setError("location can't be null");
                            animateEditor2(location);
                            return false;
                        }else
                            return true;

                    }

                }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    location.setText(place.getAddress());

                    providerAddress=""+place.getAddress();
                    latitude = "" + place.getLatLng().latitude;
                    longitute = "" + place.getLatLng().longitude;

                    Log.e("test",latitude+" , "+longitute);
                } catch (Exception ignored) {
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    public void location(){
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    public void addLocation(){
        Intent i=new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);
    }

    public void writeData(){
        DbHelper dbHelper=new DbHelper(getApplicationContext());
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //insert values into database table
        ContentValues values=new ContentValues();
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_NAME,name.getText().toString());
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_EMAIL_ADDRESS,email.getText().toString());
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_PHONE_NUMBER,phoneNo.getText().toString());

        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LAT,latitude);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LONG,longitute);
        values.put(FeedEntry.Entry.COLUMN_NAME_PROVIDER_LOCATION,providerAddress);

        long newRowId=db.insert(FeedEntry.Entry.TABLE_PROVIDERS,null,values);
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
        //Toast.makeText(getApplicationContext(), "->"+newRowId, Toast.LENGTH_SHORT).show();

    }
}