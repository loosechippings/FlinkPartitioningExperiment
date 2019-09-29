package partitionexperiment;

import java.util.Objects;

public class Message {

    Integer reference;
    Integer version;

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Message(Integer reference, Integer version) {
        this.reference = reference;
        this.version = version;
    }

    public Message() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(reference, message.reference) &&
                Objects.equals(version, message.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, version);
    }

    @Override
    public String toString() {
        return "Message{" +
                "reference=" + reference +
                ", version=" + version +
                '}';
    }
}
