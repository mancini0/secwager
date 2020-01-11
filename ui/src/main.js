import * as React from "react";
import * as ReactDOM from "react-dom";
import TraderDashboard from './components/TraderDashboard'
import { firebase } from './firebase/Firebase';
import { login, logout } from './actions/AuthActions';
import { addInstrument, updatePrice } from './actions/MarketDataActions';
import { Provider } from 'react-redux';
import { MemoryRouter, Route, Switch } from 'react-router-dom';
import store from './store/store'
import { NotFound } from "./components/NotFound";
import './styles/styles.css';
const { MarketDataServiceClient } = require('./proto/market_data_grpc_web_pb.js');
const { MarketDataRequest, League, MarketDataResponse } = require('./proto/market_data_pb.js');

ReactDOM.render(

  <Provider store={store}>
    <MemoryRouter>
      <Switch>
        <Route path="/" exact={true} component={TraderDashboard} />
        <Route component={NotFound} />
      </Switch>
    </MemoryRouter>
  </Provider>, document.getElementById("root"));

console.log(`market data url is ${process.env.MARKET_DATA_URL}`);
var marketDataClient = new MarketDataServiceClient('http://' + process.env.MARKET_DATA_URL);



//set up market data listener
var req = new MarketDataRequest();
req.setLeague(League.EVERY_LEAGUE);
var stream = marketDataClient.subscribeToMarketData(req);
stream.on('data', function(res) {
  res.getInstrumentsList().forEach(i=>{
  store.dispatch(addInstrument(i));
  store.dispatch(updatePrice({isin:i.getIsin(), price: i.getLastTrade().getPrice(), qty:i.getLastTrade().getQty()}));
  });
  });

stream.on('status', function(status) {
  console.log("s:"+ status.code);
  console.log("s:"+ status.details);
  console.log("s:"+ status.metadata);
});

stream.on('end', function(end) {
  console.log('stream ended');
});


//listen for auth changes
firebase
  .auth()
  .onAuthStateChanged((user) => {
    if (user) {
      store.dispatch(login(user));
    } else {
      console.log("LOGGING OUT");
      store.dispatch(logout());
    }
  });


