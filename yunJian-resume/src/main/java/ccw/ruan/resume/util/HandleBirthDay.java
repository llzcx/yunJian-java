package ccw.ruan.resume.util;

import ccw.ruan.common.model.pojo.Resume;
import io.lettuce.core.ScanStream;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 陈翔
 */
public class HandleBirthDay {
    //1996: 28
    //1990: 34
    //1991: 33
    //1992: 32
    //1994: 30
    // 1995: 29
    //2001: 23
    //1987: 37
    //1989: 35
    public static void main(String[] args) {
        String date = "1996";

        // 定义匹配年份的正则表达式
        String regex = "(\\d{4})[-.]?(\\d{2})?";

        // 创建Pattern对象，并编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建Matcher对象，并进行匹配
        Matcher matcher = pattern.matcher(date);

        // 查找匹配的年份
        if (matcher.find()) {
            // 提取第一个分组中的年份
            String year = matcher.group(1);
            System.out.println("年份：" + year);
            System.out.println("生日："+(2023-Integer.valueOf(year)+1));
        } else {
            System.out.println("无法匹配年份");
        }
    }
}
