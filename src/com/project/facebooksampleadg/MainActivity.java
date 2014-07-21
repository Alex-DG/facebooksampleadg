package com.project.facebooksampleadg;

import java.util.Arrays;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private String TAG = "MainActivity";
	private TextView lblEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lblEmail = (TextView) findViewById(R.id.lblEmail);
		
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);//open login popup by suppressing SSO
		authButton.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());				
			}
		});		
		
		// careful here you can use 
		authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
		authButton.setSessionStatusCallback(new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				if(session.isOpened()){
					Log.i(TAG, "Access Token " + session.getAccessToken());
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if(user != null){
								Log.i(TAG,"User ID "+ user.getId());
	                            Log.i(TAG,"Email "+ user.asMap().get("email"));
	                            lblEmail.setText(user.asMap().get("email").toString());
							}
							
						}
					});
				}
				
			}
		});
	}

	@Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
