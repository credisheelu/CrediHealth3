package com.example.credihealth3;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Adapter class extends with BaseAdapter and implements with OnClickListener 
public class LazyImageLoadAdapter extends BaseAdapter implements OnClickListener{
    
    private Activity activity;
    private JSONArray data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyImageLoadAdapter(Activity a, JSONArray d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length();
    }
    

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
         
        public TextView doctorNameTv;
        public TextView hospitalTv;
        public TextView specialityTv;
        public ImageView img;
 
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View vi=convertView;
        ViewHolder holder;
         
        if(convertView==null){
             
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_view_row, null);
             
            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.doctorNameTv = (TextView) vi.findViewById(R.id.doctorNameTv);
            holder.hospitalTv=(TextView)vi.findViewById(R.id.hospitalTv);
            holder.specialityTv=(TextView)vi.findViewById(R.id.specialityTv);
            holder.img=(ImageView)vi.findViewById(R.id.img);
             
           /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else 
            holder=(ViewHolder)vi.getTag();
        
        try{
       JSONObject json=data.getJSONObject(position);
      
        holder.doctorNameTv.setText(json.getString("doc_name"));
        holder.hospitalTv.setText(json.getString("hosp_name"));
        holder.specialityTv.setText("designation");
        ImageView image = holder.img;
        
        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage(json.getString("profile_pic_url"), image);
        }
        catch(Exception e){}
        
        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
    
    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{           
        private int mPosition;
        
       OnItemClickListener(int position){
        	 mPosition = position;
        }
        
        @Override
        public void onClick(View arg0) {
        	//MainActivity sct = (MainActivity)activity;
        	//sct.onItemClick(mPosition);
        }               
    }   
}