//package tej.mann.common.views
//
//const functions = require('firebase-functions');
//
//const admin = require('firebase-admin');
//admin.initializeApp();
//
//
//class Game {
//    constructor(creator, joiner, turn, other_player, move, draw, creator_score, joiner_score, curr_stat, old_stat, tie, left_game) {
//        this.creator = creator;
//        this.joiner = joiner;
//        this.turn = turn;
//        this.other_player = other_player;
//        this.move = move;
//        this.draw = draw;
//        this.creator_score = creator_score;
//        this.joiner_score = joiner_score;
//        this.curr_stat = curr_stat;
//        this.old_stat = old_stat;
//        this.tie = tie;
//        this.left_game = left_game;
//    }
//
//    getScoreField(name) {
//        if(this.creator === name) {
//            return "creator_score";
//        }
//        else if (this.joiner === name) {
//            return "joiner_score";
//        }
//    }
//
//    getScore(name) {
//        if(this.creator === name) {
//            return this.creator_score;
//        }
//        else if (this.joiner === name) {
//            return this.joiner_score;
//        }
//        else if (name === "tie") {
//            return this.tie;
//        }
//    }
//
//    updatedScore(name) {
//        if(this.creator === name) {
//            return this.creator_score + 1;
//        }
//        else if (this.joiner === name) {
//            return this.joiner_score + 1;
//        }
//        else if (name === "tie") {
//            return this.tie + 1;
//        }
//    }
//}
//
//
// "creator": name, "joiner": joiner,
//     "turn": name,
//     "other_player": joiner,
//     "move": 'SET',
//     "draw": 'NO',
//     [name]: 0,
//     [joiner]: 0,
//     "stat": null,
//     "old_stat": null,
//     "tie": 0,
//     "left_game": null
//
////Creates a new Game document if any player joins a room.
//exports.createGame = functions.firestore
//.document('rooms/{userId}')
//.onUpdate(async (change, context) => {
//    const joiner = change.after.data().joiner;
//    const name = change.after.data().name;
//    const status = change.after.data().status;
//    if (joiner != null && status === "EMPTY") {
//        const game = new Game(name, joiner, name, joiner, 'SET', 'NO', 0, 0, null, 0, null);
//        return change.after.ref.collection("game").add({
//            game
//        });
//    }
//    return null;
//});
//
////Updates the room to have a reference of its game.
//exports.updateRoom = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onCreate((doc, context) => {
//    const path = doc.ref.path;
//    const room = doc.ref.parent.parent;
//    return room.update({"game_path": path, "status": "IN_PLAY"});
//})
//
//// Computes the winner of each turn.
//exports.onSetStat = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onUpdate((change) => {
//    const after = change.after.data();
//    const oldStat = after.old_stat;
//    const currStat = after.curr_stat;
//    const otherPlayer = after.other_player as String;
//    const turn = after.turn as String;
//    if (after.move === "SET" && currStat != null && oldStat == null) {
//        return change.after.ref.update({
//            "move": 'COMPARE',
//            "turn": otherPlayer,
//            "old_stat": currStat,
//            "stat": null,
//            "draw": 'NO',
//            "other_player": turn
//        });
//    } else if (after.move === "COMPARE" && currStat != null && oldStat != null) {
//        const old = parseInt(oldStat['baseStat']);
//        const curr = parseInt(currStat['baseStat']);
//        let scoreField;
//        let winner;
//        let loser;
//        let newScore;
//        if (old > curr) {
//            winner = otherPlayer
//            loser = turn;
//            scoreField = after.getScoreField(winner);
//            newScore = after.updatedScore(winner);
//        } else if (old < curr) {
//            winner = turn;
//            loser = otherPlayer;
//            scoreField = after.getScoreField(winner);
//            newScore = after.updatedScore(winner);
//        } else {
//            winner = otherPlayer;
//            loser = turn;
//            scoreField = "tie";
//            newScore = after.updatedScore(scoreField);
//        }
//        console.log(scoreField);
//        console.log(newScore);
//        return change.after.ref.update({
//            "move": 'SET',
//            "turn": winner,
//            "old_stat": null,
//            [scoreField]: newScore,
//            "stat": null,
//            "draw": "YES",
//            "other_player": loser
//        });
//    }
//    return null;
//})
//
//exports.leftGame = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onUpdate(async (change) => {
//    const after = change.after.data() as Game;
//    const room = change.after.ref.parent.parent;
//    const left = after.left_game;
//    if (left == null) {
//        return null;
//    } else {
//        return change.after.ref.delete()
//            .then(() => {
//                room.delete()
//            })
//
//    }
//})
//
//



