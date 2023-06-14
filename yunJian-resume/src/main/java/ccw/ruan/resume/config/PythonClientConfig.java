package ccw.ruan.resume.config;
import java.io.*;
import java.net.*;
/**
 * @author 周威宇
 * Python客户端连接简历识别模型代码
 */
public class PythonClientConfig {
    public static void main(String[] args) throws Exception {
        String filePath = "1";
        String urlStr = String.format("http://localhost:8090/ResumeIdentification?FilePath=%s", URLEncoder.encode(filePath, "UTF-8"));
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            res.append(inputLine);
        }
        in.close();
        System.out.println("收到响应: " + res.toString());
    }
}
