export const LOGIN  ='LOGIN';
export const LOGOUT ='LOGOUT';
import {firebase} from '../firebase/Firebase';

export function login(uid){
    return {type: LOGIN, uid};
};

export const logout = () => ({
    type: LOGOUT
  });
  

  export const startLogout = () => {
    return () => {
      return firebase.auth().signOut();
    };
  };  