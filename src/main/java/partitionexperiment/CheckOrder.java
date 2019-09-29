package partitionexperiment;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class CheckOrder extends KeyedProcessFunction<Integer, Message, Message> {

    private transient ValueState<Integer> latest;

    @Override
    public void open(Configuration conf) {
        ValueStateDescriptor<Integer> descriptor =
                new ValueStateDescriptor<Integer>(
                        "latest",
                        TypeInformation.of(Integer.class)
                );
        latest = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void processElement(Message message, Context context, Collector<Message> collector) throws Exception {
        if (latest.value() == null || latest.value() < message.getVersion()) {
            latest.update(message.getVersion());
        }
        else if (latest.value() > message.getVersion()) {
            throw new IllegalStateException("Reference " + message.getReference() + " version " + latest.value() + " before " + message.getVersion());
        }
        collector.collect(message);
    }
}
