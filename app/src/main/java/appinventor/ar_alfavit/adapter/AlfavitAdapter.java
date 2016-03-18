package appinventor.ar_alfavit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import appinventor.ar_alfavit.R;
import appinventor.ar_alfavit.ui.activity.MainActivity;

public class AlfavitAdapter extends BaseAdapter {
    private Context mContext;
     TextView rus;
    static boolean rt = true;

    public AlfavitAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mStringbIds.length;
    }

    public Object getItem(int position) {
        return mStringbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.cellgrid_alfa, parent, false);
        } else {
            grid = (View) convertView;
        }

        TextView arab = (TextView) grid.findViewById(R.id.textpartArab);
        rus = (TextView) grid.findViewById(R.id.textpartRusArab);


        Typeface CF = Typeface.createFromAsset(mContext.getAssets(), "arabic2.ttf");


        rus.setVisibility(View.VISIBLE);
        arab.setTypeface(CF);

        if (MainActivity.sVerticHorizPosition == true){
            arab.setText(mStringArabbIds[position]);
            rus.setText(mStringbIds[position]);
        }else {
            arab.setText(mStringArabbIds2[position]);
            rus.setText(alfavitViewHoriz[position]);
        }


       // grid.setMinimumWidth(TestActivity.sWidthLetter/4);
        return grid;
    }

    // references to our images
    public	String[] mStringArabbIds = {
            "ث", "ت", "ب", "ا",
            "د", "خ", "ح", "ج",
            "س", "ز", "ر", "ذ",
            "ط", "ض", "ص", "ش",
            "ف", "غ", "ع", "ظ",
            "م", "ل", "ك", "ق",
            "ي", "ه", "و", "ن",

    };
    public	String[] mStringArabbIds2 = {
            "خ",  "ح",  "ج", "ث", "ت", "ب", "ا",
            "ص",  "ش", "س",    "ز", "ر",  "ذ", "د",
            "ق",  "ف",  "غ",  "ع", "ظ", "ط", "ض",
            "ي",    "ه",  "و",  "ن", "م", "ل", "ك",

    };

    public	String[] mStringbIds = {
            "са" , "та", "ба", "алиф",
            "даль","хо","хьа", "джим",
            "син","за","ро","заль",
            "тъо","дод","сод","шин",
           "фа","г'айн","а'йн" ,"зъо",
            "мим","лам","каф","къоф",
              "йа","х'а","вов", "нун",
    };
    static public String[] alfavitViewHoriz = {
            "хо",  "хьа",  "джим","са", "та", "ба", "алиф",
            "сод","шин","син",  "за","ро", "заль","даль",
            "къоф","фа", "г'айн","а'йн","зъо", "тъо", "дод",
            "йа",  "х'а",   "вов","нун","мим", "лам", "каф",

    };

}