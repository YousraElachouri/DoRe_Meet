package net.yousra.dore_meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationListenerCompat;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LocationListener{
    private static final int PERMS_CALL_ID = 1234 ;
    private LocationManager lm;
    private TextView register, forgetPassword, localisation;
   private EditText email,password;
   private Button connect;
   private ProgressBar pb;
   private FirebaseAuth mAuth;
    Localisation loca;


    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register =(TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.password);
        connect = (Button)findViewById(R.id.signbtn);
        pb= (ProgressBar) findViewById(R.id.loadprogressBar);
        localisation =(TextView)findViewById(R.id.locali) ;
        forgetPassword = (TextView)findViewById(R.id.forget);
        forgetPassword.setOnClickListener(this);


        connect.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
       }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.register):{
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            }
            case(R.id.signbtn):{
                userLogin();
                break;
            }
            case(R.id.forget):{
                startActivity(new Intent(this,ForgetAvtivity.class));
                break;
            }

        }

    }

    private void userLogin() {
        String mail = email.getText().toString().trim();
        String motpasse = password.getText().toString().trim();
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
            password.setError("mot de passe invalide doit contenir plus que 6 caractères");
            password.requestFocus();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(mail,motpasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ApproximiteActivity.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "verifiez votre boite email", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this, " Connexion echouée ", Toast.LENGTH_SHORT).show();
                    return;

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
       /* String lat,longi,adr,ville;*/

        try {
            List<Address> adresse= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            /*lat= ("Latitude :"+ adresse.get(0).getLatitude());
            longi = ("Lonjitude : "+adresse.get(0).getLongitude());
            adr = ("Adresse : "+adresse.get(0).getAddressLine(0));
            */
            localisation.setText(adresse.get(0).getLocality()+" "+
                    adresse.get(0).getCountryName());

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