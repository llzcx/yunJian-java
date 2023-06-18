package ccw.ruan.resume.manager.es;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 陈翔
 */
@Data
public class PracticeExperienceEntity {

    @Id
    private String id;

    @Field(type = FieldType.Date,format = DateFormat.date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;


    @Field(type = FieldType.Date,format = DateFormat.date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String jobName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String companyName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;


    public PracticeExperienceEntity() {
    }

    public static void main(String[] args) {
        Date date = new Date();
        final String s = date.toString();
        System.out.println(s);
    }
}