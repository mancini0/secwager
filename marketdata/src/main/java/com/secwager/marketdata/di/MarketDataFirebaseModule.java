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
import javax.annotation.Nullable;


import javax.inject.Singleton;

@Module
public class MarketDataFirebaseModule {

    final Logger log = LoggerFactory.getLogger(MarketDataFirebaseModule.class);

    @Provides
    @Singleton
    public Firestore provideFirebase() {

        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
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
    public Map<String,Instrument> provideInstrumentCache(Firestore db) {
        Map<String, Instrument> instrumentsById = new ConcurrentHashMap<>(100);
        db.collection("instruments")
                .whereEqualTo("active", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirestoreException e) {
                        if (e != null) {
                           log.error("firebase market data listen failed: {}", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    Map<String,Object> data = dc.getDocument().getData();
                                    instrumentsById.put(dc.getDocument().getId(),toInstrument(data));
                                    break;
                                case REMOVED:
                                    instrumentsById.remove(dc.getDocument().getId());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
        return instrumentsById;
    }

    //move to commons library
    private Instrument toInstrument(Map<String,Object> doc)
    {
       return Instrument.newBuilder().build();
    }

}