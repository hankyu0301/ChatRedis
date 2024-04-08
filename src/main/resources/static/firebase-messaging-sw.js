// Give the ServiceWorker access to Firebase Messaging.
// In version 9 it uses modules, so to simplify will use a 'compat' version for the ServiceWorker.
importScripts('https://www.gstatic.com/firebasejs/9.11.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.11.0/firebase-messaging-compat.js');

// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
const firebaseConfig = initializeApp({
    apiKey: "AIzaSyAcINlLhsrD102-fvvrp-uXn_zWulKSMjM",
    authDomain: "chatredis-a204a.firebaseapp.com",
    projectId: "chatredis-a204a",
    storageBucket: "chatredis-a204a.appspot.com",
    messagingSenderId: "695061780159",
    appId: "1:695061780159:web:aa38541992d05fe733580a",
    measurementId: "G-NXDMK8GD6L"
});

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function (payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);

    // Copy data object to get parameters in the click handler
    payload.data.data = JSON.parse(JSON.stringify(payload.data));

    return self.registration.showNotification(payload.data.title, payload.data);
});

// Open/focus link in a notification message.
self.addEventListener('notificationclick', function (event) {
    const target = event.notification.data.click_action || '/';
    event.notification.close();

    event.waitUntil(clients.matchAll({
        type: 'window',
        includeUncontrolled: true
    }).then(function (clientList) {
        for (let i = 0; i < clientList.length; i++) {
            const client = clientList[i];
            if (client.url === target && 'focus' in client) {
                return client.focus();
            }
        }

        return clients.openWindow(target);
    }));
});