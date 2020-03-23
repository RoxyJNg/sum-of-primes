import java.util.HashMap;

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
    }

    /**
     *  解法1：一边筛选质数一边统计质数和。
     *  并用位运算优化内存
     *  总共有 n*[(n/2)+(n/3)+...+(n/i)+...+1] ≈ n^2 次循环
     * @param n
     */
    public static void solution1(int n){
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
        System.out.println("解法1：一边筛选质数一边统计质数和，" +
                "总共有 n*[(n/2)+(n/3)+...+(n/i)+...+1] ≈ n^2 次循环。" +
                "\n 得出质数和："+sum);
    }

    /**
     * 解法2：减少循环趟数。
     * 设 r = √n，p = n/√n
     * 总共有 r+p-2+r+p-1+(r-1)(r+p-1) = 2(n-1) 次循环
     */
    public static void solution2(int n) {
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
        System.out.println("解法2：减少循环趟数，" +
                "总共有 √n+n/√n-2+√n+n/√n-1+(√n-1)(√n+n/√n-1) = 2(n-1) 次循环。" +
                "\n 得出质数和："+map.get(n));
    }
}
