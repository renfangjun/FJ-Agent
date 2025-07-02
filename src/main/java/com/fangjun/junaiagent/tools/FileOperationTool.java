package com.fangjun.junaiagent.tools;
import cn.hutool.core.io.FileUtil;
import com.fangjun.junaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import java.io.File;

/**
 * 文件操作工具（文件读写操作）
 *
 * @author fangjun
 * @modify 2025-06-27 17:01:35
 */
public class FileOperationTool {
    private final String FILE_DIR = FileConstant.TEMP_FILE_PATH + File.separator + "file";

    @Tool(description = "Read content from file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        String filePath = FILE_DIR + File.separator + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "readFile error " + e.getMessage();
        }
    }

    @Tool(description = "Write content to file")
    public String writeFile(@ToolParam(description = "Name of a file to write") String fileName,
                            @ToolParam(description = "Content to write") String content) {
        String filePath = FILE_DIR + File.separator + fileName;
        try {
            FileUtil.writeUtf8String(content, filePath);
            return "writeFile success";
        } catch (Exception e) {
            return "writeFile error " + e.getMessage();
        }
    }
}
