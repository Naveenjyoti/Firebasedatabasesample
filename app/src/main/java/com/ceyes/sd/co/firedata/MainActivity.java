package com.ceyes.sd.co.firedata;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button button;
    //DatabaseReference databaseArtists;
    EditText username,email,password,email1;
    String id123="";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DatabaseReference databaseArtists;
    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide location permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
            }
        });
        button=(Button)findViewById(R.id.button);
        databaseArtists = FirebaseDatabase.getInstance().getReference("users");
        //    accounttype=(Spinner)findViewById(R.id.accounttype);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email);
        email1=(EditText)findViewById(R.id.email1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals(""))
                {
                    username.setError("Enter Username");
                }

                else if(email.getText().toString().equals(""))
                {
                    email.setError("Enter Phone");
                }
                else if(!isValidPhoneNumber(email.getText().toString()))
                {
                    email.setError("Enter valid phone");
                }
                else if(email1.getText().toString().equals(""))
                {
                    email1.setError("Enter email");
                }
                else if(!email1.getText().toString().matches(emailPattern))
                {
                    email1.setError("Invalid email");
                }

                else if(password.getText().toString().equals(""))
                {
                    password.setError("Enter Password");
                }
//                else if(!email.getText().toString().matches(emailPattern))
//                {
//                    //accounttype
//                    email.setError("Invalid Email");
//                }
                else
                {
                    //email123=email1.getText().toString();
                    //googlelogin();
                    adduser();
                }
            }
        });

    }
    private void adduser() {
        //getting the values to save
//        String name = editTextName.getText().toString().trim();
//        String genre = spinnerGenre.getSelectedItem().toString();
//
//        //checking if the value is provided
        if (!TextUtils.isEmpty(username.getText().toString())) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist

            Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email1.getText().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        // 1 or more users exist which have the username property "usernameToCheckIfExists"
                        System.out.println(dataSnapshot.getChildrenCount());
                        Toast.makeText(MainActivity.this, "Already exists!", Toast.LENGTH_LONG).show();
                    }
                    else if(dataSnapshot.getChildrenCount()==0)
                    {
                        String id = databaseArtists.push().getKey();
                      //  Users artist = new Users(id, username.getText().toString(), email1.getText().toString(),email.getText().toString(),password.getText().toString());
                      //  databaseArtists.child(id).setValue(artist);
                        Toast.makeText(MainActivity.this, "Moved to next step successfully", Toast.LENGTH_LONG).show();
//                        username.setText("");
//                        email.setText("");
//                        email1.setText("");
//                        password.setText("");
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                        Query lastQuery = databaseReference.child("users").orderByKey().limitToLast(1);
//                        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot child: dataSnapshot.getChildren()) {
//                                    System.out.println("User key" +child.getKey());
//                                    System.out.println("User val"+ child.child("email").getValue().toString());
//                                    id123=child.child("email").getValue().toString();
//                                }
//                               // System.out.println(email);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                // Handle possible errors.
//                            }
//                        });
                        startActivity(new Intent(MainActivity.this, Images.class)
                                .putExtra("id",id123)
                                .putExtra("name",username.getText().toString())
                                .putExtra("phone",email1.getText().toString())
                                .putExtra("email",email.getText().toString())
                                .putExtra("password",password.getText().toString())
                        );
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

}
