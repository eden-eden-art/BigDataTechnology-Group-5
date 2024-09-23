export type SavedData = {
    name: string,
    gameID: string,
    salePrice: string,
    retailPrice: string,
    cheapestPrice: string,
    steamRatingPercent: string,
    metacriticScore: string
}

export type Game = {
    gameID: string;
    steamAppID: string | null;
    cheapest: string;
    cheapestDealID: string;
    external: string;
    internalName: string;
    thumb: string;
};

export type GameInfo = {
    storeID: string;
    gameID: string;
    name: string;
    steamAppID: string;
    salePrice: string;
    retailPrice: string;
    steamRatingText: string;
    steamRatingPercent: string;
    steamRatingCount: string;
    metacriticScore: string;
    metacriticLink: string;
    releaseDate: number;
    publisher: string;
    steamworks: string;
    thumb: string;
    cheapestPrice: CheapestPrice
};

type CheaperStore = {
    dealID: string;
    storeID: string;
    salePrice: string;
    retailPrice: string;
};

export type CheapestPrice = {
    price: string;
    date: number;
};

type GameData = {
    gameInfo: GameInfo;
    cheaperStores: CheaperStore[];
    cheapestPrice: CheapestPrice;
}
