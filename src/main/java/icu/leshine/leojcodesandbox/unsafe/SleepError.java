package icu.leshine.leojcodesandbox.unsafe;

/**
 * 无限睡眠，阻塞程序
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-18:45
 */
public class SleepError {
    public static void main(String[] args) throws InterruptedException {
        long FIVE_MINUTE = 5 * 60 * 1000L;
        Thread.sleep(FIVE_MINUTE);
        System.out.println("阻塞结束");
    }
}
