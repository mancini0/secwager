package secwager.posttrade

interface InstrumentDao {

    fun saveInstrument(m: Map<Any, Any>)

}