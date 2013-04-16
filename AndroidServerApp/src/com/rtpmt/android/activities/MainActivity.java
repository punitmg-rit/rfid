package com.rtpmt.android.activities;

import com.rtpmt.android.network.tcp2.Communicator;
import com.rtpmt.android.network.tcp2.Test;
import com.rtpmt.android.serverapp.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button sendButton;
	private Button clearButton;
	private EditText textArea;
	private static boolean sendingData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendButton = (Button) findViewById(R.id.buttonSend);
		clearButton = (Button) findViewById(R.id.buttonClear);
		textArea = (EditText) findViewById(R.id.editText1);

		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!sendingData){
					// call the AsyncTask
					SendDataTask s = new SendDataTask();
					s.execute(this.toString());
					sendButton.setEnabled(false);
				}

			}
		});

		clearButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	

}

class SendDataTask extends AsyncTask<String, Integer, Long> {
	protected Long doInBackground(String... str) {
		try{
		Communicator communicator = new Communicator();
		communicator.initalizeTCPClient("129.21.175.114", 3000); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0l;
	}

	protected void onProgressUpdate(Integer... progress) {
	}

	protected void onPostExecute(Long result) {
	}
}


