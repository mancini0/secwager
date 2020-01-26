export const getContractsByLeague = (state, league) => {
    return Object.values(state.marketData.instrumentsByIsin).filter(instrument => instrument.getLeague() === league);
};


export const getDepthByIsin = (state, isin) => {
    return  state.marketData.depthByIsin[isin];
};