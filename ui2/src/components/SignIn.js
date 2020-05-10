import React from 'react';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import { firebase } from '../firebase/Firebase';


export const uiConfig = {
  signInFlow: 'popup',
  signInSuccessUrl: '/',
  signInOptions: [
    firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    firebase.auth.PhoneAuthProvider.PROVIDER_ID
  ]
};

export const SignIn = (props) =>
  (

    <StyledFirebaseAuth uiConfig={uiConfig} firebaseAuth={firebase.auth()} />

  );
