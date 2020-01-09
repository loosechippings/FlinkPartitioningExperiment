package s3experiment;

import org.apache.flink.api.java.functions.NullByteKeySelector;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

public class TextAccumulator {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        try {
            env.setStateBackend(new RocksDBStateBackend("s3://foo/bar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        env.enableCheckpointing(1000);
        DataStream<String> stuff = env.socketTextStream("localhost", 8888, "\n");
        DataStream<String> accumulatedStuff = stuff
                .keyBy(new NullByteKeySelector<>())
                .process(new Accumulator())
                .setParallelism(1);

        accumulatedStuff.print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
