
FROM docker.elastic.co/elasticsearch/elasticsearch:8.3.2

ADD --chown=elasticsearch:elasticsearch config/elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml
ADD plugin/musinsa-analyzer-8.3.2.0.zip /usr/share/elasticsearch/config/musinsa-analyzer-8.3.2.0.zip

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-nori
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install /usr/share/elasticsearch/config/musinsa-analyzer-8.3.2.0.zip


