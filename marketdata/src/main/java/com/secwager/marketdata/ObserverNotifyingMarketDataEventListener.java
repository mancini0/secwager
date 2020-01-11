package com.secwager.marketdata;
import static  com.google.cloud.firestore.DocumentChange.Type.*;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.EventListener;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.secwager.Market.DepthBook;
import com.secwager.Market.LastTrade;
import io.grpc.stub.StreamObserver;
import com.secwager.marketdata.MarketData.MarketDataResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.secwager.marketdata.MarketData.Instrument;


public class ObserverNotifyingMarketDataEventListener implements EventListener<QuerySnapshot> {

    private final Logger log = LoggerFactory.getLogger(ObserverNotifyingMarketDataEventListener.class);
    private final Set<StreamObserver<MarketDataResponse>> observers;
    private final Map<String,Instrument> instrumentsById;

    @Inject public ObserverNotifyingMarketDataEventListener(Firestore db){
        this.observers=new HashSet<>();
        this.instrumentsById=new ConcurrentHashMap<>();
        db.collection("instruments")
                .whereEqualTo("active", true)
                .addSnapshotListener(this);
    }


    private Instrument toInstrument(Map<String,Object> doc)
    {
        try {

            Instrument.Builder instrumentBuilder = Instrument.newBuilder()
                    .setIsin((String) doc.get("isin"))
                    .setDescription("" + doc.get("away") + " at " + doc.get("home"))
                    .setStartTimeEpochSeconds((long) doc.get("gameTime"))
                    .setLeague(MarketData.League.valueOf((String) doc.get("league")))
                    .setActive((boolean) doc.get("active"));

            DepthBook.Builder depthBuilder = DepthBook.newBuilder();

            Optional.ofNullable(doc.get("depth"))
                    .map(depth -> ((Map) depth).get("asks"))
                    .map(asks -> ((List<Map<String, Object>>) asks))
                    .ifPresent(asks -> asks.forEach(level -> {
                        depthBuilder.addAskQtys(((Long) level.get("qty")).intValue());
                        depthBuilder.addAskPrices(((Long) level.get("price")).intValue());
                    }));

            Optional.ofNullable(doc.get("depth"))
                    .map(depth -> ((Map) depth).get("bids"))
                    .map(bids -> ((List<Map<String, Object>>) bids))
                    .ifPresent(bids -> bids.forEach(level -> {
                        depthBuilder.addBidQtys(((Long)(level.get("qty"))).intValue())
                                .addBidPrices(((Long) level.get("price")).intValue());
                    }));

            Optional.ofNullable(doc.get("lastTrade")).ifPresent(lastTrade ->
                    instrumentBuilder.setLastTrade(LastTrade.newBuilder().setPrice(((Long)
                            ((Map<String, Object>) lastTrade).get("price")).intValue())
                            .setQty(((Long) ((Map<String, Object>) lastTrade).get("qty")).intValue())
                            .build()));

            instrumentBuilder.setDepth(depthBuilder.build());
            return instrumentBuilder.build();
        }catch (Throwable t){
            log.error("whoopsies, {}", t);
        }
        return null;
    }


    private MarketDataResponse createMarketDataResponse(Set<String> ids){
        return MarketDataResponse.newBuilder().addAllInstruments(
        ids.stream()
                .map(id->instrumentsById.get(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
        ).build();
    }


    private void notifyObservers(MarketDataResponse response) {

        observers.forEach(o -> {
            try {
                o.onNext(response);
            } catch (Exception e) {
                log.error("exception encountered while notifying observer: {}", e);
            }
        });
    }


    @Override
    public void onEvent(@Nullable QuerySnapshot snapshots,
                        @Nullable FirestoreException e) {
        log.info("onEvent fired");
        if (e != null) {
            log.error("firebase market data listen failed: {}", e);
            return;
        }
        Set<String> ids = new HashSet<>();
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            switch (dc.getType()) {
                case ADDED:
                case MODIFIED:
                    Map<String,Object> data = dc.getDocument().getData();
                    log.info("modified: {}", data);
                    Instrument i = toInstrument(data);
                    instrumentsById.put(dc.getDocument().getId(),i);
                    log.info("new size: {}", instrumentsById.size());
                    break;
                case REMOVED:
                    log.info("removed: {}", dc.getDocument().getId());
                    instrumentsById.remove(dc.getDocument().getId());
                    break;
                default:
                    break;
            }
        ids.add(dc.getDocument().getId());
        }
        notifyObservers(createMarketDataResponse(ids));
    }

    public void addObserver(StreamObserver<MarketDataResponse> observer){
        observer.onNext(createMarketDataResponse(instrumentsById.keySet()));
        this.observers.add(observer);
    }

    public void removeObserver(StreamObserver<MarketDataResponse> observer){
        this.observers.remove(observer);
    }




}