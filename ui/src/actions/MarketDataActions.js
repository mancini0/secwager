export const ADD_INSTRUMENT = 'ADD_INSTRUMENT';
export const UPDATE_DEPTH = 'UPDATE_DEPTH';
export const UPDATE_PRICE = 'UPDATE_PRICE';

export function addInstrument(instrument) {
    console.log('adding instrument');
    return { type: ADD_INSTRUMENT, instrument };
};

export const updateDepth = (depth) => ({
    type: UPDATE_DEPTH,
    isin: depth.isin,
    depth: depth.depth
});


export const updatePrice = (lastTrade) => ({
    type: UPDATE_PRICE,
    isin: lastTrade.isin,
    price: lastTrade.price,
    qty: lastTrade.qty
});