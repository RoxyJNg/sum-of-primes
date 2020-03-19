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
     *  总共遍历 n*[(n/2)+(n/3)+...+(n/i)+...+1] 遍
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
                "总共遍历 n*[(n/2)+(n/3)+...+(n/i)+...+1] 遍。" +
                "\n 得出质数和："+sum);
    }

    /**
     * 解法2：减少循环趟数。
     * 假设一个数n不是质数，a<b<c<n ，b<=√n 且 n=a*c=c*a，那么遍历计算顺序为 a*c → b*b → c*a。
     * 所以当 被乘数>b 时，为重复计算。
     * 总共遍历 √n*[(n/2)+(n/3)+...+(n/i)+...+(n/√n)]+n 遍
     * @param n
     */
    public static void solution2(int n){
        boolean[] a = new boolean[n+1];
        for(int i=2;i*i<n;i++){
            if(!a[i]){
                for(int j=i;j<=n/i;j++){
                    a[i*j] = true;
                }
            }
        }
        int sum = 0;
        for(int i=2;i<n;i++){
            if(!a[i]){
                sum += i;
            }
        }
        System.out.println("解法2：减少循环趟数，" +
                "总共遍历 √n*[(n/2)+(n/3)+...+(n/i)+...+(n/√n)]+n 遍。" +
                "\n 得出质数和："+sum);
    }
}