//ts %%
//const functions = require('firebase-functions');
//
//const admin = require('firebase-admin');
//admin.initializeApp();
//
//
//class Game {
//    creator: any;
//    joiner: any;
//    turn: any;
//    other_player: any;
//    move: any;
//    draw: any;
//    creator_score: any;
//    joiner_score: any;
//    curr_stat: any;
//    old_stat: any;
//    tie: any;
//    left_game: any;
//    constructor(creator, joiner, turn, other_player, move, draw, creator_score, joiner_score, curr_stat, old_stat, tie, left_game) {
//        this.creator = creator;
//        this.joiner = joiner;
//        this.turn = turn;
//        this.other_player = other_player;
//        this.move = move;
//        this.draw = draw;
//        this.creator_score = creator_score;
//        this.joiner_score = joiner_score;
//        this.curr_stat = curr_stat;
//        this.old_stat = old_stat;
//        this.tie = tie;
//        this.left_game = left_game;
//    }
//
//    getScoreField(name) {
//        if(this.creator === name) {
//            return "creator_score";
//        }
//        else if (this.joiner === name) {
//            return "joiner_score";
//        }
//    }
//
//    getScore(name) {
//        if(this.creator === name) {
//            return this.creator_score;
//        }
//        else if (this.joiner === name) {
//            return this.joiner_score;
//        }
//        else if (name === "tie") {
//            return this.tie;
//        }
//    }
//
//    updatedScore(name) {
//        if(this.creator === name) {
//            return this.creator_score + 1;
//        }
//        else if (this.joiner === name) {
//            return this.joiner_score + 1;
//        }
//        else if (name === "tie") {
//            return this.tie + 1;
//        }
//    }
//}
//
//
////Creates a new Game document if any player joins a room.
//exports.createGame = functions.firestore
//.document('rooms/{userId}')
//.onUpdate(async (change, context) => {
//    const joiner = change.after.data().joiner;
//    const name = change.after.data().name;
//    const status = change.after.data().status;
//    if (joiner != null && status === "EMPTY") {
//        const game = new Game(name, joiner, name, joiner, 'SET', 'NO', 0, 0, null, 0, null, null);
//        return change.after.ref.collection("game").add({
//            game
//        });
//    }
//    return null;
//});
//
////Updates the room to have a reference of its game.
//exports.updateRoom = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onCreate((doc, context) => {
//    const path = doc.ref.path;
//    const room = doc.ref.parent.parent;
//    return room.update({"game_path": path, "status": "IN_PLAY"});
//})
//
//// Computes the winner of each turn.
//exports.onSetStat = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onUpdate((change) => {
//    const after = change.after.data() as Game;
//    const oldStat = after.old_stat;
//    const currStat = after.curr_stat;
//    const otherPlayer = after.other_player as String;
//    const turn = after.turn as String;
//    if (after.move === "SET" && currStat != null && oldStat == null) {
//        return change.after.ref.update({
//            "move": 'COMPARE',
//            "turn": otherPlayer,
//            "old_stat": currStat,
//            "stat": null,
//            "draw": 'NO',
//            "other_player": turn
//        });
//    } else if (after.move === "COMPARE" && currStat != null && oldStat != null) {
//        const old = parseInt(oldStat['baseStat']);
//        const curr = parseInt(currStat['baseStat']);
//        let scoreField;
//        let winner;
//        let loser;
//        let newScore;
//        if (old > curr) {
//            winner = otherPlayer
//            loser = turn;
//            scoreField = after.getScoreField(winner);
//            newScore = after.updatedScore(winner);
//        } else if (old < curr) {
//            winner = turn;
//            loser = otherPlayer;
//            scoreField = after.getScoreField(winner);
//            newScore = after.updatedScore(winner);
//        } else {
//            winner = otherPlayer;
//            loser = turn;
//            scoreField = "tie";
//            newScore = after.updatedScore(scoreField);
//        }
//        console.log(scoreField);
//        console.log(newScore);
//        return change.after.ref.update({
//            "move": 'SET',
//            "turn": winner,
//            "old_stat": null,
//            [scoreField]: newScore,
//            "stat": null,
//            "draw": "YES",
//            "other_player": loser
//        });
//    }
//    return null;
//})
//
//exports.leftGame = functions.firestore
//.document('rooms/{roomId}/game/{gameId}')
//.onUpdate(async (change) => {
//    const after = change.after.data() as Game;
//    const room = change.after.ref.parent.parent;
//    const left = after.left_game;
//    if (left == null) {
//        return null;
//    } else {
//        return change.after.ref.delete()
//            .then(() => {
//                room.delete()
//            })
//
//    }
//})


//with(itemView) {
//    deposit_amount.setTextVisibility(item.balance?.formattedAmount)
//    deposit_button.buttonState = when {
//        item.loading -> ActionButton.ActionButtonState.LOADING
//        item.enabled -> ActionButton.ActionButtonState.ENABLED
//        else -> ActionButton.ActionButtonState.DISABLED
//    }
//
//    deposit_button.setOnClickListener {
//        clickListener.onItemClick(item)
//    }
//}

