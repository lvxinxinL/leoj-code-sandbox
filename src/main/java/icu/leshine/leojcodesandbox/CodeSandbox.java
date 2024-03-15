package icu.leshine.leojcodesandbox;

import icu.leshine.leojcodesandbox.entity.ExecuteCodeRequest;
import icu.leshine.leojcodesandbox.entity.ExecuteCodeResponse;

/**
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-13-20:34
 */
public interface CodeSandbox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
