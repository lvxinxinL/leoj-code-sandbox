package icu.leshine.leojcodesandbox.unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 写文件：写入木马程序
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-19:46
 */
public class WriteFileError {
    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马.bat";
        String errorProgram = "java -version";
        Files.write(Paths.get(filePath), Arrays.asList(errorProgram));
        System.out.println("写入木马成功，啦啦啦啦你完蛋啦");
    }
}
