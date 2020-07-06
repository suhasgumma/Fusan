package com.example.fusan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Base64;

public class FaceAdapter extends ArrayAdapter<Person>{
    public FaceAdapter(Context context, ArrayList<Person> persons){
        super(context,0,persons);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Person person = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.faces_list_view, parent, false);
        }

        ImageView mostAppeared = (ImageView) convertView.findViewById(R.id.mostAppeared);
        TextView perrson = (TextView) convertView.findViewById(R.id.person);
        TextView time = (TextView) convertView.findViewById(R.id.timeee);

        //Convert String Into BitMap
        String face = person.face;
        Base64.Decoder decoder = Base64.getMimeDecoder();

        byte[] decoded = decoder.decode(face);

        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);



        mostAppeared.setImageBitmap(bitmap);
        perrson.setText(person.person);
        time.setText(person.time);

        return convertView;
    }
}