import java.util.HashMap;
import java.util.concurrent.FutureTask;

/**
 * 算出所有小于非负整数200,000的质数和
 * @author Roxy
 * @date 2020/3/19
 */
public class Solution {
    public static void main(String[] args) {
        int n = 200000;
        solution1(n);
        solution2(n);
        solution3(n);
        solution4(n);
    }

    /**
     *  解法1：一边筛选质数一边统计质数和。
     *  并用位运算优化内存
     *  总共有 n*[(n/2)+(n/3)+...+(n/i)+...+1] ≈ n^2 次循环
     * @param n
     */
    public static void solution1(int n){
        long begin = System.currentTimeMillis();
        int sum = 0;
        //第n组
        int group;
        //位下标
        int index;
        int[] a = new int[n/32+1];
        for(int i=2;i<n;i++){
            group = i/32;
            index = i%32;
            if((a[group] & (1 << index ))==0){
                for(int j=i;j<=n/i;j++){
                    group = i*j/32;
                    index = i*j%32;
                    a[group] |= (1 << index);
                }
                sum += i;
            }
        }
        long time = System.currentTimeMillis()-begin;
        System.out.println("解法1：一边筛选质数一边统计质数和，用时：" + time +
                "\n 总共有 n*[(n/2)+(n/3)+...+(n/i)+...+1] ≈ n^2 次循环。" +
                "\n 得出质数和："+sum);
    }

    /**
     * 解法2：减少循环趟数。
     * 设 r = √n，p = n/√n
     * 总共有 r+p-2+r+p-1+(r-1)(r+p-1) = 2(n-1) 次循环
     */
    public static void solution2(int n) {
        long begin = System.currentTimeMillis();
        HashMap<Integer, Long> map = new HashMap<>();
        int r = (int) Math.sqrt(n);
        int p = n / r;
        int[] a = new int[r + p - 1];

        for (int i = 1; i < (r + 1); i++) {
            a[i - 1] = n / i;
        }
        int count = 1;
        for (int i = r + p - 2; i >= r; i--) {
            a[i] = count++;
        }
        for (int i = 0; i < a.length; i++) {
            map.put(a[i], ((long) a[i] * (a[i] + 1) / 2 - 1));
        }
        long sp, i2;
        for (int i = 2; i < r + 1; i++) {
            if (map.get(i) > map.get(i - 1)) {
                sp = map.get(i - 1);
                i2 = i * i;
                for (int j = 0; j < a.length; j++) {
                    if (a[j] < i2) {
                        break;
                    }
                    map.put(a[j], map.get(a[j]) - i * (map.get(a[j] / i) - sp));
                }
            }
        }
        long time = System.currentTimeMillis()-begin;
        System.out.println("解法2：减少循环趟数，用时：" + time +
                "\n 总共有 √n+n/√n-2+√n+n/√n-1+(√n-1)(√n+n/√n-1) = 2(n-1) 次循环。" +
                "\n 得出质数和："+map.get(n));
    }

    /**
     * 解法3：暴力法
     */
    public static int solution3(int n){
        long begin = System.currentTimeMillis();
        int sum = 0;
        boolean flag;
        for(int i=2;i<n;i++){
            flag = true;
            for(int j=2;j<i;j++){
                if(i%j==0){
                    flag = false;
                    break;
                }
            }
            if (flag){
                sum += i;
            }
        }
        long time = System.currentTimeMillis()-begin;
        System.out.println("解法3：暴力法，用时："+time +
                "\n 得出质数和："+sum);
        return sum;
    }

    /**
     * 解法4：暴力法+多线程分段计算
     * 为了和单线程的做明显对比，选了个用时长的算法
     */
    public static void solution4(int n) {
        int p = 4;
        int sum = 0;
        long begin = System.currentTimeMillis();

        FutureTask<Integer> ft1 = new FutureTask<>(new SegmentTask(1,n,p));
        FutureTask<Integer> ft2 = new FutureTask<>(new SegmentTask(2,n,p));
        FutureTask<Integer> ft3 = new FutureTask<>(new SegmentTask(3,n,p));
        FutureTask<Integer> ft4 = new FutureTask<>(new SegmentTask(4,n,p));

        Thread t1 = new Thread(ft1);
        Thread t2 = new Thread(ft2);
        Thread t3 = new Thread(ft3);
        Thread t4 = new Thread(ft4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            sum = ft1.get()+ft2.get()+ft3.get()+ft4.get();
            long time = System.currentTimeMillis()-begin;
            System.out.println("解法4：暴力法+多线程分段计算，用时："+time +
                    "\n 得出质数和："+sum);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
