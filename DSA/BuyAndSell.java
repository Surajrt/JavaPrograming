//  In order to maximize the profit, we have to minimize the cost price and maximize the selling price. 
// So at every step, we will keep track of the minimum buy price of stock encountered so far.
//  If the current price of stock is lower than the previous buy price, then we will update the buy price, else if the current price of stock is greater than the previous buy price then we can sell at this price to get some profit.
//   After iterating over the entire array, return the maximum profit.

// Java code for the above approach
class GFG {
	static int maxProfit(int prices[], int n)
	{
		int buy = prices[0], max_profit = 0;
		for (int i = 1; i < n; i++) {

			// Checking for lower buy value
			if (buy > prices[i])
				buy = prices[i];

			// Checking for higher profit
			else if (prices[i] - buy > max_profit)
				max_profit = prices[i] - buy;
		}
		return max_profit;
	}

	// Driver Code
	public static void main(String args[])
	{
		int prices[] = { 7, 1, 5, 6, 4 };
		int n = prices.length;
		int max_profit = maxProfit(prices, n);
		System.out.println(max_profit);
	}
}

// This code is contributed by Lovely Jain
