package killiandebacker.com;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.Math;

public class Main {

    // 0, 1, 1, 2, 3, 5, 8, 13
    //       3  4  5  6  7   8
    public static void main(String[] args) {

        // test to find when integer overflows
//        boolean find_max = true;
//        for(long i = 0; i < 100; i++){
//            int number = (int)fibRegular(i);
//            padder(String.format("%d: %d", i, number));
//            System.out.println();
//        }
//        if(find_max)
//            return;

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(2, 1);
        map.put(1, 0);

        padder("N"); padder("X");
        padder("Fib Recur"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Cache"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Loop"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Matrix"); padder("Doubling Ratios"); padder("Expected");
        System.out.println();

        // Code copied to allow for the new timings for X and N
        // I wrote this before the full writeup was done
        // the writeup was done maybe a day or two before due date
        // I dont have the time to change this properly

        long[] prevTimes = {1,1,1,1};
        long count = 1;
        long countPrev = 1;
        while(true){
            padder(String.valueOf(countBits(count)));
            padder(String.valueOf(count));
            prevTimes = Run(count, prevTimes, countPrev);
            countPrev = count;
            count*=2;
//            if(count > 100){
//                break;
//            }
        }

    }

    // found on geeks for geeks
    static int countBits(long number)
    {
        return (int)(Math.log(number) / Math.log(2) + 1);
    }

    private static long[] Run(long n, long[] prevTimes, long nPrev){
        String[] testStrings = {"R", "C", "L", "M"};
        int i = 0;
        for(String sortType : testStrings){
            if(n > 48 && sortType == "R"){
                padder("N/A");
                padder("N/A");
                padder("N/A");
                i++;
                continue;
            }
            if(n > 2049 && sortType == "C"){
                padder("N/A");
                padder("N/A");
                padder("N/A");
                i++;
                continue;
            }
            if(prevTimes[i] > 30000000000L){
                padder("N/A");
                padder("N/A");
                padder("N/A");
                i++;
                continue;
            }

            long timeStampBefore = getCpuTime();
            double expected = 1;
            if(sortType == "R"){
                fibRecur(n);
                expected = Math.pow(2, n) / Math.pow(2, nPrev);
            }
            if(sortType == "C"){
                HashMap<Long, Long> map = new HashMap<>();
                map.put(2L, 1L);
                map.put(1L, 0L);
                fibCache(n, map);
                expected = (double)n / (double)(nPrev);
            }
            if(sortType == "L"){
                fibRegular(n);
                expected = (double)(n) / (double)(nPrev);;
            }
            if(sortType == "M"){
                fibRegular(n);
                expected = (double)(n) / (double)(nPrev);
            }

            long timeStampAfter = getCpuTime();
            long time = timeStampAfter - timeStampBefore;
            if(time <= 0){
                time = 1;
            }
            double actual = (time / (double)prevTimes[i]);
            prevTimes[i] = time;


            numberPadder(time);
            padder(Double.toString(actual) + "x");
            padder(Double.toString(expected) + "x");

            i++;
        }

        System.out.println();

        return prevTimes;
    }

    public static long fibCache(long x, HashMap<Long, Long> map){
        if(!map.containsKey(x)){
            long num = fibCache(x - 1, map) + fibCache(x  - 2, map);
            map.put(x, num);
        }
        return map.get(x);
    }

    public static long fibRecur(long x){
        // base case 1
        if( x == 2){
            return 1;
        }
        // base case 2
        if( x == 1){
            return 0;
        }
        else{
            return fibRecur(x - 1) + fibRecur(x - 2);
        }
    }

    public static long fibRegular(long n){
        long x = 0;
        long y = 1;
        if(n == 1 || n == 2){
            return n  - 1;
        }

        for(long i = 3; i <= n; i++) {
            long z = x + y;
            x = y;
            y = z;
        }
        return y;
    }

    public static long[][] multiply(long fib_mat[][], long mul_fib[][])
    {
        long[][] ret_fib = new long[2][2];
        ret_fib[0][0] = fib_mat[0][0]*mul_fib[0][0] + fib_mat[0][1]*mul_fib[1][0];
        ret_fib[0][1] = fib_mat[0][0]*mul_fib[0][1] + fib_mat[0][1]*mul_fib[1][1];
        ret_fib[1][0] = fib_mat[1][0]*mul_fib[0][0] + fib_mat[1][1]*mul_fib[1][0];
        ret_fib[1][1] = fib_mat[1][0]*mul_fib[0][1] + fib_mat[1][1]*mul_fib[1][1];

        return ret_fib;

    }

    public static long fibMatrix(long n){
        long[][] fib_mat = {{1,1},{1,0}};
        long[][] fib_mul = {{1,1},{1,0}};

        if(n == 1 || n == 2){
            return n  - 1;
        }

        for(long i = 3; i < n; i++){
            long[][] ret_fib = new long[2][2];
            ret_fib[0][0] = fib_mat[0][0]*fib_mul [0][0] + fib_mat[0][1]*fib_mul [1][0];
            ret_fib[0][1] = fib_mat[0][0]*fib_mul [0][1] + fib_mat[0][1]*fib_mul [1][1];
            ret_fib[1][0] = fib_mat[1][0]*fib_mul [0][0] + fib_mat[1][1]*fib_mul [1][0];
            ret_fib[1][1] = fib_mat[1][0]*fib_mul [0][1] + fib_mat[1][1]*fib_mul [1][1];

            fib_mat = ret_fib;
        }

        return fib_mat[0][0];

    }

    private static void padder(String str){
        int maxPadding = 20;
        String padding = "";
        for(int i = str.length(); i < maxPadding; i++){
            padding += " ";
        }
        System.out.print("|" + str + padding + "|");
    }

    private static void numberPadder(long number){
        String appended = "";
        if(number < 8000000000000L && number > 8000000000L){
            appended = "s";
            number /= 1000000000;
        }
        else if(number < 8000000000L && number > 8000000){
            appended = "ms";
            number /= 1000000;
        }
        else if(number < 8000000 && number > 8000){
            appended = "us";
            number /= 1000;
        }
        else if(number < 8000){
            appended = "ns";
        }
        padder(Long.toString(number) + appended);
    }

    public static long getCpuTime(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
    }
}

