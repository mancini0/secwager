package secwager.stripe

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.future.await
import java.util.concurrent.TimeUnit


fun main() {
    val server = embeddedServer(Netty, port = 3399) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }

            routing {
                get("/") {
                    println("handling");
                    handlePostgresRequest("select 1750")
                }
            }
        }
    }
    println("STARTING")
    connectionPool.connect().get()
    try {
        server.start(wait = true)
    } finally {
        println("DISCO")
        connectionPool.disconnect().get()
    }
}


val connectionPool = PostgreSQLConnectionBuilder.createConnectionPool {
    username = "secwager"
    host = "localhost"
    port = 26257
    database = "secwager"
    maxActiveConnections = 100
    maxIdleTime = TimeUnit.MINUTES.toMillis(15)
    maxPendingQueries = 10_000
    connectionValidationInterval = TimeUnit.SECONDS.toMillis(30)
}


private suspend fun PipelineContext<Unit, ApplicationCall>.handlePostgresRequest(query: String) {
    val queryResult = connectionPool.sendPreparedStatementAwait(query = query)
    call.respond(queryResult.rows[0][0].toString())
}

private suspend fun Connection.sendPreparedStatementAwait(query: String, values: List<Any> = emptyList()): QueryResult =
        this.sendPreparedStatement(query, values).await()