package com.example.fusan;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {
    private WindowManager windowManager;
    private View floatyView;
    private String finalGeneratedKey;
    private static int RESULT_LOAD_IMG = 1;
    String imgpath,storedpath;
    SharedPreferences sp;
    private LinearLayout ll;
    private static final int read=101;
    private static final int write=102;
    FirebaseAuth mauth;
    String feedBack;
    FirebaseDatabase database;
    DatabaseReference myRef,ratingRef;
    private FirebaseAuth.AuthStateListener mauthlistner;

    public void askPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    public void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

        final View dialogView = LayoutInflater.from(Main2Activity.this).inflate(R.layout.dialogue_feedback, null);

        builder.setView(dialogView);
        builder.setTitle("What do you Think?");


        builder.setNegativeButton("Leave Developer Sad", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final EditText feedback = (EditText)dialogView.findViewById(R.id.feedback);
                feedBack = feedback.getText().toString();

                final String[] ratings = new String[1];



                final RatingBar  ratingBar = dialogView.findViewById(R.id.ratingBar);
                final int userRating = (int)ratingBar.getRating();

                final String ratString = Integer.toString(userRating) + " stars";




                ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                            for (String key : dataMap.keySet()){
                                try {
                                    if(key.equals(ratString)){
                                        String total = String.valueOf(dataMap.get("Total reviews"));
                                        String average = String.valueOf(dataMap.get("average"));

                                         ratings[0] = String.valueOf(dataMap.get(key));
                                         int ratingInt = Integer.parseInt(ratings[0]) + 1;

                                         int totalInt = Integer.parseInt(total);

                                         String totalIntF = Integer.toString(totalInt+1);
                                         Float averageFloat = Float.parseFloat(average);

                                         Float finalAverage = ((averageFloat*totalInt)+userRating)/(totalInt+1);


                                         ratingRef.child(ratString).setValue(ratingInt);
                                         ratingRef.child("Total reviews").setValue(totalIntF);
                                         ratingRef.child("average").setValue(finalAverage);
                                    }

                                }catch (Exception e){}
                            }

                        }

                        else {
                            Log.d("Hopee", ratString);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}

                });
                myRef.child("feedback").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Rating").setValue(userRating);
                myRef.child("feedback").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Feedback").setValue(feedBack);
            }
        });

        builder.show();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        ratingRef = database.getReference("Ratings");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Main2Activity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timepicker);
        Button lock = (Button) findViewById(R.id.lock);
        Button signout=(Button)findViewById(R.id.logOut);

        sp=getSharedPreferences("setback", MODE_PRIVATE);
        final Calendar[] now = {Calendar.getInstance()};
        final int[] hour = {now[0].get(now[0].HOUR_OF_DAY)};
        final int[] min = {now[0].get(now[0].MINUTE)};
        final int[] ihour = {timePicker.getCurrentHour()};
        final int[] imin = {timePicker.getCurrentMinute()};

        askPermission();


        ArrayList<Integer> keygen = new ArrayList<Integer>();
        Random r = new Random();
        int nodots = r.nextInt(7 - 1) + 1;
        while (keygen.size() < nodots) {
            int tsize = keygen.size();
            int temp = r.nextInt(8 - 0) + 0;
            if (tsize == 0) {
                keygen.add(new Integer(temp));
            } else {
                if (keygen.indexOf(new Integer(temp)) == -1 && Math.abs((keygen.get(tsize - 1) - temp)) != 6 && Math.abs((keygen.get(tsize - 1) + temp)) != 2 && Math.abs((keygen.get(tsize - 1) + temp)) != 8 && Math.abs((keygen.get(tsize - 1) + temp)) != 14) {
                    keygen.add(new Integer(temp));
                }
            }
        }
        String generatedKey = "";
        for (int i = 0; i < keygen.size(); i++) {
            generatedKey = generatedKey + (keygen.get(i));
        }


         finalGeneratedKey = generatedKey;


        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                now[0] = Calendar.getInstance();
                hour[0] = now[0].get(now[0].HOUR_OF_DAY);
                min[0] = now[0].get(now[0].MINUTE);
                ihour[0] = timePicker.getCurrentHour();
                imin[0] = timePicker.getCurrentMinute();
                windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

                


                final WindowManager.LayoutParams params;
                int layoutparamstype;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layoutparamstype = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    layoutparamstype = WindowManager.LayoutParams.TYPE_PHONE;
                }
                layoutparamstype = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, layoutparamstype, 0, PixelFormat.OPAQUE);
                params.gravity = Gravity.CENTER | Gravity.START;
                params.x = 0;
                params.y = 0;
                FrameLayout interceptorLayout = new FrameLayout(Main2Activity.this) {

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {

                        // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {

                            // Check if the HOME button is pressed
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                                Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();
                                // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                                return true;
                            }
                        }

                        // Otherwise don't intercept the event
                        return super.dispatchKeyEvent(event);
                    }

                };
                LayoutInflater inflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

                if (inflater != null) {
                    floatyView = inflater.inflate(R.layout.activity_main0, interceptorLayout);
                    final PatternLockView  mPatternLockView = (PatternLockView) floatyView.findViewById(R.id.pattern_lock_view);
                    final TextView key=(TextView) floatyView.findViewById(R.id.key);
                    final TextView des=(TextView) floatyView.findViewById(R.id.des);
                    ll=(LinearLayout)floatyView.findViewById(R.id.ll);
                    now[0]=Calendar.getInstance();
                    hour[0] = now[0].get(now[0].HOUR_OF_DAY);
                    min[0] = now[0].get(now[0].MINUTE);
                    des.setText("Give an attemp after "+ihour[0]+":"+imin[0]+" to get the key");

                    if(sp.contains("imagepath")) {
                        storedpath=sp.getString("imagepath", "");
                        Drawable d= new BitmapDrawable(getResources(),BitmapFactory.decodeFile(storedpath));
                        ll.setBackground(d);
                    }




                    if(hour[0]>ihour[0]){
                        key.setText("The key is "+finalGeneratedKey);
                    }
                    if (hour[0] == ihour[0] && min[0] >= imin[0]){
                        key.setText("The key is "+finalGeneratedKey);
                    }


                    PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
                        @Override
                        public void onStarted() {

                        }

                        @Override
                        public void onProgress(List<PatternLockView.Dot> progressPattern) {
                        }

                        @Override
                        public void onComplete(List<PatternLockView.Dot> pattern) {
                            now[0]=Calendar.getInstance();
                            hour[0] = now[0].get(now[0].HOUR_OF_DAY);
                            min[0] = now[0].get(now[0].MINUTE);


                            if(hour[0]>ihour[0]){
                                key.setText("The key is "+finalGeneratedKey);
                            }
                            if (hour[0] == ihour[0] && min[0] >= imin[0]){
                                key.setText("The key is "+finalGeneratedKey);
                            }

                            if(PatternLockUtils.patternToString(mPatternLockView, pattern).equalsIgnoreCase(finalGeneratedKey)){

                                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                                Main2Activity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                windowManager.removeView(floatyView);
                                floatyView=null;
                                showFeedbackDialog();



                            }
                            else{
                                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                            }
                        }

                        @Override
                        public void onCleared() {
                        }
                    };
                    mPatternLockView.addPatternLockListener(mPatternLockViewListener);

                    windowManager.addView(floatyView, params);

                }
                else {
                    Toast.makeText(getApplicationContext(), "back button", Toast.LENGTH_SHORT).show();
                }
            }
        });


TextView pic = (TextView)findViewById(R.id.bgp);
pic.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(ContextCompat.checkSelfPermission(Main2Activity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"entered",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(Main2Activity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},read);
            }
        else {
            loadImagefromGallery(v);
        }
    }
});

signout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent out = new Intent(Main2Activity.this,SignIn.class);
        startActivity(out);
    }
});


    }






    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.MediaColumns.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgpath = cursor.getString(columnIndex);
                Log.d("path", imgpath);
                cursor.close();

                SharedPreferences.Editor edit=sp.edit();
                edit.putString("imagepath",imgpath);
                edit.commit();


                Bitmap myBitmap = BitmapFactory.decodeFile(imgpath);

                Drawable d= new BitmapDrawable(getResources(),myBitmap);
                ll.setBackground(d);
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Inorder to change the background check the existing one by locking once", Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Main2Activity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (floatyView!=null){
            windowManager.removeView(floatyView);
            floatyView=null;
        }
    }
}