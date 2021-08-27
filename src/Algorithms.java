package src;

import java.util.Arrays;
import java.util.Random;

public class Algorithms {

    public static void insertionSort(int arr[]) {
        for (int i = 1; i < arr.length; i++) {  // iterate from index 1 to the end
            int x = arr[i]; // x = current element
            int j = i - 1;  // j = index of predecessor
            while (j > -1 && arr[j] > x) { // move elements up if they're greater than x
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = x; // place x in correct position after moving greater elements up
        }
    }

    public static void selectionSort(int arr[]) {
        for (int i = 0; i < arr.length; i++) {  // iterate thru the array, i = start of unsorted subarray
            int min = arr[i];
            int minIndex = i;
            for (int j = i; j < arr.length; j++) {  // find minimum element in unsorted subarray
                if (arr[j] < min) {
                    min = arr[j];
                    minIndex = j;
                }
            }
            arr[minIndex] = arr[i]; // swap the minimum element with the first element in the unsorted subarray
            arr[i] = min;
        }
    }

    
    public static void bubbleSort(int arr[]) {
        boolean sorted = false; // break the while-loop if sorted = true;
        while (!sorted) {
            sorted = true;  // sorted stays true if there are no swaps made
            for (int i = 0; i < arr.length-1; i++) {    // arr[i] = first element in bubble
                int x = arr[i+1];   // x = second element in bubble
                if (arr[i] > x) {
                    arr[i+1] = arr[i];  // swap arr[i] and x
                    arr[i] = x;
                    sorted = false;
                }
            }
        }
    }
    
    public static void mergeSort(int arr[], int l, int r) {
        if (l < r) {
            int m = l + (r-l)/2;    // find middle index
            mergeSort(arr, l, m);   // call mergeSort on left and right sides
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);    // merge left and right sides
        }
    }

    public static int[] merge(int arr[], int l, int m, int r) {  // helper function for mergeSort()
        int n1 = m - l + 1; // create temp arrays for left and right side
        int n2 = r - m;
        int arr1[] = new int[n1];
        int arr2[] = new int[n2];

        for (int i = 0; i < n1; i++) {  // copy arr to temp arrays
            arr1[i] = arr[i + l];
        }
        for (int i = 0; i < n2; i++) {
            arr2[i] = arr[m + 1 + i];
        }

        int i = 0;  // i = iterate thru left temp array
        int j = 0;  // j = iterate thru right temp array
        int k = l;  // k = iterate thru arr

        while (i < n1 && j < n2) {  // compare first elements in left and right arrays and add smaller element to arr
            if (arr1[i] < arr2[j]) {
                arr[k] = arr1[i];
                i++;
            } else {
                arr[k] = arr2[j];
                j++;
            }
            k++;
        }

        while (i < n1) {    // add remaining elements in left array to arr
            arr[k] = arr1[i];
            i++;
            k++;
        }

        while (j < n2) {    // add remaining elements in right array to arr
            arr[k] = arr2[j];
            j++;
            k++;
        }

        return arr;
    }

    public static void heapSort(int[] arr) {
        int n = arr.length; // n = heap/array size

        for (int i = n/2 - 1; i >= 0; i--) {    // build a max-heap from the array using heapify function
        heapify(arr, n, i); // start from n/2 - 1 (last parent node) because leaf nodes are already heapified
        }

        for (int i = n - 1; i > 0; i--) {  // repeat until heap size is 1
            int temp = arr[0];
            arr[0] = arr[i];    // the largest element is at the root, swap it with the last element and decrement the heap size
            arr[i] = temp;
            heapify(arr, i, 0); // heapify the new root
        }
    }

    public static int[] heapify(int[] arr, int n, int i) {  // n = heap size; i = root index
        int largest = i;    // largest = index of root/parent
        int left = 2*i + 1; // left = index of left child
        int right = 2*i + 2;    // right = index of right child

        if (left < n && arr[left] > arr[largest]) largest = left;   // if left child exists, check if it's larger than the parent
        if (right < n && arr[right] > arr[largest]) largest = right;    // if right child exists, check if it's larger than the parent and left child

        if (largest != i) { // if a child is larger than the parent, swap them and heapify on the new sub-tree
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(arr, n, largest);
        }

        return arr;
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high); // after partition(), the pivot is at the correct index
            quickSort(arr, low, pivotIndex - 1);    // call quickSort on the left and right sides of the pivot
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) { // helper function to sort the array around a chosen pivot
        int pivotValue = arr[high]; // choose the last element as the pivot
        int pivotIndex = low;   // pivotIndex starts at low

        for (int i = low; i <= high - 1; i++) {    // iterate thru the subarray starting at low
            if (arr[i] < pivotValue) {  // if the current element is smaller than the pivot, swap it with the element at the current pivotIndex
                int temp = arr[pivotIndex];
                arr[pivotIndex] = arr[i];
                arr[i] = temp;
                pivotIndex++;
            }
        }
        arr[high] = arr[pivotIndex];    // place the pivot at the correct index by swapping it with the element at the current pivotIndex
        arr[pivotIndex] = pivotValue;

        return pivotIndex;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int arr1[] = new int[100];   // sorted using java's built-in method
        int arr2[] = new int[100];  // sorted using custom algorithm

        for (int i = 0; i < 10000; i++) {   // 10000 test cases
            for (int j = 0; j < 100; j++) {
                arr1[j] = rand.nextInt(1000);   // fill arr1 and arr2 with the same random values
            }
            arr2 = arr1;

            Arrays.sort(arr1);  // java's built-in sort algorithm
            insertionSort(arr2);    // custom sort algorithm

            if (!Arrays.equals(arr1, arr2)) {   // if arr1 != arr2, print arr2 and stop
                System.out.println("\n Failed: " + Arrays.toString(arr2));
                break;
            }
        }
        System.out.println("\n Passed");
    }
}
