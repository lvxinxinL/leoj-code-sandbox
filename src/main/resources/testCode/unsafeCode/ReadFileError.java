import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 读文件：服务器信息泄露
 *
 * @author 乐小鑫
 * @version 1.0
 * @Date 2024-03-15-19:14
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "src/main/resources/application.yml";
        List<String> list = Files.readAllLines(Paths.get(filePath));
        System.out.println(String.join("\n", list));
    }
}
