const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();
const firestore = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.processAddFavorite = functions.firestore.document('favorites/{favoriteId}').onCreate((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        favorisedBy: admin.firestore.FieldValue.arrayUnion(item.userId)
    });
});

exports.processDeleteFavorite = functions.firestore.document('favorites/{favoriteId}').onDelete((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        favorisedBy: admin.firestore.FieldValue.arrayRemove(item.userId)
    });
});

exports.processAddRating = functions.firestore.document('ratings/{ratingId}').onCreate((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        ratedBy: admin.firestore.FieldValue.arrayUnion(item.userId)
    });
});

exports.processDeleteRating = functions.firestore.document('ratings/{ratingId}').onDelete((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        ratedBy: admin.firestore.FieldValue.arrayRemove(item.userId)
    });
});

exports.processAddComment = functions.firestore.document('comments/{commentId}').onCreate((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        commentedBy: admin.firestore.FieldValue.arrayUnion(item.userId)
    });
});

exports.processDeleteComment = functions.firestore.document('comments/{commentId}').onDelete((snap, context) => {
    const item = snap.data();
    firestore.doc(`wines/${item.wineId}`).update({
        commentedBy: admin.firestore.FieldValue.arrayRemove(item.userId)
    });
});