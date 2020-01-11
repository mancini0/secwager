package com.secwager.marketdata.di;

import com.secwager.marketdata.MarketData.Instrument;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.auth.oauth2.GoogleCredentials;
import static  com.google.cloud.firestore.DocumentChange.Type.*;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.EventListener;
import com.secwager.marketdata.ObserverNotifyingMarketDataEventListener;
import javax.inject.Singleton;

@Module
public class MarketDataFirebaseModule {

    final Logger log = LoggerFactory.getLogger(MarketDataFirebaseModule.class);

    @Provides
    @Singleton
    public Firestore provideFirebase() {

        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    //.setCredentials(GoogleCredentials.getApplicationDefault())
                    .setCredentials(GoogleCredentials.fromStream(getClass().getClassLoader().getResourceAsStream("service-key.json")))
                    .setDatabaseUrl("https://secwager.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
            return FirestoreClient.getFirestore();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    @Provides
    @Singleton
    public ObserverNotifyingMarketDataEventListener provideEventListener(Firestore db) {
        return new ObserverNotifyingMarketDataEventListener(db);
    }
}