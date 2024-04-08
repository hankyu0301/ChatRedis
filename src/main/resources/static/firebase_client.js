import {initializeApp} from "https://www.gstatic.com/firebasejs/9.11.0/firebase-app.js";
import {getMessaging, getToken, onMessage} from "https://www.gstatic.com/firebasejs/9.11.0/firebase-messaging.js";

// Web app's Firebase configuration
// Should be copied during project initialization
const firebaseConfig = {
    apiKey: "AIzaSyAcINlLhsrD102-fvvrp-uXn_zWulKSMjM",
    authDomain: "chatredis-a204a.firebaseapp.com",
    projectId: "chatredis-a204a",
    storageBucket: "chatredis-a204a.appspot.com",
    messagingSenderId: "695061780159",
    appId: "1:695061780159:web:aa38541992d05fe733580a",
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);
// URL to your site webhook for saving an user token.
const webhook_url = '/api/v1/fcm';
const tokenSentLocalTitle = 'tokenSentToServer';
const userId = localStorage.getItem('userId');

// Handle incoming messages. Called when:
// - a message is received while the app has focus
// - the user clicks on an app notification created by a service worker `messaging.onBackgroundMessage` handler.
onMessage(messaging, (payload) => {
    console.log('Message received. ', payload);

    // In case if need to show notification even for current focused tab of your page
    payload.data.data = JSON.parse(JSON.stringify(payload.data));
    navigator.serviceWorker.getRegistration('/firebase-cloud-messaging-push-scope').then(registration => {
        registration.showNotification(
            payload.data.title,
            payload.data
        )
    });
});

// Check if browser supports push-notifications
if (
    !('Notification' in window &&
        'serviceWorker' in navigator &&
        'localStorage' in window &&
        'fetch' in window &&
        'postMessage' in window)
) {
    if (!('Notification' in window)) {
        console.error('Notification not supported');
    } else if (!('serviceWorker' in navigator)) {
        console.error('ServiceWorker not supported');
    } else if (!('localStorage' in window)) {
        console.error('LocalStorage not supported');
    } else if (!('fetch' in window)) {
        console.error('fetch not supported');
    } else if (!('postMessage' in window)) {
        console.error('postMessage not supported');
    }

    console.warn('This browser does not support push-notifications.');
    console.log('Is HTTPS', window.location.protocol === 'https:');
    console.log('Support Notification', 'Notification' in window);
    console.log('Support ServiceWorker', 'serviceWorker' in navigator);
    console.log('Support LocalStorage', 'localStorage' in window);
    console.log('Support fetch', 'fetch' in window);
    console.log('Support postMessage', 'postMessage' in window);
} else {
    if (Notification.permission === 'granted') {
        getUserToken();
    } else {
        requestPermission();
    }
}

// Request user permission for push-notifications
function requestPermission() {
    console.log('Requesting permission...');
    Notification.requestPermission().then((permission) => {
        if (permission === 'granted') {
            console.log('Notification permission granted.');
            getUserToken();
        } else {
            console.log('Unable to get permission to notify.');
        }
    });
}

// Get user registration token
function getUserToken() {
    // Register a service worker to use script with github pages. As firebase required to store serviceWorker only in the root.
    if ("serviceWorker" in navigator) {
        navigator.serviceWorker
            .register("/firebase-push/firebase-messaging-sw.js")
            .then(function (registration) {
                console.log("Registration successful, scope is:", registration.scope);
                // Get registration token. Initially this makes a network call, once retrieved
                // subsequent calls to getToken will return from cache.
                getToken(messaging, {
                    vapidKey: 'BNcFMQiklaVSK9_O7rpTk7QB2JGQCWLWVc9vSfD7BgbhG9q8GHszBucBD8F1vCS5SIvQMdoPsYGTP2TwKFdsSdE',
                    serviceWorkerRegistration: registration
                }).then((currentToken) => {
                    if (currentToken) {
                        sendTokenToServer(currentToken);
                    } else {
                        // Show permission request.
                        console.log('No registration token available. Request permission to generate one.');

                        setTokenSentToServer(false);
                    }
                }).catch((err) => {
                    console.log('An error occurred while retrieving token. ', err);
                    setTokenSentToServer(false);
                });
            })
            .catch(function (err) {
                console.log("Service worker registration failed, error:", err);
            });
    }
}

// Send the registration token your application server, so that it can:
// - send messages back to this app
// - subscribe/unsubscribe the token from topics
function sendTokenToServer(currentToken) {
    if (!isTokenSentToServer()) {
        console.log('Sending token to server...');
        window.localStorage.setItem('token', currentToken);

        setTokenSentToServer(true);
        if (webhook_url) {
            $.post(webhook_url, {token: currentToken, userId: userId}, function () {
                console.log('Token sent to the server...');
            });
        }

    } else {
        console.log('Token already sent to server so won\'t send it again unless it changes');
    }
}

// Update local information about token been sent to the server
function setTokenSentToServer(sent) {
    window.localStorage.setItem(tokenSentLocalTitle, sent ? '1' : '0');
}

// Check local information if token was sent to server
function isTokenSentToServer() {
    return window.localStorage.getItem(tokenSentLocalTitle) === '1';
}