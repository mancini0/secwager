package sandbox

import com.secwager.helloworld.GreeterGrpcKt
import com.secwager.helloworld.HelloReply
import com.secwager.helloworld.HelloRequest


class Sandbox
/**: GreeterGrpcKt.GreeterCoroutineImplBase()**/
{
    /**override**/
    suspend fun sayHello(request: HelloRequest): HelloReply {
        return HelloReply.newBuilder().build()
        
    }
}