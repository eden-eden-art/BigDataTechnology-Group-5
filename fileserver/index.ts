import express from "express";
import cors from "cors";
import fs from "node:fs/promises";
import filebrowser from "serve-handler";
import zip from "zip-local";

const app = express();
app.use(cors());
app.use(express.json());

fs.mkdir("data/")
    .catch(err => { });

const die = () => { console.log("killing myself"); process.exit(0); };
process.on("SIGINT", die);
process.on("SIGTERM", die);
process.on("SIGKILL", die);

app.post("/", (req, res) => {
    const { file, content } = req.body;
    console.log(`saving file: [${file}] - [${content}]`);
    fs.writeFile(`data/${file}`, content);

    res.status(200).send();
});

app.get("/", (req, res) => {
    console.log(`serving file browser`);
    return filebrowser(req, res, { "public": "data/" });
});

app.get("/download", async (req, res) => {
    console.log(`zipping files`);

    zip.sync.zip("./data").compress().save("./alldata.zip");

    res.sendFile(`${__dirname}/alldata.zip`);
});

app.listen(3000, () => {
    console.log("server started");
});