import firebase from 'firebase/app';
import 'firebase/auth';
import config from './firebase_config.json';

firebase.initializeApp(config);

 
export {firebase};
