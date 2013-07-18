package com.kidozen.examples.services;

import java.util.ArrayList;

import kidozen.client.Mail;
import kidozen.client.ServiceEvent;
import kidozen.client.ServiceEventListener;
import kidozen.client.Storage;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;


public class DataHelper {
	final String TAG = "DataHelper";
	public static Storage tasksStorage;

	// Listeners for call back
	private IKidozenApplicationSetup mAppSetupComplete;
	
	public void SetupKidozen(IKidozenApplicationSetup setupComplete) {
		Log.i(TAG, "kidozenAuthentication()");
		try {
			mAppSetupComplete = setupComplete;
			tasksStorage = SharedKidozen.Application().Storage("tasks");
			mAppSetupComplete.onKidozenAppSetupComplete(0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			mAppSetupComplete.onKidozenAppSetupComplete(1);
		}
	}

	/**
	 * Get task list
	 *
	 */
	public void getTasks(int filter, final ITaskQueryComplete queryComplete)
	{
		try {
			switch (filter) {
			case 3:
				tasksStorage.All(new ServiceEventListener() {
					@Override
					public void onFinish(ServiceEvent arg0) {
						JSONArray array = (JSONArray) arg0.Response;
						ArrayList<JSONObject> response = new ArrayList<JSONObject>();
						for (int i = 0; i < array.length(); i++) {
							response.add(array.optJSONObject(i));
						}
						
						queryComplete.onQueryComplete(true, response);
					}
				});
				break;
			case 2:
				tasksStorage.Query("{\"completed\": true}", new ServiceEventListener() {
					@Override
					public void onFinish(ServiceEvent arg0) {
						JSONArray array = (JSONArray) arg0.Response;
						ArrayList<JSONObject> response = new ArrayList<JSONObject>();
						for (int i = 0; i < array.length(); i++) {
							response.add(array.optJSONObject(i));
						}
						
						queryComplete.onQueryComplete(true, response);
					}
				});
				break;
			case 1:
				tasksStorage.Query("{\"completed\": false}", new ServiceEventListener() {
					@Override
					public void onFinish(ServiceEvent arg0) {
						JSONArray array = (JSONArray) arg0.Response;
						ArrayList<JSONObject> response = new ArrayList<JSONObject>();
						for (int i = 0; i < array.length(); i++) {
							response.add(array.optJSONObject(i));
						}
						
						queryComplete.onQueryComplete(true, response);
					}
				});
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			queryComplete.onQueryComplete(false, null);
		}

	}
	
	/*
	 * CRUD
	 * */
	public void insertTask(String title, String description, final ITaskSaveComplete saveComplete)
	{
		try {
			final JSONObject message = new JSONObject();
			message.put("title", title);
			message.put("desc", description);
			message.put("completed", false);
			tasksStorage.Create(message, new ServiceEventListener() {
				@Override
				public void onFinish(ServiceEvent arg0) {
					saveComplete.onSaveComplete(true, message);
				}
			});
		} catch (Exception e) {
			saveComplete.onSaveComplete(false, null);
		}
	}

	public void updateTask(JSONObject task, final ITaskUpdateComplete updateComplete)
	{
		try {
			String id = task.getString("_id");
			task.remove("completed");
			task.put("completed", true);
			
			tasksStorage.Update(task, id, new ServiceEventListener() {
				@Override
				public void onFinish(ServiceEvent arg0) {
					updateComplete.onUpdateComplete(true);
				}
			});
		} catch (Exception e) {
			updateComplete.onUpdateComplete(false);
		}
	}
	
	public void deleteTask(String id, final ITaskDeleteComplete deleteComplete)
	{
		try {
			tasksStorage.Delete(id, new ServiceEventListener() {
				@Override
				public void onFinish(ServiceEvent arg0) {
					deleteComplete.onDeleteComplete(true);
				}
			});
			
		} catch (Exception e) {
			deleteComplete.onDeleteComplete(false);
		}
	}

	/*
	 * EMail task
	 * */
	public void sendTask(JSONObject task, final IKidozenEmail mailComplete)
	{
		try {
			Mail email = new Mail();
			email.subject("task: " + task.getString("title"));
			email.textBody("description: " + task.getString("desc"));
			email.to("christian.carnero@gmail.com");
			email.from("christian.carnero@tellago.com");
			
			SharedKidozen.Application().SendEmail(email, new ServiceEventListener() {
				@Override
				public void onFinish(ServiceEvent e) {
					mailComplete.onEmailSent(e.StatusCode == 201);
				}
			});
		} catch (Exception e) {
			mailComplete.onEmailSent(false);
		}
	}

}
