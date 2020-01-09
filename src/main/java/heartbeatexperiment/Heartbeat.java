package heartbeatexperiment;

import org.apache.flink.api.java.functions.NullByteKeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class Heartbeat {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        DataStream<String> stuff = env.socketTextStream("localhost", 8888, "\n");
        DataStream<String> keyedStuff = stuff.keyBy(new NullByteKeySelector<>());
        DataStream<String> stuffWithHearbeat = keyedStuff.process(new ProcessFunction<String, String>() {
            @Override
            public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
                ctx.timerService().registerProcessingTimeTimer(ctx.timerService().currentProcessingTime()+3000);
                out.collect(value);
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> collector) {
                collector.collect(String.format("HEARTBEAT %d", ctx.timestamp()));
                ctx.timerService().registerProcessingTimeTimer(ctx.timerService().currentProcessingTime()+3000);
            }
        }).setParallelism(1);

        stuffWithHearbeat.print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
