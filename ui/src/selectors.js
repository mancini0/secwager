export const getContractsByLeague = (state, league) => {
    console.log(JSON.stringify(Object.values(state.marketData.instrumentsByIsin)));
    return Object.values(state.marketData.instrumentsByIsin).filter(instrument => instrument.getLeague() === league);
};
