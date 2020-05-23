const functions = require('firebase-functions');
const bitcoin = require('bitcoinjs-lib');
const {PubSub} = require('@google-cloud/pubsub');

 const pubSubClient = new PubSub();

 exports.assignEllipticCurveKeys = functions.auth.user().onCreate(async (user) => {
    const keyPair = bitcoin.ECPair.makeRandom();
    const data  = {
        'uid': user.uid, 
        'pubKeyBase64': keyPair.publicKey.toString('base64'), 
        'privKeyBase64': keyPair.privateKey.toString('base64')
    };
    const dataBuffer = Buffer.from(JSON.stringify(data))
    await pubSubClient.topic('user-ec-keys').publish(dataBuffer);
 });