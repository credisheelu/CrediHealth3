package com.example.credihealth3;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;


	public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
	 private Context mContext;
	 private ExpandableListView mExpandableListView;
	 private List<Category> mGroupCollection;
	 private int[] groupStatus;
	 Boolean isActive=false;

	public ExpandableListViewAdapter(Context pContext, ExpandableListView pExpandableListView,
	List<Category> pGroupCollection) {
	  mContext = pContext;
	  mGroupCollection = pGroupCollection;
	  mExpandableListView = pExpandableListView;
	  groupStatus = new int[mGroupCollection.size()];
	  setListEvent();
	 }
	 private void setListEvent() {
	  mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
	   @Override
	   public void onGroupExpand(int arg0) {
	    groupStatus[arg0] = 1;
	   }
	  });
	  mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
	   @Override
	   public void onGroupCollapse(int arg0) {
	    groupStatus[arg0] = 0;
	   }
	  });
	 }
	 @Override
	 public String getChild(int arg0, int arg1) {
	  return mGroupCollection.get(arg0).subcategory_array.get(arg1).subcategory_name;
	 }
	 @Override
	 public long getChildId(int arg0, int arg1) {
	  return arg1;
	 }
	 @Override
	 public View getChildView(final int groupPosition, final int childPosition, boolean arg2, View convertView,ViewGroup parent)
	 {
	    final ChildHolder childHolder;
	  if (convertView == null) {
	   convertView = LayoutInflater.from(mContext).inflate(R.layout.child_row, null);
	   childHolder = new ChildHolder();
	   childHolder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
	   childHolder.name=(TextView)convertView.findViewById(R.id.childname);
	   convertView.setTag(childHolder);
	  } else {
	   childHolder = (ChildHolder) convertView.getTag();
	  }
	  childHolder.name.setText(mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).subcategory_name);
	  
	  if(mGroupCollection.get(groupPosition).subcategory_array.get(childPosition).selected) {
	   childHolder.checkBox.setImageResource(R.drawable.checkbox_empty);
	  } else {
	   childHolder.checkBox.setImageResource(R.drawable.checkbox_selected);
	  }
	  return convertView;
	 }
	 @Override
	 public int getChildrenCount(int arg0) {
	  return mGroupCollection.get(arg0).subcategory_array.size();
	 }
	 @Override
	 public Object getGroup(int arg0) {
	  return mGroupCollection.get(arg0);
	 }
	 @Override
	 public int getGroupCount() {
	  return mGroupCollection.size();
	 }
	 @Override
	 public long getGroupId(int arg0) {
	  return arg0;
	 }
	 @Override
	 public View getGroupView(int groupPosition, boolean arg1, View view, ViewGroup parent) {
	  GroupHolder groupHolder;
	  
	  if (view == null) {
	   view = LayoutInflater.from(mContext).inflate(R.layout.group_row,null);
	   groupHolder = new GroupHolder();
	   groupHolder.img = (ImageView) view.findViewById(R.id.tab_img);
	   groupHolder.title = (TextView) view.findViewById(R.id.group_name);
	   view.setTag(groupHolder);
	  } else {
	   groupHolder = (GroupHolder) view.getTag();
	  }
	  groupHolder.title.setText(mGroupCollection.get(groupPosition).category_name);
	    return view;
	 }
	 class GroupHolder {
	  ImageView img;
	  TextView title;
	 }
	 class ChildHolder {
	  ImageView checkBox;
	  TextView name;
	 }
	 @Override
	 public boolean hasStableIds() {
	  return true;
	 }
	 @Override
	 public boolean isChildSelectable(int arg0, int arg1) {
	  return true;
	 }
	}