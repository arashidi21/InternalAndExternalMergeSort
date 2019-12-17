/*  Name: <Ali Rashidinejad>
 *  COSC 311  FA19
 *  pp1008
 *  URL:  https://github.com/arashidi21/DonutShopSimulation
 */
import java.util.Random;

public class InternalMerge {
    public static void main(String[] args) {
        Random r = new Random();
        for (int i = 1000; i <= 10000; i +=1000) {
            int [] arr = new int[i];
            for (int j = 0; j < i; j++)
                arr[j] = r.nextInt();
            long start, end;
            start=System.nanoTime();
            mergeSort(arr);
            end=System.nanoTime();
            System.out.println((end-start)/1000000.0);
        }
    }

    private static void mergeSort(int []  arr){
        if(arr.length <= 1)
            return;
        int mid = (arr.length/2);
        int [] leftArr = new int [mid];
        int [] rightArr = new int [arr.length-mid];
        for(int i=0; i<leftArr.length; i++)
            leftArr[i]=arr[i];
        for(int i=0; i<rightArr.length; i++)
            rightArr[i]=arr[i+mid];
        mergeSort(leftArr);
        mergeSort(rightArr);
        merge(arr, leftArr, rightArr);
    }

    private static void merge(int[] arr, int[] leftArr, int[] rightArr) {
        int i = 0, j = 0;
        for (int k = 0; k < arr.length; k++) {
            if (i >= leftArr.length)
                arr[k] = rightArr[j++];
            else if (j >= rightArr.length)
                arr[k] = leftArr[i++];
            else if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i++];
            } else {
                arr[k] = rightArr[j++];
            }
        }
    }
}
