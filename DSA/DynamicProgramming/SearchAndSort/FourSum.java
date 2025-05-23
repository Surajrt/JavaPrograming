
// Java program to find four elements that sum to a given value (4Sum)
// with given sum
import java.util.*;
class GFG {

// The following structure is needed
// to store pair sums in aux[]
static class pairSum {

	// Index (int A[]) of first element in pair
	public int first;

	// Index of second element in pair
	public int sec;

	// Sum of the pair
	public int sum;
}

// Function to check if two given pairs
// have any common element or not
static boolean noCommon(pairSum a, pairSum b)
{
	if (a.first == b.first || a.first == b.sec
		|| a.sec == b.first || a.sec == b.sec)
	return false;

	return true;
}

// The function finds four
// elements with given sum X
static void findFourElements(int[] myArr, int sum)
{
	int i, j;
	int length = myArr.length;

	// Create an auxiliary array to
	// store all pair sums
	int size = (length * (length - 1)) / 2;
	pairSum[] aux = new pairSum[size];

	// Generate all possible pairs
	// from A[] and store sums
	// of all possible pairs in aux[]
	int k = 0;
	for (i = 0; i < length - 1; i++) {
	for (j = i + 1; j < length; j++) {
		aux[k] = new pairSum();
		aux[k].sum = myArr[i] + myArr[j];
		aux[k].first = i;
		aux[k].sec = j;
		k++;
	}
	}

	// Sort the aux[] array using
	// library function for sorting
	Arrays.sort(aux, new Comparator<pairSum>() {
	// Following function is needed for sorting
	// pairSum array
	public int compare(pairSum a, pairSum b)
	{
		return (a.sum - b.sum);
	}
	});

	// Now start two index variables
	// from two corners of array
	// and move them toward each other.
	i = 0;
	j = size - 1;
	while (i < size && j >= 0) {
	if ((aux[i].sum + aux[j].sum == sum)
		&& noCommon(aux[i], aux[j])) {
		String output = myArr[aux[i].first] + ", "
		+ myArr[aux[i].sec] + ", "
		+ myArr[aux[j].first] + ", "
		+ myArr[aux[j].sec];
		System.out.println(output);
		return;
	}
	else if (aux[i].sum + aux[j].sum < sum)
		i++;
	else
		j--;
	}
}

public static void main(String[] args)
{
	int[] arr = { 10, 20, 30, 40, 1, 2 };
	int X = 91;

	// Function call
	findFourElements(arr, X);
}
}

// This code is contributed by phasing17
