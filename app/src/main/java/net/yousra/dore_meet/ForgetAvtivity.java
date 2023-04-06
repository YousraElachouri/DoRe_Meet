package net.yousra.dore_meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetAvtivity extends AppCompatActivity {
   private TextView banner;
    private EditText email;
   private Button reset;
   private ProgressBar pb;
   FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_avtivity);

        email= (EditText) findViewById(R.id.Email);
        reset = (Button) findViewById(R.id.resetbtn);
        pb =(ProgressBar) findViewById(R.id.loadbar);
        auth = FirebaseAuth.getInstance();
        banner= (TextView)findViewById(R.id.banner);

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetAvtivity.this, MainActivity.class));

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPass();
            }
        });

    }

    private void resetPass() {

        String mail = email.getText().toString().trim();

        if(mail.isEmpty()){
            email.setError("adresse email indispensable");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("adresse email non valide");
            email.requestFocus();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetAvtivity.this, "Verifiez votre boite email pour reinialiser mot de passe", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetAvtivity.this, MainActivity.class));
                }else{
                    Toast.makeText(ForgetAvtivity.this, "Essayez encore une fois", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}