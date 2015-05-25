package ro.pub.cs.systems.pdsd.testpractic2;

public class TimeInfo {
	private String hour;
	private String minute;
	
	public TimeInfo() {
		this.hour = null;
		this.minute   = null;
	}

	public TimeInfo(
			String hour,
			String minute) {
		this.hour = hour;
		this.minute   = minute;
	}
	
	public void setHour(String temperature) {
		this.hour = hour;
	}
	
	public String getHour() {
		return hour;
	}
	
	public String getMinute() {
		return minute;
	}
	
	public void setMinute() {
		this.minute   = minute;
	}
}
