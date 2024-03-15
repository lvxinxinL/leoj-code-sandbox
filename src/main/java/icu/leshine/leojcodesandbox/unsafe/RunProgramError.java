package icu.leshine.leojcodesandbox.unsafe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 运行程序：执行写入的木马文件
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-19:46
 */
public class RunProgramError {
    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/木马.bat";
        Process process = Runtime.getRuntime().exec(filePath);
        // 正常退出，获取控制台输出信息
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        // 收集输出信息
        StringBuilder compileOutputBuilder = new StringBuilder();
        String compileOutputLine;
        while ((compileOutputLine = bufferedReader.readLine()) != null) {
            System.out.println(compileOutputLine);
        }
        System.out.println("执行异常程序成功");
    }
}
