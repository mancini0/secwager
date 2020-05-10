package com.secwager.orderentry.di;

import static com.secwager.proto.cashier.CashierGrpc.newStub;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.secwager.dao.order.OrderRepo;
import com.secwager.dao.order.OrderRepoImpl;
import com.secwager.proto.cashier.CashierGrpc.CashierStub;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dagger.Module;
import dagger.Provides;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Optional;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.postgresql.ds.PGSimpleDataSource;

@Module
public class OrderEntryModule {

  @Singleton
  @Provides
  @Named("cashierChannel")
  public ManagedChannel cashierGrpcChannel() {
    return ManagedChannelBuilder
        .forAddress("cashier", 9305)
        .usePlaintext()
        .build();
  }

  @Singleton
  @Provides
  public KafkaProducer<String, byte[]> provideKafkaOrderProducer() {
    Properties props = new Properties();

    props.put("bootstrap.servers",
        Optional.ofNullable(System.getenv("KAFKA_BOOTSTRAP_SERVERS"))
            .orElseGet(() -> "localhost:9092"));

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
  public CashierStub provideCashierClient(@Named("cashierChannel") ManagedChannel channel) {
    return newStub(channel);
  }

  @Provides
  @Singleton
  public FirebaseApp provideFirebase() {
    try {
      return FirebaseApp.initializeApp(FirebaseOptions.builder()
          .setProjectId("secwager")
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .setDatabaseUrl("https://secwager.firebaseio.com")
          .build());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  public DataSource provideDataSource() {
    HikariConfig config = new HikariConfig();
    config.setAutoCommit(false);
    config.setDataSourceClassName(PGSimpleDataSource.class.getName());
    config.setJdbcUrl(System.getenv("JDBC_URL"));
    config.setUsername(System.getenv("DB_USER"));
    config.setPassword(System.getenv("DB_PASSWORD"));
    return new HikariDataSource(config);
  }

  @Provides
  @Singleton
  public QueryRunner provideQueryRunner(DataSource dataSource) {
    return new QueryRunner(dataSource);
  }

  @Provides
  @Singleton
  public OrderRepo provideOrderRepo(QueryRunner queryRunner) {
    return new OrderRepoImpl(queryRunner);
  }
}
