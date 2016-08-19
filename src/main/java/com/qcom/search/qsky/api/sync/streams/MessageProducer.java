package com.qcom.search.qsky.api.sync.streams;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Only one instance of KafkaProducer is created. MapR internally has multiple threads to manage
 * sending messages to Streams optimally.
 *
 * And also one instance of KafkaProducer is used for both Sync and Async operation. This cab be
 * tweaked to have two different KafkaProducers based on the performance analysis
 *
 * Created by mlalapet on 8/17/16.
 */
@Component
@Scope("singleton")
public class MessageProducer {

    public enum OPERATION {
        SYNC, ASYNC
    }
    @Value("${cud.topic}")
    private String createUpdateDoneTopic;

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

    public boolean sendMessage(String key, String message, OPERATION operation) {
        String topicStr = this.createUpdateDoneTopic;

        ProducerRecord rec = new ProducerRecord(topicStr, key, message);
        Future<RecordMetadata> future = producer.send(rec);

        if(operation.equals(OPERATION.SYNC)) {
            producer.flush();
            RecordMetadata metadata = null;

            try {
                metadata = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(metadata == null) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Shutting down Kafka Producer...");
        this.producer.close();
    }
}