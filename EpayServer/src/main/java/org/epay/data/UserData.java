package org.epay.data;

import net.sf.json.JSONObject;

public class UserData {
	private String userId;

	public UserData(JSONObject userData) {
		if (userData.containsKey("userId")) {
			this.userId = userData.getString("userId");
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
