package me.study;

import java.io.File;
import java.io.IOException;
import java.util.List;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class SpringElasticsearchTestcontainerApplicationTests {

    @Container
    static DockerComposeContainer elasticsearch = new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService("elasticsearch", 9200);
    private static Integer port;
    private static String url;

    @BeforeAll
    public static void beforeAll() {
        port = elasticsearch.getServicePort("elasticsearch", 9200);
        url = elasticsearch.getServiceHost("elasticsearch", 9200);
    }

    @Test
    void test1() throws IOException, InterruptedException {

        RestClient restClient = RestClient.builder(new HttpHost(url, port)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);


        List<Member> members = List.of(
                new Member("test1", "1"),
                new Member("test2", "2"),
                new Member("test3", "3")
        );

        for (Member member : members) {
            client.index(i -> i.index("test")
                    .id(member.age())
                    .document(member)
            );
        }
        Thread.sleep(1000);
        CountResponse count = client.count(i -> i.index("test"));
        long result = count.count();
        assertThat(result).isEqualTo(3);
    }


    @Test
    void test2() throws IOException {
        RestClient restClient = RestClient.builder(new HttpHost(url, port)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient client = new ElasticsearchClient(transport);


        CountResponse count = client.count(i -> i.index("test"));
        long result = count.count();
        assertThat(result).isEqualTo(3);
    }
}
