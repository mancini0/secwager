package secwager.posttrade

import org.apache.kafka.clients.consumer.KafkaConsumer
import javax.inject.Inject;
import com.google.common.util.concurrent.AbstractExecutionThreadService
import kotlinx.coroutines.channels.Channel
import com.secwager.Market.DepthBook;
import com.secwager.Market.LastTrade;
import java.time.Duration



class FooService : AbstractExecutionThreadService {


    val kafkaConsumer: KafkaConsumer<String, ByteArray>
    val dao: InstrumentDao


    @Inject
    constructor(kafkaConsumer: KafkaConsumer<String, ByteArray>, dao: InstrumentDao) {
        this.kafkaConsumer = kafkaConsumer
        this.dao = dao
    }

    override fun run() {
        kafkaConsumer.subscribe(listOf("market-data-matched-trades", "market-data-depth"))
        while (true) {
            val pendingWrites = mapOf<String,Map<String,Any>>()
            for (record in kafkaConsumer.poll(Duration.ofMillis(1500))) {
                val payload = when (record.topic()) {
                    "market-data-depth" -> {
                        val depth = DepthBook.parseFrom(record.value())
//                        val bidLevels = (depth.getBidPrices() zip depth.getBidQtys()).map(
//                        val askLevels = (depth.getAskPrices() zip depth.getAskQtys())

                    }
                    "market-data-matched-trades" -> LastTrade.parseFrom(record.value())
                    else -> ""
                }
            }
        }
    }


}