import { createStore, combineReducers, applyMiddleware } from 'redux';
import authReducer from '../reducers/AuthReducer';
import thunk from 'redux-thunk';
import MarketDataReducer from '../reducers/MarketDataReducer';



const store = createStore(
    combineReducers(
        {
            auth: authReducer,
            marketData: MarketDataReducer
        }),
    applyMiddleware(thunk)
);



export default store;