import { ADD_INSTRUMENT, UPDATE_PRICE, UPDATE_DEPTH } from '../actions/MarketDataActions';
import produce from 'immer';

let initialState = {
    instrumentsByIsin: {},
    pricesByIsin: {},
    depthByIsin: {}
}


export default (state = initialState, action) => {
    switch (action.type) {
        case ADD_INSTRUMENT:
            return produce(state, draft => { draft['instrumentsByIsin'][action.instrument.getIsin()] = action.instrument });
        case UPDATE_PRICE:
            return produce(state, draft => {draft['pricesByIsin'][action.isin] = action.price});
        case UPDATE_DEPTH:
            return produce(state, draft => {draft['depthByIsin'][action.depth.symbol] = action.depth});
        default:
            return state;
    }
}