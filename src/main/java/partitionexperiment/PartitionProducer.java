package partitionexperiment;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PartitionProducer {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<Message> messages = new ArrayList<>();
        for (int r = 0; r < 1000; r++) {
            for (int v = 0; v < 200; v++) {
                messages.add(new Message(r, v));
            }
        }

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");

        DataStream<Message> stream = env.fromCollection(messages)
                .keyBy((KeySelector<Message, Integer>) message -> message.getReference());

        FlinkKafkaProducer<Message> sink = new FlinkKafkaProducer<Message>(
                "foo",
                new KeyedSerializer(),
                props,
                Optional.empty(),
                FlinkKafkaProducer.Semantic.AT_LEAST_ONCE,
                5
        );

        stream.addSink(sink);

        System.out.println(env.getExecutionPlan());

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
