package com.kidozen.examples.services;

import java.util.ArrayList;

import org.json.JSONObject;

public interface ITaskQueryComplete {
	public void onQueryComplete(boolean status, ArrayList<JSONObject> taskList);
}


