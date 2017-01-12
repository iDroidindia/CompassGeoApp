package test_app.location.compass_location_application.compassgeoapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
//    private final String[] itemname;
 //   private final String[] imgid;

    private final ArrayList<String> itemname;
    private final ArrayList<String> imgid;
    private final ArrayList<String> itemdesc;
    private final ArrayList<String> itemdis;


    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<String> imgid, ArrayList<String> itemdesc, ArrayList<String> itemdis) {
      //  super(context, R.layout.mylist, itemname);

        super(  context, R.layout.mylist,  imgid);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.itemdesc = itemdesc;
        this.itemdis = itemdis;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //View rowView = inflater.inflate(R.layout.mylist, null, true);
        View rowView = inflater.inflate(R.layout.mylist, null);

        TextView TV_Title = (TextView) rowView.findViewById(R.id.item);
        ImageView IV = (ImageView) rowView.findViewById(R.id.icon);
        TextView TV_Desc = (TextView) rowView.findViewById(R.id.tv_desc);
        TextView TV_Dis = (TextView) rowView.findViewById(R.id.tv_dis);

        TV_Title.setText(itemname.get(position));

      //  imageView.setImageResource(imgid[position]);
        Picasso.with(getContext()).load(imgid.get(position))
                .error(R.mipmap.ic_launcher)
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(IV);
        TV_Desc.setText(itemdesc.get(position));
        //TV_Dis.setText(itemdis.get(position) + " KM from the user.");
        TV_Dis.setText(itemdis.get(position));
        return rowView;

    }


}
