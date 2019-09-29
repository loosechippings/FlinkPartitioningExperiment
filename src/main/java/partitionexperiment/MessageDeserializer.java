package partitionexperiment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class MessageDeserializer implements DeserializationSchema<Message> {

    private Gson gson;

    public Message deserialize(byte[] bytes) throws IOException {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        Message m = gson.fromJson(new String(bytes), Message.class);
        return m;
    }

    public boolean isEndOfStream(Message message) {
        return false;
    }

    public TypeInformation<Message> getProducedType() {
        return TypeInformation.of(Message.class);
    }
}
