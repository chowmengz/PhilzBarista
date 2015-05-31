package com.achow.philzbarista;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.imageViewUser) ImageView mUserProfile;
    @InjectView(R.id.finishOrderButton) Button mFinishOrderButton;
    @InjectView(R.id.OrderDeatil) TextView mOrderDeatils;
    @InjectView(R.id.customerName) TextView mCustomerName;
    @InjectView(R.id.customerTitle) TextView mCustomerTitle;

    public static Firebase mFirebaseClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Firebase.setAndroidContext(getApplicationContext());

        mFirebaseClient = new Firebase("https://philzapp.firebaseio.com/");

        mFirebaseClient.child("orderRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is gross, i dont know what im doing
                if (dataSnapshot.child("pending").getValue().equals(true)) {
                    mUserProfile.setVisibility(View.VISIBLE);
                    mFinishOrderButton.setVisibility(View.VISIBLE);
                    mOrderDeatils.setVisibility(View.VISIBLE);
                    mCustomerName.setVisibility(View.VISIBLE);
                    mCustomerTitle.setVisibility(View.VISIBLE);

                    mOrderDeatils.setText(dataSnapshot.child("order").getValue().toString());
                } else {
                    mUserProfile.setVisibility(View.INVISIBLE);
                    mFinishOrderButton.setVisibility(View.INVISIBLE);
                    mOrderDeatils.setVisibility(View.INVISIBLE);
                    mCustomerName.setVisibility(View.INVISIBLE);
                    mCustomerTitle.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        mFinishOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseClient.child("orderRequest/pending").setValue(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
