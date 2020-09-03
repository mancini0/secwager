package com.secwager.orderentry

import com.google.gson.JsonParser
import io.grpc.*
import java.util.*


class JwtServerInterceptor : ServerInterceptor {
    companion object {
        val UID_CTX_KEY = Context.key<String>("uid")
        val ENCODED_JWT_PAYLOAD_KEY = "jwt-payload-base64"


    }

    override fun <ReqT : Any, RespT : Any> interceptCall(call: ServerCall<ReqT, RespT>, headers: Metadata,
                                                         next: ServerCallHandler<ReqT, RespT>): ServerCall.Listener<ReqT> {
        headers.get(Metadata.Key.of(ENCODED_JWT_PAYLOAD_KEY,
                Metadata.ASCII_STRING_MARSHALLER))?.let {
            val uid = JsonParser.parseString(String(Base64.getDecoder().decode(it)))
                    .asJsonObject["sub"]?.asString
            val ctx = Context.current().withValue(UID_CTX_KEY, uid)
            return Contexts.interceptCall(ctx, call, headers, next)
        }
        call.close(Status.UNAUTHENTICATED, headers)
        return object : ServerCall.Listener<ReqT>() {}
    }
}