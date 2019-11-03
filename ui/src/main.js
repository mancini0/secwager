import * as React from "react";
import * as ReactDOM from "react-dom";
import App from './components/App'
import { firebase } from './firebase/Firebase';
import { login, logout } from './actions/AuthActions';
import { addInstrument, updatePrice } from './actions/MarketDataActions';
import { Provider } from 'react-redux';
import { MemoryRouter, Route, Switch } from 'react-router-dom';
import store from './store/store'
import LoginModal from "./components/LoginModal";
import { NotFound } from "./components/NotFound";
import './styles/styles.css';
const { MarketDataServiceClient } = require('./proto/market_data_grpc_web_pb.js');
const { InstrumentRequest, League, InstrumentResponse } = require('./proto/market_data_pb.js');

ReactDOM.render(

  <Provider store={store}>
    <MemoryRouter>
      <Switch>
        <Route path="/" exact={true} component={App} />
        <Route path="/login" exact={true} component={LoginModal} />
        <Route component={NotFound} />
      </Switch>
    </MemoryRouter>
  </Provider>, document.getElementById("root"));

var marketDataClient = new MarketDataServiceClient('http://' +/*window.location.host*/
  'localhost:10000');


//listen for new games
var req = new InstrumentRequest();
req.setLeague(League.EVERY_LEAGUE);
marketDataClient.getInstruments(req, {}, (err, result) => {
  if (err) {
    console.log('err:' + JSON.stringify(err));
  }
  else {
    console.log('found instruments: ' + JSON.stringify(result.getInstrumentsList()));
    result.getInstrumentsList().forEach(instrument => store.dispatch(addInstrument(instrument)));
  }
});


//listen for price changes

setInterval(() => {
  let price = Math.floor(Math.random() * 100) + 1;
  let isins = ['BRHTOT8AEPL', 'MUNARS7AEPL', 'NORAVA8AEPL']
  let isin = isins[Math.floor(Math.random() * isins.length)];
  console.log(`fake price data: ${isin} ${price}`);
  store.dispatch(updatePrice({ isin, price }));
}, 5000);




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


