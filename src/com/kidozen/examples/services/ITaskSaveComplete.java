package com.kidozen.examples.services;

import org.json.JSONObject;

public interface ITaskSaveComplete {
	public void onSaveComplete(boolean status, JSONObject kidozenResponse);
}

