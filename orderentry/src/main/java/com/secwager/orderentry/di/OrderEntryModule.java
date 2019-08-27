package com.secwager.orderentry.di;

import static com.secwager.cashier.CashierGrpc.newFutureStub;

import com.secwager.cashier.CashierGrpc.CashierFutureStub;
import dagger.Module;
import dagger.Provides;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Optional;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.kafka.clients.producer.KafkaProducer;

@Module
public class OrderEntryModule {

  @Singleton
  @Provides
  @Named("cashierChannel")
  public ManagedChannel cashierGrpcChannel() {
    return ManagedChannelBuilder
        .forTarget("dns:///cashier")
        .usePlaintext()
        .maxRetryAttempts(0)
        .build();
  }

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

  @Provides
  @Singleton
  public CashierFutureStub provideCashierClient(@Named("cashierChannel") ManagedChannel channel) {
    return newFutureStub(channel);
  }

}
