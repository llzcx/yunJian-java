package ccw.ruan.resume.manager.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * @author 陈翔
 */
@Data
public class PracticeExperienceEntity {

    @Id
    private String id;

    @Field(type = FieldType.Date_Range)
    private ResumeAnalysisEntity.DateRange range;


    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String jobName;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String companyName;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "ik_max_word"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String description;

        public PracticeExperienceEntity(String id, ResumeAnalysisEntity.DateRange range,
                                        String jobName, String companyName, String description) {
                this.id = id;
                this.range = range;
                this.jobName = jobName;
                this.companyName = companyName;
                this.description = description;
        }

        public PracticeExperienceEntity() {
        }
}