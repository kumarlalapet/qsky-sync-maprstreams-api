package com.qcom.search.qsky.api.sync.rest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by mlalapet on 8/17/16.
 */
@Component
@Scope("singleton")
public class MessageProducer {

    enum Operation {
        CREATE, UPDATE, DONE
    }
    @Value("${cu.topic}")
    private String createTopic;

    @Value("${done.topic}")
    private String doneTopic;

    private KafkaProducer producer;

    public MessageProducer(){
        init();
    }

    private void init() {
        Properties props = new Properties();
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);

    }

    boolean sendMessage(String key, String message, Operation operation) {
        String topicStr = this.createTopic;
        if(operation.equals(Operation.DONE))
            topicStr = this.doneTopic;

        ProducerRecord rec = new ProducerRecord(topicStr, key, message);
        Future<RecordMetadata> response = producer.send(rec);

        return true;
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Shutting down Kafka Producer...");
        this.producer.close();
    }
}
