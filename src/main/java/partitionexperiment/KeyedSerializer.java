package partitionexperiment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;

import java.io.Serializable;

public class KeyedSerializer implements KeyedSerializationSchema<Message>, Serializable {

    private transient Gson gson;

    @Override
    public byte[] serializeKey(Message element) {
        return element.reference.toString().getBytes();
    }

    @Override
    public byte[] serializeValue(Message element) {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson.toJson(element).getBytes();
    }

    @Override
    public String getTargetTopic(Message element) {
        return null;
    }
}
