import express from "express";
import cors from "cors";

const { MongoClient } = require('mongodb');

const url = 'mongodb://mongo:27017';
const dbName = 'data';
const client = new MongoClient(url);

client.connect()
    .catch(err => console.log(err))

const app = express();
app.use(cors());

app.get("/durations", (req, res) => {
    const db = client.db(dbName)
    const coll = db.collection("durations")

    const lengths = coll.find().toArray()
        .then(d => res.json(d));
})
app.get("/titlelengths", (req, res) => {
    const db = client.db(dbName)
    const coll = db.collection("titlelengths")

    const lengths = coll.find().toArray()
        .then(d => res.json(d));
})
app.get("/titleheatmap", (req, res) => {
    const db = client.db(dbName)
    const coll = db.collection("titleheatmap")

    const lengths = coll.find().toArray()
        .then(d => res.json(d));
})
app.get("/popularartists", (req, res) => {
    const db = client.db(dbName)
    const coll = db.collection("popularartists")

    const lengths = coll.find().toArray()
        .then(d => res.json(d));
})

app.listen("3005", () => { console.log("data server up") })