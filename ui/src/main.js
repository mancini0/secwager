import * as React from "react";
import * as ReactDOM from "react-dom";
import { App } from './components/App'
import { firebase } from './firebase/Firebase';
import { login, logout } from './actions/AuthActions';
import { addInstrument } from './actions/MarketDataActions';
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
  'localhost:8080');

var req = new InstrumentRequest();
req.setLeague(League.EVERY_LEAGUE);
marketDataClient.getInstruments(req, {}, (err, result) => {
  if (err) {
    console.log('err:' + JSON.stringify(err));
  }
  else {
    result.getInstrumentsList().forEach(instrument => store.dispatch(addInstrument(instrument)));
  }
});



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


