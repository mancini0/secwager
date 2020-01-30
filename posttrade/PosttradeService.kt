package secwager.posttrade

import javax.inject.Inject;
import com.google.common.util.concurrent.AbstractExecutionThreadService
import kotlinx.coroutines.channels.Channel


class FooService : AbstractExecutionThreadService {


    @Inject
    constructor() {
    }

    override fun run() {
        while (this.isRunning) {

        }
    }
}