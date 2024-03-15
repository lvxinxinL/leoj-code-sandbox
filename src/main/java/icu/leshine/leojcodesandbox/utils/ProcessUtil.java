package icu.leshine.leojcodesandbox.utils;

import icu.leshine.leojcodesandbox.entity.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 进程工具类
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-14:15
 */
public class ProcessUtil {

    /**
     * 执行命令并获取控制台输出
     *
     * @param runCmd  要运行的终端命令
     * @param optName 操作名称
     * @return 控制台输出信息
     */
    public static ExecuteMessage runProcessAndGetMessage(String runCmd, String optName) throws Exception {
        ExecuteMessage executeMessage = new ExecuteMessage();


        StopWatch stopWatch = new StopWatch();// 统计程序执行时间
        stopWatch.start();
        Process runProcess = Runtime.getRuntime().exec(runCmd);
        int exitValue = runProcess.waitFor();// 等待程序执行，获取程序退出的错误码
        executeMessage.setExitValue(exitValue);

        if (exitValue == 0) {
            // 正常退出，获取控制台输出信息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            // 收集输出信息
            StringBuilder compileOutputBuilder = new StringBuilder();
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputBuilder.append(compileOutputLine).append("\n");
            }
            System.out.println(optName + "成功");
            executeMessage.setMessage(compileOutputBuilder.toString());
//                System.out.println(compileOutputBuilder.toString());
        } else {
            // 异常退出，获取控制台输出信息
            // 分批获取输出
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            // 收集输出信息
            StringBuilder stringBuilder = new StringBuilder();
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(compileOutputLine).append("\n");
            }
            System.out.println(stringBuilder.toString());

            // 分批获取输出
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
            // 收集输出信息
            StringBuilder errorCompileOutputBuilder = new StringBuilder();
            String errorCompileOutputLine;
            while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                errorCompileOutputBuilder.append(errorCompileOutputLine).append("\n");
            }
            System.out.println(optName + "失败");
            executeMessage.setErrorMessage(errorCompileOutputBuilder.toString());
//                System.out.println(errorCompileOutputBuilder.toString());
        }
        stopWatch.stop();
        executeMessage.setTime(stopWatch.getTotalTimeMillis());// 设置程序执行时间消耗


        return executeMessage;
    }
}
