package uebung1;

import java.util.Stack;

public class IterativeQuickSort {
    private int a[];
    public IterativeQuickSort(int arr[]) {
        a = arr;
        quickSort();
    }
    public String toString(){
        String s = "";
        for (int i = 0; i < a.length; i++) {
            s += a[i];
            s += (i != a.length-1)? ", ":".";
        }
        return s;
    }

    public void swap(int i, int j){
        int tmp = a[j];
        a[j] = a[i];
        a[i] = tmp;
    }

    public int findPivotPosition(int low, int high){
        int pivot = a[high];
        int index_bigger = low - 1;
        int pivot_index = high;
        for (int j = low; j < high; j++){
            if (a[j] <= pivot){
                index_bigger++;
                swap(index_bigger, j);
            }
        }
        pivot_index = index_bigger+1;
        swap(pivot_index, high);
        return pivot_index;
    }

    public void quickSort(){
        Stack stack = new Stack<Integer>();
        stack.push(0);
        stack.push(a.length-1);
        while(!stack.isEmpty()){
            int high = (int) stack.pop();
            int low = (int) stack.pop();
            int p = findPivotPosition(low, high);
            if(p != 0 && (p-1) > low){
                stack.push(low);
                stack.push(p-1);
            }
            if(p != a.length-1 && (p+1) < high){
                stack.push(p+1);
                stack.push(high);
            }
        }
    }

    public static void main(String[] args) {
        int arr[] = {1, 3, 2, 0, 9, 0, 8, 6, 1, 9};
        IterativeQuickSort sort = new IterativeQuickSort(arr);
        System.out.println(sort.toString());
    }
}
