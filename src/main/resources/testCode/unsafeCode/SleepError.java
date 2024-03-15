public class Main {
    public static void main(String[] args) throws InterruptedException {
        long ONE_MINUTE = 60 * 1000L;
        Thread.sleep(ONE_MINUTE);
        System.out.println("阻塞结束");
    }
}
