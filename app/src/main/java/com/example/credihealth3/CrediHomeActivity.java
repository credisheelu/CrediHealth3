package com.example.credihealth3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class CrediHomeActivity extends Activity {

	ExpandableListViewAdapter adapter;
	PopupWindow popupWindow;
	boolean filterPOPUpEnabled=false;
	Button filterBt;
	ArrayList<Category> category_array = new ArrayList<Category>();
	String URL;
	ListView list;
	EditText searchEt;
	ImageButton searchBt;
	String searchTerm="";
	HttpClient client;
	ArrayList<String> cityFilterList;
	ArrayList<String> hosFilterList;
	ArrayList<String> specFilterList;
	ArrayList<String> genFilterList;
	String filters="";
	boolean searchClicked=false;
	JSONObject infoJson;
	int nullException=0;
	int pageNo=1;
	int doc_count=0;
	ArrayList<JSONArray> jsonal;
	
	MergeAdapter mergeAdapter;
	
   private String removeCounts(String filterString)
   {
	   StringTokenizer tokenizer=new StringTokenizer(filterString,"(");
	   return tokenizer.nextToken().trim();
   }
   
   
 public void filterBuilder()
  {
	   filters="";
	   Iterator itr;
	   
	   itr=cityFilterList.iterator();
	   while(itr.hasNext())
	   filters=filters+"&filter_city[]="+itr.next().toString();
	   itr=hosFilterList.iterator();
	   while(itr.hasNext())
	   filters=filters+"&filter_hospital[]="+itr.next().toString();
	   itr=specFilterList.iterator();
	   while(itr.hasNext())
	   filters=filters+"&filter_spec[]="+itr.next().toString();
	    itr=genFilterList.iterator();
	   while(itr.hasNext())
	   filters=filters+"&filter_gender[]="+itr.next().toString();
	   
	  Log.v("Filters", filters);
  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credi_home);
		
		searchEt=(EditText)findViewById(R.id.searchEt);
		searchBt=(ImageButton)findViewById(R.id.searchIBt);
		
		cityFilterList=new ArrayList<String>();
		hosFilterList=new ArrayList<String>();
		specFilterList=new ArrayList<String>();
		genFilterList=new ArrayList<String>();
		
		filterBt=(Button)findViewById(R.id.filterBt);
		filterBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		if(filterPOPUpEnabled)
		{
		 popupWindow.showAsDropDown(filterBt);    
         popupWindow.setFocusable(true);
	     popupWindow.update();
		}
		else{
	
			  LayoutInflater layoutInflater = (LayoutInflater)getBaseContext()
					  .getSystemService(LAYOUT_INFLATER_SERVICE);  
			  View popupView = layoutInflater.inflate(R.layout.activity_list, null);  
			  Category category = new Category();
			  category.category_name="Cities";
			  
			try {
				JSONArray cities=infoJson.getJSONArray("cities");
				for (int j = 0; j < cities.length(); j++) {
						    SubCategory subcategory = new SubCategory();
						    subcategory.subcategory_name = cities.getString(j);
						    subcategory.selected = true;
						    category.subcategory_array.add(subcategory);
						    }
						   category_array.add(category);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				category = new Category();
				category.category_name="Hospitals";
				  try {
					JSONArray cities=infoJson.getJSONArray("hospitals");
					 for (int j = 0; j < cities.length(); j++) {
						    SubCategory subcategory = new SubCategory();
						    subcategory.subcategory_name = cities.getString(j);
						    subcategory.selected = true;
						    category.subcategory_array.add(subcategory);
						   }
						   category_array.add(category);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				  category = new Category();
				  category.category_name="Genders";
				  try {
					JSONArray cities=infoJson.getJSONArray("genders");
					 for (int j = 0; j < cities.length(); j++) {
						    SubCategory subcategory = new SubCategory();
						    subcategory.subcategory_name = cities.getString(j);
						    subcategory.selected = true;
						    category.subcategory_array.add(subcategory);
						   }
						   category_array.add(category);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				  
				  category = new Category();
				  category.category_name="Specialities";
				  try {
					JSONArray cities=infoJson.getJSONArray("specs");
					 for (int j = 0; j < cities.length(); j++) {
						    SubCategory subcategory = new SubCategory();
						    subcategory.subcategory_name = cities.getString(j);
						    subcategory.selected = true;
						    category.subcategory_array.add(subcategory);
						   }
						 category_array.add(category);
				} catch (JSONException e) {
				      e.printStackTrace();
				}
		  
	 popupWindow = new PopupWindow( popupView, LayoutParams.WRAP_CONTENT,  
             LayoutParams.WRAP_CONTENT); 
	 ExpandableListView	  popListview=(ExpandableListView)popupView.findViewById(R.id.popup);
	 Button closePopup=(Button)popupView.findViewById(R.id.closePopupBt);
	 closePopup.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			popupWindow.dismiss();
		}
	});
	  
		  adapter = new ExpandableListViewAdapter(getBaseContext(), popListview, category_array);
		  popListview.setAdapter(adapter);
		 
		  popListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			 Log.e("Group position :: "+groupPosition, " &&   Child position :: "+childPosition);
			    if(category_array.get(groupPosition).subcategory_array.get(childPosition).selected) {
			     category_array.get(groupPosition).subcategory_array.get(childPosition).selected = false;
			     Category filterGroup=category_array.get(groupPosition);
			     
			     String filterGroupName=filterGroup.category_name;
			     String filterString=removeCounts(filterGroup.subcategory_array.get(childPosition)
			    		 .subcategory_name);
			     if(filterGroupName.equals("Cities"))
			      cityFilterList.add(filterString);
			     if(filterGroupName.equals("Hospitals"))
				  hosFilterList.add(filterString);
			     if(filterGroupName.equals("Specialities"))
				  specFilterList.add(filterString);
			     if(filterGroupName.equals("Genders"))
				  genFilterList.add(filterString);
			     filterBuilder();
			      nullException=0;
			      mergeAdapter = new MergeAdapter();
				   pageNo=1;
			      new Read().execute("j");
			    }
			    else {
			     category_array.get(groupPosition).subcategory_array.get(childPosition).selected = true;
                 Category filterGroup=category_array.get(groupPosition);
			     
			     String filterGroupName=filterGroup.category_name;
			     String filterString=removeCounts(filterGroup.subcategory_array.get(childPosition)
			    		 .subcategory_name);
			     if(filterGroupName.equals("Cities"))
			      cityFilterList.remove(filterString);
			     if(filterGroupName.equals("Hospitals"))
				  hosFilterList.remove(filterString);
			     if(filterGroupName.equals("Specialities"))
				  specFilterList.remove(filterString);
			     if(filterGroupName.equals("Genders"))
				  genFilterList.remove(filterString);
			       filterBuilder();
			      nullException=0;
			      mergeAdapter = new MergeAdapter();
				   pageNo=1;
			      new Read().execute("j");
			         }
			  
			    adapter.notifyDataSetChanged();
			   
			    return true;
		}});

		 filterPOPUpEnabled=true;
		 popupWindow.showAsDropDown(filterBt);    
		 popupWindow.setFocusable(true);
		 popupWindow.update();
		 }}
		});
		
		
		client=new DefaultHttpClient();
		jsonal=new ArrayList<JSONArray>();
		list=(ListView)findViewById(R.id.listView);
		list.setOnScrollListener(new OnScrollListener() {
	     
	        @Override
	        public void onScrollStateChanged(AbsListView view, int scrollState) {
	        	 
	        }

	        @Override
	        public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) {
	        	if(firstVisibleItem+visibleItemCount==(pageNo*10)-nullException)
	        	{
	        	if(pageNo<=(Math.ceil((double)doc_count/10)))
		            {  
		               pageNo=pageNo+1;
		               Toast.makeText(getApplicationContext(), "scroll down",Toast.LENGTH_LONG).show();
		               
		               new Read().execute("doctors");
		              
		             }
	        	}
	         }
	    });
		
		searchBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mergeAdapter!=null)
				{	
							int total=mergeAdapter.getCount();
							for(int i=0;i<total;i++)
							{
					            LazyImageLoadAdapter	adapter= (LazyImageLoadAdapter)mergeAdapter
					            		.getAdapter(i);
						     adapter.imageLoader.clearCache();
						
							}
							mergeAdapter=null;
				}
			     nullException=0;
			     category_array = new ArrayList<Category>();
				 mergeAdapter = new MergeAdapter();
				 pageNo=1;
				 filterPOPUpEnabled=false;
				 searchClicked=true;
				 filters="";
				 searchTerm=searchEt.getText().toString();
				 new Read().execute("doctors");
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.credi_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

