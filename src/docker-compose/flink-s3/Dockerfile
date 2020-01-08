FROM flink:1.9

RUN mkdir /opt/flink/plugins/s3-fs-presto && \
   cp /opt/flink/opt/flink-s3-fs-presto-1.9.1.jar /opt/flink/plugins/s3-fs-presto/ && \
   echo s3.access-key: minio >> /opt/flink/conf/flink-conf.yaml && \
   echo s3.secret-key: minio123 >> /opt/flink/conf/flink-conf.yaml && \
   echo s3.endpoint: http://127.0.0.1:9001 >> /opt/flink/conf/flink-conf.yaml && \
   echo s3.path-style: true >> /opt/flink/conf/flink-conf.yaml
