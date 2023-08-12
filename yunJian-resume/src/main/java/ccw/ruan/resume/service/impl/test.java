package ccw.ruan.resume.service.impl;

import ccw.ruan.common.util.JsonUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class test {
    public static void main(String[] args) {
        String fileName = "E:/result.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String content = JsonUtil.object2StringSlice("sss");
            bufferedWriter.write(content);
            bufferedWriter.close(); // 记得关闭写入流

            System.out.println("写入文件成功！");
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }


    }
}
