package com.kidozen.examples.services;

import kidozen.client.KZApplication;
import kidozen.client.ServiceEvent;
import kidozen.client.ServiceEventListener;
import kidozen.client.Storage;

import org.json.JSONObject;

import android.util.Log;

public class KidozenHelper {
	private static final String TAG = "KidozenHelper";

	// TODO: Check if this can be included in the settings
	public static final String TENANT_MARKET_PLACE = "http://kidodemo.dev.kidozen.com/";
	public static final String APPLICATION = "tasks";
	public static final String PROVIDER = "Kidozen";
	public static final String KIDOZEN_USERNAME = "demo@kidozen.com";
	public static final String KIDOZEN_PASSWORD = "pass";
	public static final String SENDER_ID = "921202932497";

	// Kidozen application object
	public static KZApplication kidozenApplication;

	// Kidozen storage object
	public static Storage kidozenStorage;

	// Http status codes
	public static final int STATUS_OK = 200;
	public static final int STATUS_CREATED = 201;

	
	public void setUpKidozenApplication()
			throws Exception {
		Log.i(TAG, "setUpKidozen()");

		KidozenHelper.kidozenApplication = new KZApplication(
				KidozenHelper.TENANT_MARKET_PLACE, KidozenHelper.APPLICATION,
				applicationFinishCallback);
	}
	/**
	 * Callback after creating an object of kidozen application
	 */
	private ServiceEventListener applicationFinishCallback = new ServiceEventListener() {

		public void onFinish(ServiceEvent e) {
			Log.i(TAG, "applicationFinishCallback, STATUS: " + e.StatusCode);
		}
	};

	public void kidozenAuthentication(String userName, String password) {
		Log.i(TAG, "kidozenAuthentication()");

		kidozenApplication.Authenticate(KidozenHelper.PROVIDER, userName,
				password, authenticateCompletionListener);
	}

	private ServiceEventListener authenticateCompletionListener = new ServiceEventListener() {

		public void onFinish(ServiceEvent e) {
			Log.i(TAG, "authenticateCompletionListener, STATUS: " + e.StatusCode);

			if (e.StatusCode == KidozenHelper.STATUS_OK) {
				try {
					KidozenHelper.kidozenStorage = KidozenHelper.kidozenApplication.Storage("tasks");
				} catch (Exception e1) {
					Log.e(TAG, "kidozenAuthentication(), authenticateCompletionListener");
				}
			}

		}
	};
	
	public void CreateTask (JSONObject task, ServiceEventListener onCreateCallback) {
		KidozenHelper.kidozenStorage.Create(task, onCreateCallback);
	}
	
	public void GetTasks (String filter, ServiceEventListener onGetTasksFinish) {
		KidozenHelper.kidozenStorage.Query(filter, onGetTasksFinish);
	}
	
	public void UpdateTask(JSONObject task, String id, ServiceEventListener onUpdateCallback) {
		KidozenHelper.kidozenStorage.Update(task,id, onUpdateCallback);
	}
}
