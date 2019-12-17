/*  Name: <Ali Rashidinejad>
 *  COSC 311  FA19
 *  pp2
 *  URL:  https://github.com/arashidi21/DonutShopSimulation
 */
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class ExternalMergeSort {
    public static void main(String[] args) throws IOException {
        RandomAccessFile f1 = new RandomAccessFile("data.raf", "rw");
        Random r = new Random();
        for(int i=100; i<=1000; i+=100){
            f1.setLength(0);
            for(int j=0; j<i; j++)
                f1.writeInt(r.nextInt());
            mergeSort(f1);
        }
        f1.close();
    }
    private static void mergeSort(RandomAccessFile f1) throws IOException {
        RandomAccessFile f2 = new RandomAccessFile("helper.raf", "rw");
        f1.seek(0);
        int f1Length = (int) f1.length()/Integer.BYTES;
        long start, end;
        start = System.nanoTime();
        sort(f1, f2, f1Length, totalPasses(f1), 0);
        end = System.nanoTime();
        System.out.println((end - start)/1000000);
        if(f2.length()!=0){
            f1.setLength(0);
            f2.seek(0);
            for(int i=0; i<(f2.length()/Integer.BYTES); i++){
                int tmp = f2.readInt();
                f1.writeInt(tmp);
            }
        }
        f1.seek(0);
        f2.close();
    }
    private static int totalPasses(RandomAccessFile f1) throws IOException {
        int f1Length = (int) (f1.length()/Integer.BYTES);
        int pass=0;
        for(int i=1; i<=f1Length; i*=2)
            pass++;
        return pass;
    }
    private static void sort(RandomAccessFile f1, RandomAccessFile f2, int fileLength,
                             int totalPasses, int i) throws IOException {
        if(totalPasses==0)return;
        f2.setLength(0);
        int runLength = (int) Math.pow(2, i);
        int mergeRuns=fileLength/runLength;
        if(fileLength%runLength!=0)
            mergeRuns++;
        for (int j = 0; j < mergeRuns; j++)
            merge(f1, f2, fileLength, runLength, j);
        f1.setLength(0);
        sort(f2, f1, fileLength,totalPasses-1, i+1);
    }

    private static void merge(RandomAccessFile f1, RandomAccessFile f2, int fileLength,
                              int runLength, int j) throws IOException {
        int mergeLStart = (2 * j * runLength);

        int mergeLEnd;
        if(mergeLStart+runLength<fileLength)
            mergeLEnd = mergeLStart+runLength;
        else
            mergeLEnd=fileLength;

        int mergeRStart = mergeLStart + runLength;

        int mergeREnd;
        if(mergeRStart+runLength<fileLength)
            mergeREnd = mergeRStart+runLength;
        else
            mergeREnd=fileLength;

        while ((mergeRStart < mergeREnd) && (mergeLStart < mergeLEnd)) {
            f1.seek(mergeLStart*Integer.BYTES);
            int leftTmp = f1.readInt();
            f1.seek(mergeRStart*Integer.BYTES);
            int rightTmp = f1.readInt();
            if (leftTmp < rightTmp) {
                mergeLStart++;
                f2.writeInt(leftTmp);
            } else {
                mergeRStart++;
                f2.writeInt(rightTmp);
            }
        }
        while (mergeRStart < mergeREnd) {
            f1.seek(mergeRStart*Integer.BYTES);
            int val= f1.readInt();
            f2.writeInt(val);
            mergeRStart++;
        }
        while (mergeLStart < mergeLEnd) {
            f1.seek(mergeLStart*Integer.BYTES);
            int val=f1.readInt();
            f2.writeInt(val);
            mergeLStart++;
        }
    }
}
