package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Visualizer extends JPanel implements ActionListener {

    Random rand = new Random();
    int arr[];
    boolean arrayCreated = false;
    int width, height, x, y;
    
    int numBars = 100;
    final int MINBARS = 50;
    final int MAXBARS = 200;

    JPanel settingsPanel, sortsPanel;
    JButton settings[] = new JButton[2];
    JButton sorts[] = new JButton[6];
    JButton newArray, sortButton, insertion, selection, bubble, merge, heap, quick;
    Font buttonFont = new Font("Dialog", Font.BOLD, 14);
    String sortType = "";

    SwingWorker<Void, Void> sorter;
    boolean sorterCreated = false;

    public Visualizer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 60, 20));
        setBackground(Color.black);
        add(createSettings());
        add(createSorts());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = super.getWidth() * 12 / 13;
        height = super.getHeight() * 8 / 11;
        x = super.getWidth() / 21;
        y = super.getHeight() / 5;

        if (!arrayCreated) {
            createArray(numBars);
            arrayCreated = true;
        }

        g.setColor(Color.white);
        for (int i = 0; i < arr.length; i++) {
            g.fillRect(x + (i * (width / arr.length)), y + height - arr[i], width / arr.length, arr[i]);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 600);
    }

    public int[] createArray(int bars) {
        arr = new int[bars];
        for (int i = 0; i < bars; i++) {
            arr[i] = rand.nextInt(height);
        }
        return arr;
    }

    public JPanel createSettings() {
        settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.black);

        newArray = new JButton("New Array");
        sortButton = new JButton("Sort");

        settings[0] = newArray;
        settings[1] = sortButton;

        for (JButton b : settings) {
            b.addActionListener(this);
            b.setBackground(Color.white);
            b.setFont(buttonFont);
            b.setFocusable(false);
            settingsPanel.add(b);
        }

        return settingsPanel;
    }

    public JPanel createSorts() {
        sortsPanel = new JPanel();
        sortsPanel.setBackground(Color.black);

        insertion = new JButton("Insertion");
        selection = new JButton("Selection");
        bubble = new JButton("Bubble");
        merge = new JButton("Merge");
        heap = new JButton("Heap");
        quick = new JButton("Quick");

        sorts[0] = insertion;
        sorts[1] = selection;
        sorts[2] = bubble;
        sorts[3] = merge;
        sorts[4] = heap;
        sorts[5] = quick;

        for (JButton b : sorts) {
            b.addActionListener(this);
            b.setBackground(Color.white);
            b.setFont(buttonFont);
            b.setFocusable(false);
            sortsPanel.add(b);
        }

        return sortsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (Arrays.asList(sorts).contains(source)) {
            for (JButton b : sorts) {
                if (source.equals(b)) {
                    b.setBackground(new Color(180, 210, 230));
                    sortType = b.getText();
                }
                else b.setBackground(Color.white);
            }
            return;
        }

        if (source.equals(newArray)) {  // create a new array
            arrayCreated = false;
            if (sorterCreated) sorter.cancel(true);
            repaint();
            return;
        } else initShuffler();  // sort with the selected algorithm    
    }

    public void initShuffler() {
        sorterCreated = true;
        sorter = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                switch (sortType) {
                    case "": break;
                    case "Insertion":
                        insertionSort();
                        break;
                    case "Selection":
                        selectionSort();
                        break;
                    case "Bubble":
                        bubbleSort();
                        break;
                    case "Merge":
                        mergeSort(0, arr.length - 1);
                    case "Heap":
                        heapSort();
                    case "Quick":
                        quickSort(0, arr.length - 1);
                }
                return null;
            }
        };
        sorter.execute();
    }

    public void insertionSort() throws InterruptedException {
        for (int i = 1; i < arr.length; i++) {  // iterate from index 1 to the end
            int x = arr[i]; // x = current element
            int j = i - 1;  // j = index of predecessor
            while (j > -1 && arr[j] > x) { // move elements up if they're greater than x
                arr[j + 1] = arr[j];
                j--;
                Thread.sleep(5);
                repaint();
            }
            arr[j + 1] = x; // place x in correct position after moving greater elements up
        }
    }

    public void selectionSort() throws InterruptedException {
        for (int i = 0; i < arr.length; i++) {  // iterate thru the array, i = start of unsorted subarray
            int min = arr[i];
            int minIndex = i;
            for (int j = i; j < arr.length; j++) {  // find minimum element in unsorted subarray
                if (arr[j] < min) {
                    min = arr[j];
                    minIndex = j;
                    Thread.sleep(10);
                    repaint();
                }
            }
            arr[minIndex] = arr[i]; // swap the minimum element with the first element in the unsorted subarray
            arr[i] = min;
        }
    }

    
    public void bubbleSort() throws InterruptedException {
        boolean sorted = false; // break the while-loop if sorted = true;
        while (!sorted) {
            sorted = true;  // sorted stays true if there are no swaps made
            for (int i = 0; i < arr.length-1; i++) {    // arr[i] = first element in bubble
                int x = arr[i+1];   // x = second element in bubble
                if (arr[i] > x) {
                    arr[i+1] = arr[i];  // swap arr[i] and x
                    arr[i] = x;
                    sorted = false;
                    Thread.sleep(5);
                    repaint();
                }
            }
        }
    }
    
    public void mergeSort(int l, int r) throws InterruptedException {
        if (l < r) {
            int m = l + (r-l)/2;    // find middle index
            mergeSort(l, m);   // call mergeSort on left and right sides
            mergeSort(m + 1, r);
            merge(l, m, r);    // merge left and right sides
            Thread.sleep(20);
            repaint();
        }
    }

    public void merge(int l, int m, int r) throws InterruptedException {  // helper function for mergeSort()
        int n1 = m - l + 1; // create temp arrays for left and right side
        int n2 = r - m;
        int arr1[] = new int[n1];
        int arr2[] = new int[n2];

        for (int i = 0; i < n1; i++) {  // copy arr to temp arrays
            arr1[i] = arr[i + l];
        }
        for (int j = 0; j < n2; j++) {
            arr2[j] = arr[m + 1 + j];
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
            Thread.sleep(10);
            repaint();
        }

        while (i < n1) {    // add remaining elements in left array to arr
            arr[k] = arr1[i];
            i++;
            k++;
            Thread.sleep(10);
            repaint();
        }

        while (j < n2) {    // add remaining elements in right array to arr
            arr[k] = arr2[j];
            j++;
            k++;
            Thread.sleep(10);
            repaint();
        }
    }

    public void heapSort() throws InterruptedException {
        int n = arr.length; // n = heap/array size

        for (int i = n/2 - 1; i >= 0; i--) {    // build a max-heap from the array using heapify function
        heapify(n, i); // start from n/2 - 1 (last parent node) because leaf nodes are already heapified
        }

        for (int i = n - 1; i > 0; i--) {  // repeat until heap size is 1
            int temp = arr[0];
            arr[0] = arr[i];    // the largest element is at the root, swap it with the last element and decrement the heap size
            arr[i] = temp;
            heapify(i, 0); // heapify the new root
            Thread.sleep(8);
            repaint();
        }
    }

    public void heapify(int n, int i) throws InterruptedException {  // n = heap size; i = root index
        int largest = i;    // largest = index of root/parent
        int left = 2*i + 1; // left = index of left child
        int right = 2*i + 2;    // right = index of right child

        if (left < n && arr[left] > arr[largest]) largest = left;   // if left child exists, check if it's larger than the parent
        if (right < n && arr[right] > arr[largest]) largest = right;    // if right child exists, check if it's larger than the parent and left child

        if (largest != i) { // if a child is larger than the parent, swap them and heapify on the new sub-tree
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            heapify(n, largest);
            Thread.sleep(8);
            repaint();
        }
    }

    public void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pivotIndex = partition(low, high); // after partition(), the pivot is at the correct index
            quickSort(low, pivotIndex - 1);    // call quickSort on the left and right sides of the pivot
            quickSort(pivotIndex + 1, high);
            Thread.sleep(20);
            repaint();
        }
    }

    public int partition(int low, int high) throws InterruptedException { // helper function to sort the array around a chosen pivot
        int pivotValue = arr[high]; // choose the last element as the pivot
        int pivotIndex = low;   // pivotIndex starts at low

        for (int i = low; i <= high - 1; i++) {    // iterate thru the subarray starting at low
            if (arr[i] < pivotValue) {  // if the current element is smaller than the pivot, swap it with the element at the current pivotIndex
                int temp = arr[pivotIndex];
                arr[pivotIndex] = arr[i];
                arr[i] = temp;
                pivotIndex++;
                Thread.sleep(20);
                repaint();
            }
        }
        arr[high] = arr[pivotIndex];    // place the pivot at the correct index by swapping it with the element at the current pivotIndex
        arr[pivotIndex] = pivotValue;

        return pivotIndex;
    }
}