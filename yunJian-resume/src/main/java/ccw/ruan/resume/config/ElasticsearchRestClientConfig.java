package ccw.ruan.resume.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * ElasticSearch 客户端配置
 * @author 陈翔
 * 2023/06/14
 */
@Configuration
public class ElasticsearchRestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${es.addr}")
    String esAddr;


    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esAddr)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
