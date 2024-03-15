package icu.leshine.leojcodesandbox.unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * 占用内存不释放
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-19:04
 */
public class MemoryError {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        while (true) {
            list.add(new byte[10000]);
        }
    }
}
