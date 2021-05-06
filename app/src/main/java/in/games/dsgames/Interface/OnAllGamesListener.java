package in.games.dsgames.Interface;

import java.util.ArrayList;

import in.games.dsgames.Model.GetGamesModel;


public interface OnAllGamesListener {
    void onMatkaGames(ArrayList<GetGamesModel> matkaGameList);
    void onStarlineGames(ArrayList<GetGamesModel> starlineGAmeList);
}
