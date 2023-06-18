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

        @Field(type = FieldType.Text, analyzer = "keyword")
        private String startTime;

        @Field(type = FieldType.Text, analyzer = "keyword")
        private String endTime;

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
    }