public JSONObject getList()throws ClientProtocolException,IOException,JSONException{
	   
	
    	HttpGet get=new HttpGet(URL.replaceAll(" ","%20"));
    	HttpResponse r=client.execute(get);
    	int status=r.getStatusLine().getStatusCode();
    	if(status==200)
    	{
    		HttpEntity e=r.getEntity();
    		String data=EntityUtils.toString(e);
    		
    		JSONObject jsonobj=new JSONObject(data);
    		doc_count=jsonobj.getJSONObject("info").getInt("doc_count");
    		if(searchClicked)
    		{
    		infoJson=jsonobj.getJSONObject("info");
    		searchClicked=false;
    		}
    		return jsonobj;
    		}
    	return  null;
    }

public class Read extends AsyncTask<String,Integer,JSONObject>
{

	@Override
	protected JSONObject doInBackground( String... params) {
		JSONObject obj=null;
		try {
				
				URL="http://www.credihealth.com/api/v2/search?q="+searchTerm+"&city=Delhi%20NCR"+filters+"&type=Doctors&lat=&lng=&loc_id=&radius=&page="+pageNo+"&sort=Experience";
				obj=getList();//can give null pointer exception
				doc_count=obj.getJSONObject("info").getInt("doc_count");
				
			} catch (ClientProtocolException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(NullPointerException e)
			{
				nullException+=10;
			}
		    return obj;
}

	@Override
	protected void onPostExecute(JSONObject result) {
		if(result!=null)
		{
		   try {
			JSONArray doctors=result.getJSONArray("search");
			if(doctors!=null)
			 {
             LazyImageLoadAdapter adapter=new LazyImageLoadAdapter(CrediHomeActivity.this, doctors);
	           mergeAdapter.addAdapter(adapter);
		       if(pageNo==1)
		        list.setAdapter(mergeAdapter);
		       else
		    	 mergeAdapter.notifyDataSetChanged();
		    }
		        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}}
		
	}

}
