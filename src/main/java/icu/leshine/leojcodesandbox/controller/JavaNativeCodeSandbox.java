package icu.leshine.leojcodesandbox.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import icu.leshine.leojcodesandbox.CodeSandbox;
import icu.leshine.leojcodesandbox.entity.ExecuteCodeRequest;
import icu.leshine.leojcodesandbox.entity.ExecuteCodeResponse;
import icu.leshine.leojcodesandbox.entity.ExecuteMessage;
import icu.leshine.leojcodesandbox.entity.JudgeInfo;
import icu.leshine.leojcodesandbox.utils.ProcessUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Java 原生实现代码沙箱
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-14-20:09
 */
public class JavaNativeCodeSandbox implements CodeSandbox {

    private static final String USER_DIR = "user.dir";

    private static final String GLOBAL_CODE_PATH_NAME = "tmpCode";

    private static final String JAVA_CLASS_NANE = "Main.java";

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        // 从示例文件中读取提交代码
        String code = ResourceUtil.readStr("simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 获取题目提交请求信息
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 1. 将用户提交的代码存到文件里
        String userDir = System.getProperty(USER_DIR);
        // 1.1 判断全局代码文件是否存在，不存在则新建
        String userGlobalCodePathName = userDir + File.separator + GLOBAL_CODE_PATH_NAME;
        if (!FileUtil.exist(userGlobalCodePathName)) {
            FileUtil.mkdir(userGlobalCodePathName);
        }
        // 1.2 将用户的代码隔离存放
        String userCodeParentPath = userGlobalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + JAVA_CLASS_NANE;
        // 写入用户提交的代码
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
//        System.out.println("userCodeFile:" + userCodeFile);// userCodeFile:D:\code\leoj-code-sandbox\tmpCode\f59d3b90-fef7-4eca-895a-33c07a9dd62c\Main.java

        // 2. 编译代码，得到 class 文件
        try {
            String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile);
            ExecuteMessage compileMessage = null;
            compileMessage = ProcessUtil.runProcessAndGetMessage(compileCmd, "编译");
            System.out.println(compileMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 3. 执行代码，获取输出结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        try {
            for (String inputArgs : inputList) {
                String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
                ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(runCmd, "运行");
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 4. 收集整理执行结果返回给调用服务
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 4.1 获取输出列表
        long maxTime = Integer.MIN_VALUE;
        List<String> outputList = new ArrayList<>();
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                // 说明有程序执行错误
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            // 记录最大的时间消耗
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
            outputList.add(executeMessage.getMessage());
        }

        // 说明程序执行成功
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        // todo  获取程序执行内存
//        judgeInfo.setMemory();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 5. 清理文件
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 6. 错误处理，提升程序的健壮性
     *
     * @param e 程序运行 catch 到的异常
     * @return 空的响应
     */
    private ExecuteCodeResponse getErrorResponse(Exception e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(null);
        // 代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
