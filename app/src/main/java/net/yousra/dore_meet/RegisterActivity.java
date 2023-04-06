package net.yousra.dore_meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,LocationListener  {

    private static final int PERMS_CALL_ID = 1234 ;
    private LocationManager lm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private TextView banner, localisation;
    private EditText name, email, password, age;
    private Spinner specialite;
    private Button enregistrer;
    private ProgressBar pb;



    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.banner);
        enregistrer = (Button) findViewById(R.id.btnregister);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        age = (EditText) findViewById(R.id.age);
        specialite = (Spinner) findViewById(R.id.spinner);
        localisation = (TextView) findViewById(R.id.local);
        pb = (ProgressBar) findViewById(R.id.pb);
        banner.setOnClickListener(this);
        enregistrer.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner: {
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.btnregister: {
                registerUser();
                break;
            }


        }
    }

    private void registerUser() {

        String mail = email.getText().toString().trim();
        String nom = name.getText().toString().trim();
        String motpasse = password.getText().toString().trim();
        String ag = age.getText().toString().trim();
        String spec = specialite.getSelectedItem().toString().trim();
        String local = localisation.getText().toString();



        if (nom.isEmpty()) {
            name.setError("Nom et Prénom indispensable ");
            name.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            email.setError("adresse email indispensable ");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Adresse Email invalide ");
            email.requestFocus();
            return;
        }
        if (motpasse.isEmpty()) {
            password.setError("Mot de passe indispensable ");
            password.requestFocus();
            return;
        }
        if(motpasse.length()<6){
            password.setError("mot de passe invalide, il doit contenir plus que 6 caractères");
            password.requestFocus();
            return;
        }
        if (ag.isEmpty()) {
            age.setError("nom et prenom indispensable ");
            age.requestFocus();
            return;
        }
        if (spec.equals("specialité")) {
            Toast.makeText(this, "le choix de la specialité est indispensable", Toast.LENGTH_SHORT).show();
            specialite.requestFocus();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,motpasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                  Musicien  user = new Musicien(nom,mail,motpasse,ag,spec,localisation.getText().toString());
                  db.getInstance().getReference("Users")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      Toast.makeText(RegisterActivity.this, "Enregistrement effectué", Toast.LENGTH_SHORT).show();
                                      pb.setVisibility(View.GONE);
                                      Intent i = new Intent(RegisterActivity.this, ProfileActivity.class);
                                      i.putExtra("nom",nom);
                                      startActivity(i);
                                  }else{
                                      Toast.makeText(RegisterActivity.this, "Enregistremnt non effectué", Toast.LENGTH_SHORT).show();
                                      pb.setVisibility(View.GONE);
                                  }
                              }
                          });


                }else{
                    Toast.makeText(RegisterActivity.this, "Enregistremnt non effectué", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }
        });



    }

    protected void onResume() {
        super.onResume();
        checkPermession();
    }

    private void checkPermession() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMS_CALL_ID);
            return;
        }

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        if(lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,0,this);
        if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== PERMS_CALL_ID)
            checkPermession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(lm != null)
            lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        /*String lat,longi,adr,ville;*/
        String ville;

        try {
            List<Address> adresse= geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(),1);
            /*
            lat= ("Latitude :"+ adresse.get(0).getLatitude());
            longi = ("Lonjitude : "+adresse.get(0).getLongitude());
            adr = ("Adresse : "+adresse.get(0).getAddressLine(0));
            */
            ville= adresse.get(0).getLocality() + " " +adresse.get(0).getCountryName();

           localisation.setText(ville);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
