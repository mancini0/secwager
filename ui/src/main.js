import * as React from "react";
import * as ReactDOM from "react-dom";
import {App} from './components/App'
import {firebase} from './firebase/Firebase';
import {login,logout} from './actions/AuthActions';
import {Provider} from 'react-redux';
import {MemoryRouter,Route, Switch} from 'react-router-dom';
import store from './store/store'
import LoginModal from "./components/LoginModal";
import {NotFound} from "./components/NotFound";
import './styles/styles.css';

ReactDOM.render(

<Provider store={store}>
  <MemoryRouter>
    <Switch>
      <Route path="/" exact={true} component={App}/>
      <Route path="/login" exact={true} component={LoginModal}/> 
      <Route component={NotFound}/>
    </Switch>
  </MemoryRouter>
</Provider>, document.getElementById("root"));



firebase.auth().onAuthStateChanged((user)=>{
    if(user){
        store.dispatch(login(user));
    }else{
        console.log("LOGGING OUT");
        store.dispatch(logout());
    }
});