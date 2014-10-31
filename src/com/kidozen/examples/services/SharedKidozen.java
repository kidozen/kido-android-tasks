package com.kidozen.examples.services;

import kidozen.client.KZApplication;


public enum SharedKidozen {
	INSTANCE;
	private KZApplication application;
	
	private static String TAG = "SharedKidozen";
	private static final Object lock = new Object();


    public static final String TENANT = "https://tenant";
    public static final String APP= "application";
    public static final String APPKEY = "applicationKey";
    

    public static KZApplication Application()  {
        try {
            if (INSTANCE.application == null) {
                INSTANCE.application = new KZApplication(TENANT, APP, APPKEY, false);
            }
            return INSTANCE.application;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}