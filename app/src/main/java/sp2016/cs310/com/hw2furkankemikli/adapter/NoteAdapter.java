package sp2016.cs310.com.hw2furkankemikli.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sp2016.cs310.com.hw2furkankemikli.R;
import sp2016.cs310.com.hw2furkankemikli.bean.NoteBean;

import java.util.List;

/**
 * Created by furkankemikli on 17.05.2016.
 */
public class NoteAdapter extends ArrayAdapter<NoteBean> {
    Context context;
    int textViewResourceId;
    List<NoteBean> noteBeans;
    public NoteAdapter(Context context,int textViewResourceId,List<NoteBean> objects) {
        super(context,textViewResourceId, objects);
        this.context=context;
        this.textViewResourceId=textViewResourceId;
        this.noteBeans=objects;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        View row=view;

        if(row==null)
        {
            LayoutInflater layoutInflator=((Activity) context).getLayoutInflater();
            row=layoutInflator.inflate(textViewResourceId,parent,false);

        }
        NoteBean noteBean=noteBeans.get(position);
        if(noteBean!=null)
        {
            TextView title=(TextView)row.findViewById(R.id.noteTitle);
            if(noteBean.getNoteTitle().length() > 20)
            {
              title.setText(noteBean.getNoteTitle().substring(0,19)+ "...");

            }
            else
            {
            title.setText(noteBean.getNoteTitle());
            }
            title.setTextColor(Color.parseColor("#FFA07A"));
            TextView detail = (TextView)row.findViewById(R.id.noteDetail);
            if(noteBean.getNoteContent().length() > 30)
            {
                detail.setText(noteBean.getNoteContent().substring(0,29)+ "...");
            }
            else {
                detail.setText(noteBean.getNoteContent());
            }

            TextView date=(TextView)row.findViewById(R.id.noteDate);
            date.setText(noteBean.getNoteDate());
            ImageView imageview = (ImageView)row.findViewById(R.id.imgNote);
            if(noteBean.getNoteImage()!=null)
            {
                String encodedString=noteBean.getNoteImage();
                String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if(bitmap!=null){
                    int scaleFactor = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 512, scaleFactor, true);
                    imageview.setImageBitmap(scaledBitmap);
                }
            }
            LinearLayout noteView=(LinearLayout)row.findViewById(R.id.noteView);
            noteView.setTag(noteBean.getNoteId());
          }
        return row;
    }
}