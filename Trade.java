

/*
 * Day you entered the trade
 Entry price 
 Day you exit the trade
 ExitPrice
 StopLoss
 Target
 Direction of the trade
 HoldingPeriod how many day
 */
public class Trade {
	private String entryDate, exitDate, symbol;
	private float entryPrice, exitPrice, stopLoss, Target;
	private Direction Dir;
	private int holdingPeriod;
	public boolean isOn;
	
	public Trade() {
		symbol = "";
		entryDate = "";
		entryPrice = 0;
		stopLoss = 0; 
		Target = 0;
		holdingPeriod = 0;
		isOn = false;
		Dir = Direction.NONE;
	}
	
	public void open(String s, String ED, float price, float loss, float target, Direction d) {
		symbol = s;
		entryDate = ED;
		entryPrice = price;
		stopLoss = loss; 
		Target = target;
		holdingPeriod = 0;
		isOn = true;
		Dir = d;
	}
	public void close(String Ed, float xPrice, int holdingP) {
		exitPrice = xPrice;
		exitDate = Ed;
		holdingPeriod = holdingP;
		isOn = false;
	}
	
	public float percentPL() {
		if(Dir == Direction.LONG) {
			return (((exitPrice - entryPrice)/entryPrice)*100);
		}else if (Dir == Direction.SHORT) {
			return ((entryPrice - exitPrice)/entryPrice) *100;
		}else {
			System.out.println("We should never be here, something is wrong!");
			return 0;
		}
	}
	public String toString() {
		String st = symbol+ ", " + entryDate + ", " + entryPrice + ", " + stopLoss + ", " + Target + ", " + Dir + ", " + exitDate +", " + exitPrice + ", " + holdingPeriod;
		return st;
	}
	public int getHoldingPeriod() {
		return holdingPeriod;
	}
	public void setHoldingPeriod(int holdingPeriod) {
		this.holdingPeriod = holdingPeriod;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getExitDate() {
		return exitDate;
	}
	public void setExitDate(String exitDate) {
		this.exitDate = exitDate;
	}
	public float getEntryPrice() {
		return entryPrice;
	}
	public void setEntryPrice(float entryPrice) {
		this.entryPrice = entryPrice;
	}
	public float getExitPrice() {
		return exitPrice;
	}
	public void setExitPrice(float exitPrice) {
		this.exitPrice = exitPrice;
	}
	public float getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(float stopLoss) {
		this.stopLoss = stopLoss;
	}
	public float getTarget() {
		return Target;
	}
	public void setTarget(float target) {
		Target = target;
	}
	public Direction getDir() {
		return Dir;
	}
	public void setDir(Direction dir) {
		Dir = dir;
	}

}