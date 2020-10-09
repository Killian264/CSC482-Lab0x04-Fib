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

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(2, 1);
        map.put(1, 0);

        padder("N");
        padder("Fib Recur"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Cache"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Loop"); padder("Doubling Ratios"); padder("Expected");
        padder("Fib Matrix"); padder("Doubling Ratios"); padder("Expected");
        System.out.println();

        long[] prevTimes = {1,1,1,1};
        long count = 1;
        while(true){
            padder(String.valueOf(count));
            prevTimes = Run(count, prevTimes);
            count *= 2;
        }

    }

    private static long[] Run(long n, long[] prevTimes){
        String[] testStrings = {"R", "C", "L", "M"};
        int i = 0;
        for(String sortType : testStrings){
            if(n > 32 && sortType == "R"){
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
                expected = Math.pow(2, n) / Math.pow(2, n/2);
            }
            if(sortType == "C"){
                HashMap<Long, Long> map = new HashMap<>();
                map.put(2L, 1L);
                map.put(1L, 0L);
                fibCache(n, map);
                expected = 2;
            }
            if(sortType == "L"){
                fibRegular(n);
                expected = 2;
            }
            if(sortType == "M"){
                fibRegular(n);
                expected = 2;
            }

            long timeStampAfter = getCpuTime();
            long time = timeStampAfter - timeStampBefore;
            if(time <= 0){
                time = 1;
            }
            double actual = (time / prevTimes[i]);
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
        int x = 0;
        int y = 1;

        for(int i = 3; i <= n; i++) {
            int z = x + y;
            x = y;
            y = z;
        }
        return y;
    }

    static long[][] multiply(long fib_mat[][], long mul_fib[][])
    {
        long[][] ret_fib = new long[2][2];
        ret_fib[0][0] = fib_mat[0][0]*mul_fib[0][0] + fib_mat[0][1]*mul_fib[1][0];
        ret_fib[0][1] = fib_mat[0][0]*mul_fib[0][1] + fib_mat[0][1]*mul_fib[1][1];
        ret_fib[1][0] = fib_mat[1][0]*mul_fib[0][0] + fib_mat[1][1]*mul_fib[1][0];
        ret_fib[1][1] = fib_mat[1][0]*mul_fib[0][1] + fib_mat[1][1]*mul_fib[1][1];

        return ret_fib;

    }

    static long fibMatrix(long n){
        long[][] fib_mat = {{1,1},{1,0}};
        long[][] fib_mul = {{1,1},{1,0}};

        for(int i = 3; i < n; i++){
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
