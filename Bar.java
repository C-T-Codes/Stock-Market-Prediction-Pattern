public class Bar {
	private String Date;
	private float open;
	private float high;
	private float low, close, adjClose;
	private long volume;
	public Bar(String st) {
		String[] stAr = st.split(",");
		if(stAr.length != 7) {
			System.out.println("Problem in bar");
			return;
		}
		Date = stAr[0];
		open = Float.parseFloat(stAr[1]);
		high = Float.parseFloat(stAr[2]);
		low = Float.parseFloat(stAr[3]);
		close = Float.parseFloat(stAr[4]);
		adjClose = Float.parseFloat(stAr[5]);
		volume = Long.parseLong(stAr[6]);
		
	}
	public float range() {
		return (this.high - this.low);
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public float getOpen() {
		return open;
	}
	public void setOpen(float open) {
		this.open = open;
	}
	public float getHigh() {
		return high;
	}
	public void setHigh(float high) {
		this.high = high;
	}
	public float getLow() {
		return low;
	}
	public void setLow(float low) {
		this.low = low;
	}
	public float getClose() {
		return close;
	}
	public void setClose(float close) {
		this.close = close;
	}
	public float getAdjClose() {
		return adjClose;
	}
	public void setAdjClose(float adjClose) {
		this.adjClose = adjClose;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public String toString() {
		String st = this.Date + ", " + open + ", " + high + ", " + low + ", "+ close + ", " + adjClose + ", " + volume;
		return st;
	}

}