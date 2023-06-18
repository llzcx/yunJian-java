package ccw.ruan.resume.util;

import ccw.ruan.common.model.vo.WorkExperience;
import cn.hutool.core.lang.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈翔
 */
public class ResumeHandle {


    public static LocalDate currentDate = LocalDate.of(2023, 4, 1);
    /**
     * 计算工作年限
     * @param workExperiences
     * @return
     */
    public static int calculateWorkYears(List<WorkExperience> workExperiences) {
        int totalWorkYears = 0;
        // 当前时间，至今等同于2023年4月


        for (WorkExperience workExperience : workExperiences) {
            LocalDate startTime = parseStartTime(workExperience.getStartTime());
            LocalDate endTime = parseEndTime(workExperience.getEndTime());

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
            int workYears = (int) ChronoUnit.YEARS.between(startTime, endTime);
            if (workYears < 1) {
                // 如果工作年限少于一年，则按照一年计算
                workYears = 1;
            }

            totalWorkYears += workYears;
        }

        return totalWorkYears;
    }

    public static LocalDate parseLocalDate(String dateString) {
        // 将非数字字符替换为-
        dateString = dateString.replaceAll("[^0-9]", "-");
        // 去掉末尾的-或.
        dateString = dateString.replaceAll("[-.]+$", "");
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    public static LocalDate parseStartTime(String dateString) {
        try {
            return LocalDate.parse(reg(dateString), DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    public static LocalDate parseEndTime(String dateString) {
        if (dateString.contains("至今")) {
            // 如果结束时间为“至今”，则将结束时间设置为当前时间（即4月1日）
            return currentDate;
        }
        try {
            return LocalDate.parse(reg(dateString), DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String reg(String dateString){
        // 将非数字字符替换为-
        dateString = dateString.replaceAll("[^0-9]", "-");
        // 去掉末尾的-或.
        dateString = dateString.replaceAll("[-.]+$", "");
        return dateString.trim();
    }


    public static Date toDate(LocalDate localDate){
        return Date.from(Objects.requireNonNull(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public static void main(String[] args) {
        final LocalDate date = parseStartTime("2021-01-23");
        System.out.println(date);
    }


}
