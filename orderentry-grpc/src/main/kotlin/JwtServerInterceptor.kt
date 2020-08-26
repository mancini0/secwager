package com.secwager.orderentry

import com.google.gson.JsonParser
import io.grpc.*
import org.slf4j.LoggerFactory
import java.util.*


class JwtServerInterceptor : ServerInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(call: ServerCall<ReqT, RespT>, headers: Metadata,
                                                           next: ServerCallHandler<ReqT, RespT>): ServerCall.Listener<ReqT> {

        headers.get(Metadata.Key.of("jwt-payload-base64",
                Metadata.ASCII_STRING_MARSHALLER))?.let {
            val uid = JsonParser.parseString(String(Base64.getDecoder().decode(it)))
                    .asJsonObject["sub"]?.asString
            val ctx = Context.current().withValue(Context.key("uid"), uid)
            return Contexts.interceptCall(ctx, call, headers, next)
        }
        call.close(Status.UNAUTHENTICATED, headers)
        return object:ServerCall.Listener<ReqT>(){}
    }
}