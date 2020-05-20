package com.example.shopapp.Buyers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.Model.Users;
import com.example.shopapp.Prevalent.Prevalent;
import com.example.shopapp.R;
import com.example.shopapp.Sellers.SellerHomeActivity;
import com.example.shopapp.Sellers.SellerRegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerBegin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton= findViewById(R.id.main_login_btn);
        sellerBegin = findViewById(R.id.seller_begin);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey= Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey= Paper.book().read(Prevalent.UserPasswordKey);
        if(UserPasswordKey != "" && UserPhoneKey != "" ){
            if(!TextUtils.isEmpty(UserPasswordKey)  && !TextUtils.isEmpty(UserPhoneKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(final String userPhoneKey, final String userPasswordKey) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(userPhoneKey).exists()){
                    Users usersData = dataSnapshot.child("Users").child(userPhoneKey).getValue(Users.class);
                    if(usersData.getPhone().equals(userPhoneKey)){
                        if(usersData.getPassword().equals(userPasswordKey)){
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Logged in Successfully ",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentonlineUser = usersData;
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is incorrect ",Toast.LENGTH_LONG).show();
                        }
                    }

                }
                else{
                    Toast.makeText(MainActivity.this,"Account with this "+userPhoneKey+" do not exist",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"Please create Account ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
