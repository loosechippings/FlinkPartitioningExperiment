package partitionexperiment;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamUtils;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class ReinterpretedPartitionConsumer {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(20);

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("zookeeper.connect", "localhost:2181");
        props.setProperty("group.id", "test");


        FlinkKafkaConsumer<Message> consumer = new FlinkKafkaConsumer<Message>(
                "foo",
                new MessageDeserializer(),
                props
        );
        consumer.setStartFromEarliest();


        DataStream<Message> stream = env.addSource(consumer);

        DataStreamUtils.reinterpretAsKeyedStream(
                stream,
                new KeySelector<Message, Integer>() {
                    @Override
                    public Integer getKey(Message message) throws Exception {
                        return message.getReference();
                    }
                },
                TypeInformation.of(Integer.class)
        )
                .process(new CheckOrder())
                .print();

//        System.out.println(env.getExecutionPlan());

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
