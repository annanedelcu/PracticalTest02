package ro.pub.cs.systems.pdsd.testpractic2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class CommunicationThread extends Thread {
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String ip = bufferedReader.readLine();
					String hour            = bufferedReader.readLine();
					String minute = bufferedReader.readLine();
					HashMap<String, TimeInfo> data = serverThread.getData();
					TimeInfo timeInfo = null;
					if (hour != null && !hour.isEmpty() && minute != null && !minute.isEmpty()) {
						if (data.containsKey(ip)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							timeInfo = data.get(ip);
						} else {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if (pageSourceCode != null) {
								
							} else {
								Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
							}
						}
						
//						if (timeInfo != null) {
//							String result = null;
//							if (Constants.ALL.equals(informationType)) {
//								result = weatherForecastInformation.toString();
//							} else if (Constants.TEMPERATURE.equals(informationType)) {
//								result = weatherForecastInformation.getTemperature();
//							} else if (Constants.WIND_SPEED.equals(informationType)) {
//								result = weatherForecastInformation.getWindSpeed();
//							} else if (Constants.CONDITION.equals(informationType)) {
//								result = weatherForecastInformation.getCondition();
//							} else if (Constants.HUMIDITY.equals(informationType)) {
//								result = weatherForecastInformation.getHumidity();
//							} else if (Constants.PRESSURE.equals(informationType)) {
//								result = weatherForecastInformation.getPressure();
//							} else {
//								result = "Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!";
//							}
//							printWriter.println(result);
//							printWriter.flush();
//						} else {
//							Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast information is null!");
//						}
						
					} else {
						Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
					}
				} else {
					Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} 
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}
}
