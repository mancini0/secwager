package com.secwager.blockchain

import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.secwager.blockchain.constants.Queries;
import org.apache.commons.dbutils.QueryRunner
import org.bitcoinj.core.Address
import org.bitcoinj.core.ECKey
import org.bitcoinj.script.Script
import org.bitcoinj.wallet.Wallet
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class UserECKeyReceiver @Inject constructor(val wallet: Wallet, val queryRunner: QueryRunner) : MessageReceiver {
    companion object {
        private val log = LoggerFactory.getLogger(UserECKeyReceiver::class.java)
    }

    private val gson = Gson()


    override fun receiveMessage(msg: com.google.pubsub.v1.PubsubMessage, ackReplyConsumer: AckReplyConsumer) {
        log.info("msg: {}", msg)
        val userData: Map<String, String> = gson.fromJson(msg.data.toStringUtf8(), object : TypeToken<Map<String, String>>() {}.type)
        val pubKey = ECKey.fromPublicOnly(Base64.getDecoder().decode(userData.get("pubKeyBase64")))
        val p2pkhAddress = Address.fromKey(wallet.params, pubKey, Script.ScriptType.P2PKH).toString()
        wallet.importKey(ECKey.fromPublicOnly(Base64.getDecoder().decode(userData.get("pubKeyBase64"))))
        val outputStream = ByteArrayOutputStream();
        wallet.saveToFileStream(outputStream)
        queryRunner.execute(Queries.UPSERT_USER, userData.get("uid"), userData.get("pubKeyBase64"), userData.get("privKeyBase64"), p2pkhAddress)
        queryRunner.execute(Queries.SAVE_WALLET, Base64.getEncoder().encodeToString(outputStream.toByteArray()))
        queryRunner.dataSource.connection.commit();
        ackReplyConsumer.ack()
    }


}