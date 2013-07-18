package com.kidozen.examples.tasks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kidozen.examples.services.DataHelper;
import com.kidozen.examples.services.IKidozenEmail;
import com.kidozen.examples.services.ITaskDeleteComplete;
import com.kidozen.examples.services.ITaskQueryComplete;
import com.kidozen.examples.services.ITaskUpdateComplete;

public class TasksFragment extends Fragment {
	public static final String ARG_SECTION_NUMBER = "section_number";
	protected static final String TAG = "TasksFragment";
	private Dialog updateDialog ; 
	protected ArrayList<JSONObject> mTaskList;

	protected OnClickListener onCompleteClick= new OnClickListener() {
		DataHelper dh = new DataHelper();
		
		@Override
		public void onClick(View v) {
			try {
				Integer idx = (Integer)v.getTag();
				JSONObject selectedTaskTag = mTaskList.get(idx);
				switch (v.getId()) {
					case R.id.buttonComplete:
						dh.updateTask(selectedTaskTag, new ITaskUpdateComplete() {
							@Override
							public void onUpdateComplete(boolean status) {
								// TODO Auto-generated method stub
							}
						});
						break;
					case R.id.buttonDelete:
						String id = selectedTaskTag.getString("_id");
						dh.deleteTask(id, new ITaskDeleteComplete(){
							@Override
							public void onDeleteComplete(boolean success) {
								// TODO Auto-generated method stub
							}
						});
						break;
					case R.id.buttonSend:
						dh.sendTask(selectedTaskTag, new IKidozenEmail(){
							@Override
							public void onEmailSent(boolean success) {
								// TODO Auto-generated method stub
							}
						}
						);
						
						break;
					default:
						break;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			//	Toast.makeText(TasksFragment.this,"Create Operation is performed!",Toast.LENGTH_SHORT ).show();
			}
			updateDialog.cancel();
		}
	};

	private OnItemClickListener itemOnClicklistener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			updateDialog.setContentView(R.layout.dialog_update);
			updateDialog.setTitle("Update");                
			//set up button
			Button buttonComplete = (Button) updateDialog.findViewById(R.id.buttonComplete);
			buttonComplete.setTag(arg2);
			buttonComplete.setOnClickListener(onCompleteClick);
			
			Button buttonDelete = (Button) updateDialog.findViewById(R.id.buttonDelete);
			buttonDelete.setTag(arg2);
			buttonDelete.setOnClickListener(onCompleteClick);
			
			Button buttonSend = (Button) updateDialog.findViewById(R.id.buttonSend);
			buttonSend.setTag(arg2);
			buttonSend.setOnClickListener(onCompleteClick);
			updateDialog.show();
		}
	};

	public TasksFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		updateDialog = new Dialog(getActivity());

		final ListView tasksListView = (ListView) inflater.inflate(R.layout.tasks_listview, null);
		Integer t = getArguments().getInt(ARG_SECTION_NUMBER);
		DataHelper dh = new DataHelper();
		
		dh.getTasks(t,new ITaskQueryComplete() {
			@Override
			public void onQueryComplete(boolean status, ArrayList<JSONObject> taskList) {
				mTaskList = taskList;
				tasksListView.setAdapter(new TaskAdapter(getActivity(), R.layout.task_item,taskList));
				tasksListView.setOnItemClickListener(itemOnClicklistener );
			}
		});
		return tasksListView;
	}


	/**
	 * Simple task adapter
	 *
	 */
	public class TaskAdapter extends ArrayAdapter<JSONObject>{
		private static final String TAG = "TaskAdapter";
		private int resource;
		private LayoutInflater inflater;
		
		public TaskAdapter ( Context ctx, int resourceId, List<JSONObject> objects) {
			super( ctx, resourceId, objects );
			resource = resourceId;
			inflater = LayoutInflater.from( ctx );
		}

		@Override
		public View getView ( int position, View convertView, ViewGroup parent ) {
			convertView =inflater.inflate( resource, null );
			JSONObject obj = (JSONObject) getItem(position);
			try {
				TextView textviewSubject = (TextView) convertView.findViewById(R.id.textViewItemTitle);
				TextView textviewDescription = (TextView) convertView.findViewById(R.id.textViewItemDescription);
				textviewSubject.setText(obj.getString("title"));
				textviewDescription.setText(obj.getString("desc"));
				convertView.setTag(obj);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			return convertView;
		}
	}
}