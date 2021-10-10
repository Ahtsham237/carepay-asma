package com.example.carepay.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class SessionManager {
	private static String TAG = SessionManager.class.getSimpleName();
	SharedPreferences
			pref;
	Editor
			editor;
	Context
			_context;
	private static final String PREF_NAME = "MyLoginPref";
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	private static final String KEY_USERNAME = "name";
	private static final String KEY_USEREMAIL = "email";
	private static final String KEY_ROLE = "role";
	private static final String KEY_IMAGE = "image";
	private static final String KEY_Id = "id";
	private static final String permission = "permission";
	private static final String image = "image";
//	private static final  int   image = 1;

	// 1 arg constructer
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}
	public void setId(String id){
		editor.putString(KEY_Id,id);
		editor.commit();
	}
	public String getId(){
		return  pref.getString("id","");
	}

	public void setRole(String role){
		editor.putString(KEY_ROLE,role);
		editor.commit();
	}
	public String getRole(){
		return  pref.getString(KEY_ROLE,"");
	}
	public void setLogin(boolean isLoggedIn,String username,String uid) {
		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		editor.putString(KEY_USEREMAIL,username);
		editor.putString(KEY_Id, uid);
		// commit changes
		editor.commit();
		Log.d(TAG, "User login session modified!");
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		// commit changes
		editor.commit();
		Log.d(TAG, "User login session modified!");
	}
	public boolean getlogin(){
		return pref.getBoolean(KEY_IS_LOGGED_IN,false);
	}
	public void setEmail(String email) {
		editor.putString("email", email);
		editor.commit();
	}
	public void setName(String name) {
		editor.putString("name", name);
		editor.commit();
	}
	public String getEmail(){
		return pref.getString("email","");
	}


	public boolean isLoggedIn() {
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
	public String getKeyUsername()
	{
		return pref.getString(KEY_USERNAME,"");
	}

	public String getKeyUserId() {
		return pref.getString(KEY_Id,"");
	}
	public void setPermission(String role){
		editor.putString(permission,role);
		editor.commit();
	}
	public String getPermission(){
		return  pref.getString(permission,"no");
	}
	public void setImage(String role){
		editor.putString(image,role);
		editor.commit();
	}
	public String getImage(){
		return  pref.getString(image,"no");
	}
}
