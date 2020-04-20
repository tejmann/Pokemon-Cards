const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

const END_GAME = 20

//Creates a new Game document if any player joins a room.
exports.createGame = functions.firestore
    .document('rooms/{userId}')
    .onUpdate(async (change, context) => {
        const joiner = change.after.data().joiner;
        const name = change.after.data().name;
        const status = change.after.data().status;
        if (joiner != null && status === "EMPTY") {
            return change.after.ref.collection("game").add({
                "creator": name, "joiner": joiner,
                "turn": name,
                "other_player": joiner,
                "move": 'SET',
                "draw": 'NO',
                "creator_score": 0,
                "joiner_score": 0,
                "curr_stat": null,
                "old_stat": null,
                "tie": 0,
                "left_game": null,
                "number": 0,
                "winner": null
            });
        }
        return null;
    });


//Updates the room to have a reference of its game.
exports.updateRoom = functions.firestore
    .document('rooms/{roomId}/game/{gameId}')
    .onCreate((doc) => {
        const path = doc.ref.path;
        const room = doc.ref.parent.parent;
        return room.update({"game_path": path, "status": "IN_PLAY"});
    })

// Computes the winner of each turn.
exports.onSetStat = functions.firestore
    .document('rooms/{roomId}/game/{gameId}')
    .onUpdate((change) => {
        const after = change.after.data();
        const oldStat = after.old_stat;
        const currStat = after.curr_stat;
        const otherPlayer = after.other_player as string;
        const turn = after.turn as string;
        const creator = after.creator;
        const joiner = after.joiner;
        if (after.move === "SET" && currStat != null && oldStat == null) {
            return change.after.ref.update({
                "move": 'COMPARE',
                "turn": otherPlayer,
                "old_stat": currStat,
                "curr_stat": null,
                "draw": 'NO',
                "other_player": turn
            });
        } else if (after.move === "COMPARE" && currStat != null && oldStat != null) {
            const old = parseInt(oldStat['baseStat']);
            const curr = parseInt(currStat['baseStat'])
            const newNumber = after.number + 1;
            let winner: string;
            let loser;
            let scoreField;
            if (old > curr) {
                winner = otherPlayer
                loser = turn;
            } else if (old < curr) {
                winner = turn;
                loser = otherPlayer;
            } else {
                winner = otherPlayer;
                loser = turn;
            }
            if (winner === creator) {
                scoreField = "creator_score";
            } else if (winner === joiner) {
                scoreField = "joiner_score";
            }
            const score = after[scoreField] as number;
            const updatedScore = score + 1;
            console.log(typeof updatedScore);
            console.log(scoreField);
            console.log(updatedScore);
            let draw;
            if (newNumber === END_GAME) {
                draw = "NO";
            }
            else {
                draw = "YES"
            }
            return change.after.ref.update({
                "move": 'SET',
                "turn": winner,
                "old_stat": null,
                [scoreField]: updatedScore,
                "curr_stat": null,
                "draw": draw,
                "other_player": loser,
                "number": newNumber
            });
        }
        return null;
    })

exports.setWinner = functions.firestore
    .document('rooms/{roomId}/game/{gameId}')
    .onUpdate(change => {
        const after = change.after.data();
        const number = after.number;
        if (number != END_GAME) {
            return;
        } else {
            const creator = after.creator;
            const joiner = after.joiner;
            const creatorScore = after.creator_score;
            const joinerScore = after.joiner_score;
            let winner;
            if (creatorScore > joinerScore) {
                winner = creator;
            } else {
                winner = joiner;
            }
            return change.after.ref.update({"winner": winner});
        }
    })


exports.leftGame = functions.firestore
    .document('rooms/{roomId}/game/{gameId}')
    .onUpdate(async (change) => {
        const after = change.after.data();
        const room = change.after.ref.parent.parent;
        const left = after.left_game;
        const end = after.end_game;
        if (left == null && end == null) {
            return null;
        }
        else {
            return change.after.ref.delete()
                .then(() => {
                    room.delete();
                })

        }
    })


