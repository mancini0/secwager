package secwager.stripe;

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.QueryResult
import kotlinx.coroutines.future.await
import io.ktor.util.pipeline.PipelineContext
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder


val connectionPool = PostgreSQLConnectionBuilder.createConnectionPool(
        "jdbc:postgresql://localhost:26257/secwager?user=secwager&sslmode=disable");

fun main(args: Array<String>) {


    val server = embeddedServer(Netty, 1738) {
        install(ContentNegotiation) {
            gson {}
        }
        routing {
            get("/webhook") {
                handlePostgresRequest("select 0")
            }
        }
    }
    connectionPool.connect().get()
    try {
        server.start(wait = true)
    } finally {
        connectionPool.disconnect().get()
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostgresRequest(query: String) {
    val queryResult = connectionPool.sendPreparedStatementAwait(query = query)
    call.respond(queryResult.rows[0][0].toString())
}

private suspend fun Connection.sendPreparedStatementAwait(query: String, values: List<Any> = emptyList()): QueryResult =
        this.sendPreparedStatement(query, values).await()