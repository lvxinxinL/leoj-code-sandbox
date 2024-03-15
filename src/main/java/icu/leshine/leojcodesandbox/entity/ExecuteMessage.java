package icu.leshine.leojcodesandbox.entity;

import lombok.Data;

/**
 * 执行命令参数实体类
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-14:17
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;
}
