import type { CheapestPrice, Game, SavedData } from "./types";
import type { GameInfo } from "./types";
import { Kafka } from "kafkajs";

const requestOptions: RequestInit = {
    method: 'GET',
    redirect: 'follow'
};

const tokens = [
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
];


const kafka = new Kafka({
    clientId: 'collector',
    brokers: ['localhost:9092'],
});
const producer = kafka.producer({
    allowAutoTopicCreation: true,

});

producer.connect().then(() => {
    console.log("producer connected");

    // let time = 1000
    // tokens.forEach(token => {
    //     console.log(`Calling for ${token}`);
        
    //     setTimeout(() => oneBatch(token), time)
    //     time + 30000
    // });

    oneBatch("a")
});


const oneBatch = (token: string, limit?: number) => {
    console.log(token);
    fetch(`https://www.cheapshark.com/api/1.0/games?title=${token}&limit=${limit ?? 5}`, requestOptions)
        .then(response => response.json())
        .then((result: Game[]) => {
            result.forEach((game: Game) => {
                fetch(`https://www.cheapshark.com/api/1.0/deals?id=${game.cheapestDealID}`, requestOptions)
                    .then(response => response.json())
                    .then((info: { gameInfo: GameInfo, cheapestPrice: CheapestPrice; }) => {
                        const push: SavedData = {
                            name: game.external,
                            gameID: game.gameID,
                            salePrice: info.gameInfo.salePrice,
                            retailPrice: info.gameInfo.retailPrice,
                            cheapestPrice: info.cheapestPrice.price,
                            steamRatingPercent: info.gameInfo.steamRatingPercent,
                            metacriticScore: info.gameInfo.metacriticScore
                        };

                        console.log(`sending ${push.name}`);

                        producer.send({
                            topic: 'games',
                            messages: [
                                { value: JSON.stringify(push) }
                            ]
                        });
                    });
            });
        })
        .catch(error => console.log('error', error));
};
