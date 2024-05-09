import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

public class symbolTester {
	public Collection getName;
	private float riskFactor;
	private String mSymbol0;
	private String dataPath0;
	private String mSymbol;
	private String dataPath;

	private int nameSize;
	private int capacity=100;
	private String[] name = new String[capacity];
	private Vector<Bar> mData;
	private Vector<Trade> mTrades;
	private boolean loaded = false;

	public symbolTester(String s0, String p0) {
		nameSize = 0;
		mSymbol0 = s0;
		dataPath0 = p0;
		loaded = false;
	}
	
	public void loadName(){
		String fileName = dataPath0 + mSymbol0 + ".txt";
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line;

			while((line = br.readLine()) != null) {
				reSize();
				name[nameSize]=line;
				nameSize++;
			}
			loaded = true;
			br.close();
			fr.close();
		}catch(IOException e) {
			System.out.println("Something is wrong: " + e.getMessage());
			loaded = false;
			return;
		}
	}
	private void reSize(){
		if(nameSize == capacity){
			String[] temp = new String[capacity+100];
			for(int i=0; i<nameSize; i++){
				temp[i] = name[i];
			}
			name = temp;
		}
	}

	public String getName(int index) {
		return name[index];
	}
	public int getNameSize() {
		return nameSize;
	}
	

	public symbolTester(String s, String p, float risk) {
		riskFactor =  risk;
		mSymbol = s;
		dataPath = p;
		mData = new Vector<Bar>(3000, 100);
		mTrades = new Vector<Trade>(200, 100);
		loaded = false;
	}

	public Vector<Trade> getTrades() {
		return mTrades;
	}
	public void loadData() {
		//create file name
		String fileName = dataPath + mSymbol + "_Daily.csv";
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while((line = br.readLine()) != null) {
				//create a bar using the constructor that accepts the data as a String
				Bar b = new Bar(line);
				//add the bar to the Vector
				mData.add(b);
			}
			loaded = true;
			br.close();
			fr.close();
		}catch(IOException e) {
			System.out.println("Something is wrong: " + e.getMessage());
			loaded = false;
			return;
		}
	}

	private boolean xDaysLow(int ind, int days) {
		for (int i = ind-1; i > ind-days; i--) {
			if(mData.elementAt(i).getLow() < mData.elementAt(ind).getLow())
				return false;
		}
		return true;
	}
	private boolean xDaysHigh(int ind, int days) {
		for (int i = ind-1; i > ind-days; i--) {
			if(mData.elementAt(i).getHigh() > mData.elementAt(ind).getHigh())
				return false;
		}
		return true;
	}
	void outcomes(Trade T, int ind) {//determines 
		for(int i = ind; i < mData.size(); i++) {
			if(T.getDir() == Direction.LONG) {
				if(mData.elementAt(i).getHigh() > T.getTarget()) { //it is a win
					//consider a gap day
					if(mData.elementAt(i).getOpen() > T.getTarget()) {
						//close at open  a gap day
						T.close(mData.elementAt(i).getDate(), mData.elementAt(i).getOpen(), i-ind);
						return;
					}else {
						//close the trade at target
						T.close(mData.elementAt(i).getDate(), T.getTarget(), i-ind);
						return;
					}
				} else if(mData.elementAt(i).getLow() < T.getStopLoss()) {
					//check if there is a gap down
					if(mData.elementAt(i).getOpen() < T.getStopLoss()) {
						//get out at the open
						T.close(mData.elementAt(i).getDate(), mData.elementAt(i).getOpen(), i-ind);
						return;
					}else {
						//get out at stoploss
						T.close(mData.elementAt(i).getDate(), T.getStopLoss(), i-ind);
						return;
					}

				}
			}else {// it is a short trade

				if(mData.elementAt(i).getLow() < T.getTarget()) { //it is a win
					//consider a gap day
					if(mData.elementAt(i).getOpen() < T.getTarget()) {
						//close at open  a gap day
						T.close(mData.elementAt(i).getDate(), mData.elementAt(i).getOpen(), i-ind);
						return;
					}else {
						//close the trade at target
						T.close(mData.elementAt(i).getDate(), T.getTarget(), i-ind);
						return;
					}
				} else if(mData.elementAt(i).getHigh() > T.getStopLoss()) {
					//check if there is a gap down
					if(mData.elementAt(i).getOpen() > T.getStopLoss()) {
						//get out at the open
						T.close(mData.elementAt(i).getDate(), mData.elementAt(i).getOpen(), i-ind);
						return;
					}else {
						//get out at stoploss
						T.close(mData.elementAt(i).getDate(), T.getStopLoss(), i-ind);
						return;
					}

				}



			}
		}//end of for
		//if we get here the trade is not closed, close it at the close of the last day
		T.close(mData.elementAt(mData.size()-1).getDate(), mData.elementAt(mData.size()-1).getClose(), mData.size()-1-ind);
	}

	public boolean test() {//tests our methods for short and long trade
		if(!loaded) {
			loadData();
			if (!loaded) {
				System.out.println("cannot load data");
				return false;
			}
		}

		for(int i = 20; i <mData.size()-1; i++) {
			if(xDaysLow(i, 20) //3) today(i) makes 20days low -- LONG TRADE
					&& mData.elementAt(i).getOpen() < mData.elementAt(i-1).getLow()  //1)today has gap down (open lower than low of the day before) means the opening price for that trade is lower than the lowest price of yesterday
					&& mData.elementAt(i).getClose() > mData.elementAt(i-1).getClose()//2) close higher than perivous day (i-1) close - means the trade's closing price of that day is greater than its yesterdays closing price 
					&& mData.elementAt(i-1).getClose() < mData.elementAt(i-1).getOpen() //4) yesterday close lower than its open 
					&&(mData.elementAt(i).getHigh()-mData.elementAt(i).getClose())/(mData.elementAt(i).getHigh()-mData.elementAt(i).getLow())< 0.1)    //and when meant the threshold of 10 percent
			{
				//we have a trade, buy at opne of i+1 (tomorrow) stoploss i.low, target = entry+factor*risk
				float entryprice = mData.elementAt(i+1).getOpen();//the price the etf/stock opens tomorrow
				float stoploss = mData.elementAt(i).getLow() - 0.01f;//todays lowest price subtracted from .01f
				float risk = entryprice - stoploss;
				float target = entryprice + riskFactor * risk;
				Trade T = new Trade();//creates trade
				T.open(mSymbol, mData.elementAt(i+1).getDate(), entryprice, stoploss, target, Direction.LONG);
				outcomes(T, i+1);//i+1 is when we buy at open - we buy the stock/etf the next day on its opening price 
				//add the trade to the Trade vector
				mTrades.add(T);
			}else if(xDaysHigh(i, 20) //3) today(i) makes 20days high --SHORT Trade-sell first high then buy low 
					&& mData.elementAt(i).getOpen() > mData.elementAt(i-1).getHigh()  //1) means the opening price for that trade is greater than the Highest price of yesterday
					&& mData.elementAt(i).getClose() < mData.elementAt(i-1).getClose()//2) means the trade's closing price of that day is lower than its yesterdays closing price 
					&& mData.elementAt(i-1).getClose() > mData.elementAt(i-1).getOpen()//4) yesterday closing price is greater than its opening price
					&& (mData.elementAt(i).getClose()-mData.elementAt(i).getLow())/(mData.elementAt(i).getHigh()-mData.elementAt(i).getLow())< 0.1 )    //and when meant the threshold of 10 percent
			{
				//we have a trade, buy at opne of i+1 (tomorrow) stoploss i.low, target = entry+factor*risk
				float entryprice = mData.elementAt(i+1).getOpen();
				float stoploss = mData.elementAt(i).getHigh() + 0.01f; //todays highest price adding .01f
				float risk = stoploss-entryprice;
				float target = entryprice - riskFactor * risk;
				Trade T = new Trade();
				T.open(mSymbol, mData.elementAt(i+1).getDate(), entryprice, stoploss, target, Direction.SHORT	);
				outcomes(T, i+1);//i+1 is when we buy this method buys the next day when conditions are meet
				//add the trade to the Trade vector
				mTrades.add(T);
			}
		}

		return true;
	}

}
