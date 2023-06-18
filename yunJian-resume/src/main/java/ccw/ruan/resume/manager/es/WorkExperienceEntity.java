package ccw.ruan.resume.manager.es;

import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
import java.io.IOException;


/**
 * @author 陈翔
 */
@Data
public class WorkExperienceEntity {
        @Id
        private String id;

        /**
         * 范围
         */
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
}