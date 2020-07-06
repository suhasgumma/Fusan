package com.example.fusan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Dashboard extends AppCompatActivity {

    LinearLayout call,messaging,maps,camera,email,gallery,contacts,browser,settings,playstore,digitalWBeing,analysis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        call = findViewById(R.id.call);
        messaging = findViewById(R.id.messaging);
        maps = findViewById(R.id.maps);
        camera = findViewById(R.id.camera);
        email = findViewById(R.id.email);
        gallery = findViewById(R.id.gallery);
        contacts =findViewById(R.id.contacts);
        browser=findViewById(R.id.browser);
        settings=findViewById(R.id.settings);
        playstore=findViewById(R.id.playstore);
        analysis = findViewById(R.id.analysis);
        digitalWBeing = findViewById(R.id.digitalWellBeing);


        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),upload.class);
                startActivity(intent);
            }
        });

        digitalWBeing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"));

                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(callIntent);
                }
            }
        });

        messaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);

            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri mapUri = Uri.parse("geo:latitude,longitude?q:");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent =new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse("http://www.gmail.com"));

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent =new Intent(Intent.ACTION_VIEW);
                galleryIntent.setData(Uri.parse("content://media/external/images/media/"));

                if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(galleryIntent);
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contactsIntent =new Intent(Intent.ACTION_VIEW);
                contactsIntent.setData(Uri.parse("content://contacts/people/"));

                if (contactsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(contactsIntent);
                }
            }
        });

        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent =new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://www.google.com"));

                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
            }
        });

        playstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent playstoreIntent =new Intent(Intent.ACTION_VIEW);
                playstoreIntent.setData(Uri.parse("https://play.google.com/store/apps"));

                if (playstoreIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(playstoreIntent);
                }

            }
        });

    }
}
