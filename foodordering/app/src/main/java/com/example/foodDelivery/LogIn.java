package com.example.foodDelivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodDelivery.Common.Common;
import com.example.foodDelivery.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

/*
 * @Author - Shubash
 */
public class LogIn extends AppCompatActivity {

    EditText editPhone,editPassword;
    Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone    = (MaterialEditText)findViewById(R.id.editPhone);

        btnLogIn = (Button)findViewById(R.id.btnSignIn1);

        //Initializing and Connecting to Firebase Db
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog messageDialog = new ProgressDialog(LogIn.this);
                messageDialog.setMessage("Please Wait .....");
                messageDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            messageDialog.dismiss();
                            //Get User Information
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());
                            if (user.getPassword().equals(editPassword.getText().toString())) {
                                Intent homeIntent = new Intent(LogIn.this,Home.class);
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(LogIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            messageDialog.dismiss();
                            Toast.makeText(LogIn.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
