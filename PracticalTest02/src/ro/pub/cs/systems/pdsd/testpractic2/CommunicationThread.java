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
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (ip / command)!");
					String ip = bufferedReader.readLine();
					String command = bufferedReader.readLine();
					HashMap<String, TimeInfo> data = serverThread.getData();
					TimeInfo timeInfo = new TimeInfo();
					
					if(command.compareTo("set".toString()) == 0) {
						String hour            = bufferedReader.readLine();
						String minute = bufferedReader.readLine();
						
						if (data.containsKey(ip)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							timeInfo = data.get(ip);
						}
						
						timeInfo.setHour(hour);
						timeInfo.setMinute(minute);
						serverThread.setData(ip, timeInfo);
					} else 
						if(command.compareTo("reset".toString()) == 0) {
							data.remove(ip);
						} else
					
					if (command.compareTo("poll".toString()) == 0) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if (pageSourceCode != null) {
								String serverHour = pageSourceCode.substring(11, 12); 
								String serverMinute = pageSourceCode.substring(14, 15);
								
								String state = null;
								
								timeInfo = data.get(ip);
								
								if(Integer.parseInt(timeInfo.getHour()) < Integer.parseInt(serverHour)) {
									printWriter.println("inactive\n");
//									printWriter.flush();
									
									if(Integer.parseInt(timeInfo.getMinute()) < Integer.parseInt(serverMinute)) {
										
									}
								}
							} else {
								Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
							}
					
						
//						
//							
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
