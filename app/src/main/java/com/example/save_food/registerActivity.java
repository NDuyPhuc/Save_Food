package com.example.save_food;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    EditText edtemail, edtpass;
    FirebaseAuth auth;
    Button btnLog;
    TextView acc;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        acc = findViewById(R.id.acc);
        edtemail   = findViewById(R.id.email);
        edtpass   = findViewById(R.id.password);
        btnLog   = findViewById(R.id.btnlog);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reg();
            }
        });
    }

    private void Reg(){
        dialog.show();
        String email = edtemail.getText().toString().trim();
        String password = edtpass.getText().toString().trim();
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            String email = user.getEmail();
                            String uid = user.getUid();


                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/savefood-a697c.appspot.com/o/imagedef%2Fimage.png?alt=media");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            startActivity(new Intent(registerActivity.this, loginActivity.class));
                        } else {
                            dialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(registerActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}