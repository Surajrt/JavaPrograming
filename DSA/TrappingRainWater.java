// An element of the array can store water if there are higher bars on the left and the right. 
// The amount of water to be stored in every position can be found by finding the heights of bars on the left and right sides. 
// The total amount of water stored is the summation of the water stored in each index.

public class TrappingRainWater {
    // Java code to implement of the approach 
	// Function to return the maximum 
    	// water that can be stored 
	public static int maxWater(int[] arr, int n) 
	{ 

		// To store the maximum water 
		// that can be stored 
		int res = 0; 

		// For every element of the array 
		// except first and last element 
		for (int i = 1; i < n - 1; i++) { 

			// Find maximum element on its left 
			int left = arr[i]; 
			for (int j = 0; j < i; j++) { 
				left = Math.max(left, arr[j]); 
			} 

			// Find maximum element on its right 
			int right = arr[i]; 
			for (int j = i + 1; j < n; j++) { 
				right = Math.max(right, arr[j]); 
			} 

			// Update maximum water value 
			res += Math.min(left, right) - arr[i]; 
		} 
		return res; 
	} 

	// Driver code 
	public static void main(String[] args) 
	{ 
		int[] arr = { 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1 }; 
		int n = arr.length; 

		System.out.print(maxWater(arr, n)); 
	} 
} 
// This code is contributed by Debidutta Rath


