import java.util.concurrent.Callable;

/**
 * 多线程分段
 * @author Roxy
 * @date 2020/4/6
 */
public class SegmentTask implements Callable<Integer> {
    //第几分段
    private int order;
    private int n;
    //总共有几个分段
    private int p;
    public SegmentTask(int order, int n, int p) {
        this.order = order;
        this.n = n;
        this.p = p;
    }

    @Override
    public Integer call() throws Exception {
        int begin = (order-1)*(n/p)+1;
        int end = order*(n/p);
        if(order == 1){
            begin = 2;
        }

        int sum = 0;
        boolean flag;
        for(int i=begin;i<end;i++){
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
        return sum;
    }

    public int sum(){
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
        return sum;
    }
}
