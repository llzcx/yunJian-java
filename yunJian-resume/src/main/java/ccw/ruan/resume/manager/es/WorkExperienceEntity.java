package ccw.ruan.resume.manager.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;


/**
 * @author 陈翔
 */
@Data
public class WorkExperienceEntity {
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