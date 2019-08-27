package com.secwager.orderentry.di;

import dagger.Module;
import dagger.Provides;
import java.util.Optional;
import java.util.Properties;
import javax.inject.Singleton;
import org.apache.kafka.clients.producer.KafkaProducer;

@Module
public class OrderEntryModule {


  @Singleton
  @Provides
  //TODO throw or return Optional if properties are unset or invalid(dagger does not like exceptions thrown during dependency construction)
  public KafkaProducer<String, byte[]> provideKafkaOrderProducer() {
    Properties props = new Properties();

    props.put("bootstrap.servers",
        Optional.ofNullable(System.getenv("KAFKA_BOOTSTRAP_SERVERS"))
            .orElseGet(() -> "localhost:9092"));
    props.put("enable.idempotence", "true");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
    props.put("transactional.id", Optional.ofNullable(System.getenv("POD_NAME_WITH_ORDINAL"))
        .orElseGet(() -> "orderentry-x"));
    KafkaProducer<String, byte[]> orderProducer = new KafkaProducer<>(props);
    orderProducer.initTransactions();
    return orderProducer;
  }
}
