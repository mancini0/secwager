import * as React from "react";
import * as ReactDOM from "react-dom";
import Header from './components/Header';
import store from './store/store';
import {Provider} from 'react-redux';
import { MemoryRouter, Route, Switch } from 'react-router-dom';
import './styles/styles.css';
import { firebase } from './firebase/Firebase';
import { login, logout } from './actions/AuthActions';

ReactDOM.render(

<Provider store={store}>
   <MemoryRouter>
        <Switch>
          <Route path="/" exact={true} component={Header} />
        </Switch>
      </MemoryRouter>
</Provider>  ,
  document.getElementById("root")
);


firebase
  .auth()
  .onAuthStateChanged((user) => {
    if (user) {
      store.dispatch(login(user));
    } else {
      store.dispatch(logout());
    }
  });