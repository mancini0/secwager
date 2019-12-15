

class InstrumentRepoIgniteImpl @Inject
constructor(private val queryRunner: QueryRunner) : InstrumentRepo {
    internal val log = LoggerFactory.getLogger(InstrumentRepoIgniteImpl::class.java)
    private val leagueMap: Map<String, League>

    init {
        leagueMap = HashMap()
        leagueMap.put("EPL", League.ENGLISH_PREMIER_LEAGUE)
        leagueMap.put("LIGUE_1", League.LIGUE_1)
        leagueMap.put("LA_LIGA", League.LA_LIGA)
        leagueMap.put("SERIE_A", League.SERIE_A)
        leagueMap.put("UCL", League.UEFA_CHAMPIONS_LEAGUE)
        leagueMap.put("UEL", League.UEFA_EUROPA_LEAGUE)
    }

    @Override
    fun findAllActiveInstruments(): Set<Instrument> {
        try {
            val rows = queryRunner
                    .query(Queries.ALL_ACTIVE_INSTRUMENTS, MapListHandler())
            log.info("Found {} rows", rows.size())
            return rows.stream().map({ row ->
                Instrument.newBuilder()
                        .setDescription(row.get("DESCRIPTION") as String)
                        .setIsin(row.get("ISIN") as String)
                        .setActive(true)
                        .setLeague(leagueMap[row.get("LEAGUE_ID") as String])
                        .setStartTimeEpochSeconds((row.get("GAME_TIME") as java.sql.Timestamp).getTime())
                        .build()
            }).collect(Collectors.toSet())
        } catch (e: SQLException) {
            log.error("Unable to retrieve active instruments due to exception {}", e)
            return Collections.emptySet()
        }

    }
}
