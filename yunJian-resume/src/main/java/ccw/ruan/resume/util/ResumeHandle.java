package ccw.ruan.resume.util;

import ccw.ruan.common.model.vo.WorkExperience;
import cn.hutool.core.lang.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简历处理工具类
 * @author 陈翔
 */
public class ResumeHandle {

    public static LocalDate currentDate = LocalDate.of(2023, 4, 1);

    /**
     * 计算工作年限
     * 工作年限：工作时间累加，不含实习经历、社会实践、兼职经历、空档期，只含全职在职经历，如果包含月份则向上取整，至今等同于2023年4月。
     *    例1: 有一段工作经历 2020～2022， 工作年限为2022-2020+1=3
     *    例2: 有两段工作经历 2019～2020 及 2021～2022， 工作年限为（2020-2019+1）+（2022-2021+1）= 4
     *    例3: 有一段工作经历 2022～至今， 工作年限为 2023-2022+1=2
     *    例4: 有一段工作经历 2020.3～2021.4， 工作年限为 ceil(2021年4月-2020年3月)=ceil(1年1月)=2
     *    例5: 有一段工作经历 2020.3～2021.2， 工作年限为 ceil(2021年2月-2020年3月)=ceil(0年11月)=1
     *    例6: 有两段工作经历 2020.3～2020.6， 2021.1～2021.5 工作年限为 ceil((2020年6月-2020年3月)+(2021年5月-2021年1月))=ceil(0年3月+0年4月)=1
     *    例7: 有一段工作经历 2020.3～至今， 工作年限为 ceil(2023年4月-2020年3月)=ceil(3年1月)=4
     *    例8: 有一段工作经历 2020.11～至今， 工作年限为 ceil(2023年4月-2020年11月)=ceil(2年5月)=3
     *    例9: 无工作经历， 工作年限为0
     * @param workExperiences 工作经历列表
     * @return 工作年限
     */
    public static int calculateWorkYears(List<WorkExperience> workExperiences) {
        int totalWorkMonths = 0;
        for (WorkExperience workExperience : workExperiences) {
            final String start = workExperience.getStartTime();
            final String end = workExperience.getEndTime();
            if((start!=null && end!=null) &&
                    ((isFourDigitNumber(start) && isFourDigitNumber(end))|| (isFourDigitNumber(start) && end.contains("至今")))) {
                int i = Integer.parseInt(start);
                int j;
                if(end.contains("至今")){
                    j = 2023;
                }else{
                    j = Integer.parseInt(end);
                }
                totalWorkMonths += 12 * (j-i+1);
                continue;
            }
            LocalDate startTime = parseStartTime(start);
            LocalDate endTime = parseEndTime(end);
            if (startTime == null) {
                // 开始时间解析失败，忽略该工作经历
                continue;
            }
            if (endTime == null || startTime.isAfter(endTime)) {
                // 结束时间解析失败或开始时间晚于结束时间，则将结束时间设置为开始时间加一年
                endTime = startTime.plusYears(1);
            }
            if (endTime.isAfter(currentDate)) {
                // 如果结束时间在当前时间之后，则将结束时间设置为当前时间
                endTime = currentDate;
            }
            int workMonths = (int) ChronoUnit.MONTHS.between(startTime, endTime);
            if (workMonths < 1) {
                // 如果工作年限少于一个月，则按照一个月计算
                workMonths = 1;
            }

            totalWorkMonths += workMonths;
        }

        return totalWorkMonths / 12;
    }

    public static LocalDate parseStartTime(String dateString) {
        try {
            return LocalDate.parse(formatDate(reg(dateString)), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LocalDate parseEndTime(String dateString) {
        if (dateString.contains("至今")) {
            // 如果结束时间为“至今”，则将结束时间设置为当前时间（即4月1日）
            return currentDate;
        }
        try {
            return LocalDate.parse(formatDate(reg(dateString)), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String reg(String dateString) {
        // 将非数字字符替换为-
        dateString = dateString.replaceAll("[^0-9]", "-");
        // 去掉末尾的-或.
        dateString = dateString.replaceAll("[-.]+$", "");
        return dateString.trim();
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(Objects.requireNonNull(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public static String formatDate(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}(-\\d{1,2})?$");
        Matcher matcher = pattern.matcher(date);

        if (matcher.matches()) {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;

            return String.format("%04d-%02d-%02d", year, month, 1);
        }

        return date; // 如果不匹配日期格式，则直接返回原始值
    }
    public static boolean isFourDigitNumber(String str) {
        if(str==null){
            return false;
        }
        Pattern pattern = Pattern.compile("^\\d{4}$");
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }
    public static void main(String[] args) {
        List<WorkExperience> list = new ArrayList<>();
        WorkExperience workExperience1 = new WorkExperience("2011-1", "2013-2");
        WorkExperience workExperience2 = new WorkExperience("2020.4", "至今");
        list.add(workExperience1);
        list.add(workExperience2);
        final int year = calculateWorkYears(list);
        System.out.println(year);
    }

}
