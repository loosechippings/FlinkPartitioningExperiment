package s3experiment;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class Accumulator extends KeyedProcessFunction<Byte, String, String> {
    ValueState<String> state;

    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<String> desc = new ValueStateDescriptor<String>(
                "state",
                String.class
        );
        state = getRuntimeContext().getState(desc);
    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
        String s = state.value();
        if ( s == null ) {
            s = value;
        } else {
            s = s + ", " + value;
        }
        state.update(s);
        out.collect(s);
    }
}
