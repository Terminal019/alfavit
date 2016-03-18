package appinventor.ar_alfavit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import appinventor.ar_alfavit.R;
import appinventor.ar_alfavit.ui.activity.TestActivity;

public class TestAdapter extends BaseAdapter {
    private Context mContext;
    LinearLayout fonShar;
    public ViewHolderText viewHolderText;
   static public int sLevel = 1;
    static class ViewHolderText {
        TextView arabicText;
    }
    public TestAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return Strings.mStringArabVertic.length;
    }

    public Object getItem(int position) {
        return Strings.mStringArabVertic[position];
    }

    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(R.layout.cellgrid_test, parent, false);
            viewHolderText = new ViewHolderText();
            viewHolderText.arabicText = (TextView) convertView.findViewById(R.id.textpartArab);
            convertView.setTag(viewHolderText);

        } else {
            viewHolderText = (ViewHolderText) convertView.getTag();
        }

        fonShar = (LinearLayout) convertView.findViewById(R.id.fonLiner);
        Typeface CF = Typeface.createFromAsset(mContext.getAssets(), "arabic2.ttf");

        if (TestActivity.sCountVis < 50) {
            if (position == TestActivity.sCountVis){
                convertView.setVisibility(View.INVISIBLE);
            }
        }
        viewHolderText.arabicText.setTypeface(CF);

        if (TestActivity.sVerticHorizPosition == true){

            if(sLevel == 1) {
                viewHolderText.arabicText.setText(Strings.mStringArabVertic[position]);
                fonShar.setBackgroundResource(R.drawable.shar2_1);
            }

            if(sLevel == 2){
                    viewHolderText.arabicText.setText(Strings.mStringArabVerticViewRandom[position]);
                    fonShar.setBackgroundResource(R.drawable.shar2_2);
            }

            if(sLevel == 3){
                viewHolderText.arabicText.setText(Strings.mStringArabVerticViewRandom3[position]);
                fonShar.setBackgroundResource(R.drawable.shar2_3);
            }

        }else if (TestActivity.sVerticHorizPosition == false) {
            if(sLevel == 1) {
                viewHolderText.arabicText.setText(Strings.mStringArabbHoriz[position]);
                fonShar.setBackgroundResource(R.drawable.shar2_1);
            }

            if(sLevel == 2){
                viewHolderText.arabicText.setText(Strings.mStringArabHorizViewRandom[position]);
                fonShar.setBackgroundResource(R.drawable.shar2_2);
            }

            if(sLevel == 3){
                viewHolderText.arabicText.setText(Strings.mStringArabHorizViewRandom3[position]);
                fonShar.setBackgroundResource(R.drawable.shar2_3);
            }
        }
        return convertView;
    }

}