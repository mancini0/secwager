package com.secwager.blockchain.constants

object Queries {
    const val SAVE_WALLET = "INSERT INTO wallet_data(save_time, bytes) values(current_timestamp, ?)"
    const val UPSERT_USER = "INSERT INTO USERS(user_id, pub_key, priv_key, p2pkh_addr, create_ts) \n" +
            "values (?,?,?,?,current_timestamp) ON CONFLICT DO nothing"
}