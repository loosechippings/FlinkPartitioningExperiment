# Flink Partitioning Experiement

### Set-up

Create a [local Kafka cluster](https://github.com/confluentinc/cp-docker-images/tree/5.3.1-post/examples/kafka-single-node).

### Topic

Create topic with 20 partitions.

### Producer

Use keyed serializer and use an apropriate field for key.

Use null partitioner so that Kafka handles partitioning and all topic partitions are used.

Key the DataStream before adding the sink, otherwise Flink will round-robin assign messages to instances of the sink and you'll get out of order messages.

### Consumer

Key DataStream before the order checking process function.

### Reinterpreted Partition Consumer

Throws null pointer exception from inside the managed state API - haven't worked out why.

Checking the execution plan shows that the Source -> Process function is a *forward* rather than a *hash*.
