import java.sql.SQLOutput;

import java.util.Vector;

public class Tester {

	public static void main(String[] args) {
		float[] risk = {0.5f, 1, 2, 5, 10};
		//we have five risk factors here, so we calculate it five times
		for (int R = 0; R < 5; R++) {
			Vector<Trade> Trades = new Vector<Trade>(3000);
			symbolTester tester0 = new symbolTester("ETFs", "Enter path to data file");
			symbolTester tester1 = new symbolTester("Stocks", "Enter path to data file");
			tester0.loadName();// save all the names from ETFs.txt to name[]
			for (int i = 0; i < tester0.getNameSize(); i++) {
				//name[0] is SPY_Daily.csv, We read this file first, then name[1] name[2] ...
				symbolTester tester = new symbolTester(tester0.getName(i), "Enter path to data file", risk[R]);
				tester.test();//using pattern to find all the dates we need
				Trades.addAll(tester.getTrades());
			}
			tester1.loadName();// save all the names from Stocks.txt to name[]
			for (int j = 0; j < tester1.getNameSize(); j++) {
				symbolTester tester = new symbolTester(tester1.getName(j), "Enter path to data file", risk[R]);
				tester.test();
				Trades.addAll(tester.getTrades());
			}

			//Compute the stats
			double totalwinner = 0;
			double totalTrades=0;
			double longTrade = 0;
			double shortTrade = 0;
			double longWinner = 0;
			double shortWinner = 0;
			double totalProfitLong = 0;//
			double totalProfitShort = 0;//
			double totalProfit = 0;
			double totalhodingPeriod = 0;
			double averageHoldingPeriod = 0;
			double averageProfitForTrades = 0;//

			for (int i = 0; i < Trades.size(); i++) {
				//if we make money in a trade
				if (Trades.elementAt(i).percentPL() >= 0) {
					totalwinner++;//count how many trade we make money
					if(Trades.elementAt(i).getDir() == Direction.LONG) {
						longWinner++;//count all the long trade that we make money
						totalProfitLong += Trades.elementAt(i).percentPL();
						longTrade++;//count all the long trade that we make money
					}
					if(Trades.elementAt(i).getDir() == Direction.SHORT) {
						shortWinner++;//count all the short trade that we make money
						totalProfitShort+= Trades.elementAt(i).percentPL();
						shortTrade++;//count all the short trade that we make money
					}
				}
				//else we lose money in trade
				else{
					if(Trades.elementAt(i).getDir() == Direction.LONG) {
						totalProfitLong += Trades.elementAt(i).percentPL();
						longTrade++;//adding the long trade that we lost money into line 49, which means the longTrade here is total long trade
					}
					if(Trades.elementAt(i).getDir() == Direction.SHORT) {
						totalProfitShort += Trades.elementAt(i).percentPL();
						shortTrade++;
					}

				}

				totalhodingPeriod += Trades.elementAt(i).getHoldingPeriod();
				totalProfit += Trades.elementAt(i).percentPL();

			}

			System.out.println("Number of Trade " + (totalTrades = shortTrade+longTrade));
			System.out.println("Percent Winners " + String.format("%.2f", (totalwinner/totalTrades)*100) );
			System.out.println("Percent Long Winners " + String.format("%.2f", ((longWinner/totalwinner)*100)) );
			System.out.println("Percent Short Winners " + String.format("%.2f", ((shortWinner/totalwinner)*100)) );
			System.out.println("Average Holding Period " + String.format("%.5f",(averageHoldingPeriod = totalhodingPeriod/totalTrades)) );
			System.out.println("Average Profit " + String.format("%.5f",(averageProfitForTrades = totalProfit/totalTrades)) );
			System.out.println("Average Profit Trade Long " + String.format("%.5f",totalProfitLong/longTrade) );
			System.out.println("Average Profit Trade Short " + String.format("%.5f",totalProfitShort/shortTrade));
			System.out.println("profit PerHoldingPeriod " + String.format("%.5f", averageProfitForTrades/averageHoldingPeriod) );
			System.out.println("-----------");







		}



	}
}
