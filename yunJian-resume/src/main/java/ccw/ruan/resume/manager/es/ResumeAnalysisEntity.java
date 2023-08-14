package ccw.ruan.resume.manager.es;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.dubbo.config.support.Nested;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author 陈翔
 */
@Document(indexName = "resume")
@Data
public class ResumeAnalysisEntity {

    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String name;
    @Field(type = FieldType.Integer, analyzer = "keyword")
    private String userId;
    @Field(type = FieldType.Date,format = DateFormat.date)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String graduationInstitution;
    @Field(type = FieldType.Boolean)
    private Boolean sex;
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String phone;
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String mailBox;
    @Field(type = FieldType.Text, analyzer = "keyword")
    private String education;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String major;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String expectedJob;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String projectExperiences;
    @Field(type = FieldType.Integer)
    private Integer workYear;
    @Nested
    private List<WorkExperienceEntity> workExperiences;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String skillsCertificate;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String awardsHonors;




}