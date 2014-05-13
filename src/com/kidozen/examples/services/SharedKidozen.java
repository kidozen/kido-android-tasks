package com.kidozen.examples.services;

import kidozen.client.KZApplication;
import kidozen.client.ServiceEvent;
import kidozen.client.ServiceEventListener;
import android.util.Log;

public enum SharedKidozen {
	INSTANCE;
	private KZApplication application;
	
	static String TENANT = "http://tenant";
	static String APP ="demo";
	static String USER ="your user @kidozen.com";
	static String PASS ="your secret";
	private static String TAG = "SharedKidozen";
	private static final Object lock = new Object();

	
	public static KZApplication Application() throws Exception {
			synchronized (lock) {
				INSTANCE.application = new KZApplication(TENANT,APP, true, new ServiceEventListener() {
					@Override
					public void onFinish(ServiceEvent e) {
						if (e.StatusCode==200) {
							lock.notify();
						}
					}
				});
			}
			
			INSTANCE.application.Authenticate("Kidozen", USER, PASS, new ServiceEventListener() {
				@Override
				public void onFinish(ServiceEvent e) {
					if (e.StatusCode==200) {
						Log.d(TAG, "AUTH Finish: " + e.Body);
					}
				}
			});
			
			return INSTANCE.application;
	}

}