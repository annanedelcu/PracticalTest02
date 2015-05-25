package ro.pub.cs.systems.pdsd.testpractic2;

import ro.pub.cs.systems.pdsd.testpractic2.ClientThread;
import ro.pub.cs.systems.pdsd.testpractic2.ServerThread;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TestPractic2 extends Activity {
	
	// Server widgets
		private EditText     serverPortEditText       = null;
		private Button       connectButton            = null;
		
		// Client widgets
		private EditText     clientAddressEditText    = null;
		private EditText     clientPortEditText       = null;
		private EditText     hourEditText             = null;
		private EditText     minuteEditText             = null;
		private Button      setButton = null;
		private Button      resetButton = null;
		private Button      pollButton = null;
		private TextView    pollAnswerTextView  = null;
	
	private ServerThread serverThread             = null;
	private ClientThread clientThread             = null;
	
	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
			}
			
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_practic2);
		
		serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
		connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectButtonClickListener);
		
		clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
		clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
		hourEditText = (EditText)findViewById(R.id.hour_edit_text);
		minuteEditText = (EditText)findViewById(R.id.minute_edit_text);
		setButton = (Button)findViewById(R.id.set_button);
		resetButton = (Button)findViewById(R.id.reset_button);
		pollButton = (Button)findViewById(R.id.poll_button);
		//setButton.setOnClickListener(getWeatherForecastButtonClickListener);
		pollAnswerTextView = (TextView)findViewById(R.id.poll_answer_text_view);
	}
	
	@Override
	protected void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_practic2, menu);
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
