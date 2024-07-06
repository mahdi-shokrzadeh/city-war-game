package controllers.game;

import controllers.ClanController;
import controllers.UserCardsController;
import database.DBs.*;
import models.*;
import models.card.Card;
import models.game.Game;

import java.util.*;
import java.util.stream.Stream;

public class GameController {
    private static final UserDB userDB = new UserDB();
    private static final GameDB gameDB = new GameDB();
    private static final ClanDB clanDB = new ClanDB();
    private static final ClanBattleDB cbDB = new ClanBattleDB();
    private static final UserCardDB ucDB = new UserCardDB();

    private static int calculateWinnerScore(User user, int hp, int n) {
        int temp = hp - user.getHitPoints() == 0 ? 1 : hp - user.getHitPoints();
        return (user.getDamage() + temp) / n;
    }

    private static int calculateLoserScore(User user, int hp, int n) {
        return (user.getDamage() / 10 + 1000 / hp) * n;
    }

    private static int calculateUserLevel(User user) {
        int sum = 0;
        int level = user.getLevel();
        while (sum <= user.getExperience()) {
            sum = (int) (level * (level + 1) * 0.5);
            user.setExperience(user.getExperience() - 50 * sum);
            level++;
        }
        return Math.max(level, 1);
    }

    private static int calculateCardLevel(UserCard uc) {
        int sum = 0;
        int level = uc.getLevel();
        while (sum < uc.getExperience()) {
            sum = (int) (uc.getLevel() * (uc.getLevel() + 1) * 0.5);
            uc.setExperience(uc.getExperience() - 50 * sum);
            level++;
        }
        level = Math.max(level, 1);
        level = Math.min(level, 3);
        return level;
    }

    public static Response createGame(Game game, User p1, User p2, int numberOfRounds, String winnerString,
            List<Card> p1Cards,
            List<Card> p2Cards) {
        int p1OriginalHP = userDB.getOne(p1.getID()).getHitPoints();
        int p2OriginalHP = userDB.getOne(p2.getID()).getHitPoints();
        User winnerUser = null;
        User loserUser = null;
        int winnerOriginalHP;
        int loserOriginalHP;
        int winnerScore;
        int loserScore;
        int p1Score;
        int p2Score;
        Exception exception = null;
        Map<String, Object> result = new HashMap<>();
        String winnerReward = "";
        String loserReward = "";
        List<Card> allP1Cards = ((List<Card>) UserCardsController.getUsersCards(p1).body.get("cards"));
        List<Card> allP2Cards = ((List<Card>) UserCardsController.getUsersCards(p2).body.get("cards"));

        if (!winnerString.equals("player_one") && !winnerString.equals("player_two")) {
            return new Response("invalid winner", -422);
        }
        if (winnerString.equals("player_one")) {
            winnerScore = calculateWinnerScore(p1, p1OriginalHP, numberOfRounds);
            loserScore = calculateLoserScore(p2, p2OriginalHP, numberOfRounds);
            p1Score = winnerScore;
            p2Score = loserScore;
            winnerUser = p1;
            loserUser = p2;
            winnerOriginalHP = p1OriginalHP;
            loserOriginalHP = p2OriginalHP;
        } else {
            winnerScore = calculateWinnerScore(p2, p2OriginalHP, numberOfRounds);
            loserScore = calculateLoserScore(p1, p1OriginalHP, numberOfRounds);
            p2Score = winnerScore;
            p1Score = loserScore;
            winnerUser = p2;
            loserUser = p1;
            winnerOriginalHP = p2OriginalHP;
            loserOriginalHP = p1OriginalHP;
        }

        allP1Cards.removeAll(p1Cards);
        for (Card card : allP1Cards) {
            try {
                UserCard userCard = (UserCard) UserCardsController.getUserCard(p1.getID(), card.getID()).body
                        .get("userCard");
                userCard.setExperience(userCard.getExperience() + p1Score);
                int level = calculateCardLevel(userCard);
                if (winnerString.equals("player_one")) {
                    winnerReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                } else {
                    loserReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                }
                userCard.setLevel(level);
                ucDB.update(userCard, userCard.getID());
            } catch (Exception e) {
                exception = e;
            }
        }
        allP2Cards.removeAll(p2Cards);
        for (Card card : allP2Cards) {
            try {
                UserCard userCard = (UserCard) UserCardsController.getUserCard(p2.getID(), card.getID()).body
                        .get("userCard");
                userCard.setExperience(userCard.getExperience() + p2Score);
                int level = calculateCardLevel(userCard);
                if (winnerString.equals("player_two")) {
                    winnerReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                } else {
                    loserReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                }
                userCard.setLevel(level);
                ucDB.update(userCard, userCard.getID());
            } catch (Exception e) {
                exception = e;
            }
        }

        if (game == null) {
            return new Response("game is null", -400);
        }
        try {
            game.setNumber_of_rounds(numberOfRounds);
            game.setWinner(winnerString);
            game.setEnded_at(new Date().toString());
            gameDB.create(game);
            result.put("game", game);
        } catch (Exception e) {
            return new Response("an exception happened while creating game", -500, e);
        }

        winnerReward += "/ User: ";
        loserReward += "/ User: ";

        try {
            int winnerLevel = calculateUserLevel(winnerUser);
            int winnerCoins = winnerUser.getCoins() + (winnerScore / 10);
            winnerReward += "+" + winnerScore + " experience\t";
            winnerReward += "+" + (winnerLevel - winnerUser.getLevel()) + " level\t";
            winnerReward += "+25 hit points\t";
            winnerUser.setExperience(winnerUser.getExperience() + winnerScore);
            winnerUser.setHitPoints(winnerOriginalHP + 25);
            for (int i = 0; i < winnerLevel - winnerUser.getLevel(); i++) {
                winnerUser.setLevel(winnerUser.getLevel() + 1);
                winnerCoins += winnerUser.getLevel() * 15;
            }
            winnerUser.setCoins(winnerCoins);
            winnerReward += "+" + (winnerCoins - winnerUser.getCoins()) + " coins\t";
            int loserLevel = calculateUserLevel(loserUser);
            int loserCoins = 0;
            loserReward += "+" + loserScore + " experience\t";
            loserUser.setExperience(loserUser.getExperience() + loserScore);
            for (int i = 0; i < loserLevel - loserUser.getLevel(); i++) {
                loserUser.setLevel(loserUser.getLevel() + 1);
                loserCoins += loserUser.getLevel() * 15;
            }
            loserUser.setCoins(loserUser.getCoins() + loserCoins);
            loserReward += "+" + loserCoins + " coins\t";
            loserUser.setHitPoints(loserOriginalHP);
            userDB.update(winnerUser, winnerUser.getID());
            userDB.update(loserUser, loserUser.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving users", -500, e);
        }

        result.put("winner", winnerReward);
        result.put("loser", loserReward);
        if (exception != null) {
            return new Response("an exception happened while upgrading a card", -500, exception);
        }
        return new Response("game created successfully", 200, result);

    }

    public static Response createBotGame(Game game, User player, int numberOfRounds, String winner, List<Card> cards) {
        int playerOriginalHP = userDB.getOne(player.getID()).getHitPoints();
        Map<String, Object> result = new HashMap<>();
        String winnerReward = "";
        String loserReward = "";
        Exception exception = null;
        int score = winner.equals("player_two") ? calculateWinnerScore(player, playerOriginalHP, numberOfRounds)
                : calculateLoserScore(player, playerOriginalHP, numberOfRounds);

        if (!winner.equals("player_two") && !winner.equals("bot")) {
            return new Response("invalid winner", -422);
        }

        if (game == null) {
            return new Response("game is null", -400);
        }
        try {
            game.setNumber_of_rounds(numberOfRounds);
            game.setWinner(winner);
            game.setEnded_at(new Date().toString());
            gameDB.create(game);
        } catch (Exception e) {
            return new Response("an exception happened while creating game", -500, e);
        }

        List<Card> allP1Cards = ((List<Card>) UserCardsController.getUsersCards(player).body.get("cards"));
        allP1Cards.removeAll(cards);
        for (Card card : allP1Cards) {
            try {
                UserCard userCard = (UserCard) UserCardsController.getUserCard(player.getID(), card.getID()).body
                        .get("userCard");
                userCard.setExperience(userCard.getExperience() + score);
                int level = calculateCardLevel(userCard);
                if (winner.equals("player_two")) {
                    winnerReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                } else {
                    loserReward += "card " + card.getName() + ": +" + (level - userCard.getLevel()) + " level\t";
                }
                userCard.setLevel(calculateCardLevel(userCard));
                ucDB.update(userCard, userCard.getID());
            } catch (Exception e) {
                exception = e;
            }
        }

        winnerReward += "/ User: ";
        loserReward += "/ User: ";

        try {
            if (winner.equals("player_two")) {
                int level = calculateUserLevel(player);
                int coins = player.getCoins() + (score / 10);
                score = calculateWinnerScore(player, playerOriginalHP, numberOfRounds);
                player.setExperience(player.getExperience() + score);
                player.setHitPoints(playerOriginalHP + 25);
                winnerReward += "+" + score + " experience\t";
                winnerReward += "+25 hit points\t";
                winnerReward += "+" + (level - player.getLevel()) + " level\t";
                for (int i = 0; i < level - player.getLevel(); i++) {
                    player.setLevel(player.getLevel() + 1);
                    coins += player.getLevel() * 15;
                }
                winnerReward += "+" + (coins - player.getCoins()) + " coins\t";
                player.setCoins(coins);
                if (player.getProgress() == 5) {
                    player.resetProgress();
                } else {
                    player.makeProgress();
                }
            } else {
                score = calculateLoserScore(player, playerOriginalHP, numberOfRounds);
                player.setExperience(player.getExperience() + score);
                loserReward += "+" + score + " experiencet";
                int level = calculateUserLevel(player);
                player.setLevel(level);
                loserReward += "+" + level + " level\t";
                player.setHitPoints(playerOriginalHP);
            }
            userDB.update(player, player.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving user", -500, e);
        }

        result.put("winner", winnerReward);
        result.put("loser", loserReward);
        if (exception != null) {
            return new Response("an exception happened while upgrading a card", -500, exception);
        }
        return new Response("game created successfully", 200, result);
    }

    public static Response createGambleGame(Game game, User p1, User p2, int numberOfRounds, String winner,
            int betAmount) {
        int p1OriginalHP = userDB.getOne(p1.getID()).getHitPoints();
        int p2OriginalHP = userDB.getOne(p2.getID()).getHitPoints();
        String winnerReward = "+" + betAmount + " coins\t";
        String loserReward = "-" + betAmount + " coins\t";
        Map<String, Object> result = new HashMap<>();

        if (!winner.equals("player_one") && !winner.equals("player_two")) {
            return new Response("invalid winner", -422);
        }

        if (game == null) {
            return new Response("game is null", -400);
        }
        try {
            game.setNumber_of_rounds(numberOfRounds);
            game.setWinner(winner);
            game.setEnded_at(new Date().toString());
            gameDB.create(game);
        } catch (Exception e) {
            return new Response("an exception happened while creating game", -500, e);
        }

        try {
            if (winner.equals("player_one")) {
                p1.setCoins(p1.getCoins() + betAmount);
                p2.setCoins(p2.getCoins() - betAmount);
            } else {
                p1.setCoins(p1.getCoins() - betAmount);
                p2.setCoins(p2.getCoins() + betAmount);
            }
            p1.setHitPoints(p1OriginalHP);
            p2.setHitPoints(p2OriginalHP);
            userDB.update(p1, p1.getID());
            userDB.update(p2, p2.getID());
        } catch (Exception e) {
            return new Response("an exception happened while saving users", -500, e);
        }

        result.put("winner", winnerReward);
        result.put("loser", loserReward);
        return new Response("game created successfully", 200, result);
    }

    public static Response getAllUserGames(int id) {
        List<Game> games = null;
        try {
            games = gameDB.getByUserID(id);
        } catch (Exception e) {
            return new Response("an exception happened while fetching users' games", -500, e);
        }
        if (games == null) {
            return new Response("no games were found for this user", -400);
        }
        return new Response("users' games fetched successfully", 200, "games", games);
    }

    public static Response getGameEssentials(User user) {
        ClanBattle battle;
        Clan userClan = null;
        try {
            userClan = clanDB.getOne(user.getClanID());
        } catch (Exception e) {
            return new Response("an exception happned while fetching your clan", -500, e);
        }
        if (userClan == null) {
            return new Response("your clan was not found", -400);
        }
        Clan defenderClan = null;
        Clan attackerClan = null;
        User opponent = null;
        try {
            battle = cbDB.firstWhereEqualsOr(userClan.getID());
        } catch (Exception e) {
            return new Response("an exception happened while fetching battle", -500, e);
        }
        if (battle == null) {
            return new Response("no battle was found for your clan", -400);
        }

        attackerClan = ((Clan) ClanController.getCLanById(battle.getAttackerID()).body.get("clan"));
        defenderClan = ((Clan) ClanController.getCLanById(battle.getDefenderID()).body.get("clan"));

        if (battle.getHasFinale()) {
            if (attackerClan.getLeaderID() != user.getID()) {
                return new Response("the battle is in finale phase, only leaders are allowed to play game", -401);
            }
            for (int uID : defenderClan.getMembersIDS()) {
                if (uID == defenderClan.getLeaderID()) {
                    opponent = userDB.getOne(uID);
                }
            }

        } else {
            List<Integer> playedUserIDS = Stream
                    .concat(battle.getPlayedAttackersIDS().stream(), battle.getPlayedDefendersIDS().stream()).toList();
            boolean userHasPlayed = false;
            for (int id : playedUserIDS) {
                if (user.getID() == id) {
                    userHasPlayed = true;
                    break;
                }
            }
            if (userHasPlayed) {
                return new Response("user has already played", -403);
            }

            Random random = new Random();
            if (user.getClanID() == battle.getAttackerID()) {
                List<Integer> defenders = defenderClan.getMembersIDS();
                for (int id : battle.getPlayedDefendersIDS()) {
                    defenders.removeIf(o -> o == id);
                }
                opponent = userDB.getOne(defenders.get(random.nextInt(defenders.size())));
            } else if (user.getClanID() == battle.getDefenderID()) {
                List<Integer> attackers = attackerClan.getMembersIDS();
                for (int id : battle.getPlayedAttackersIDS()) {
                    attackers.removeIf(o -> o == id);
                }
                opponent = userDB.getOne(attackers.get(random.nextInt(attackers.size())));
            }
            if (opponent == null) {
                return new Response("no opponent was found", -404);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("battle", battle);
        result.put("attackerClan", attackerClan);
        result.put("defenderClan", defenderClan);
        result.put("opponent", opponent);
        return new Response("clan game essentials fetched successfully", 200, result);
    }

    public static Response creatGameClan(Game game, ClanBattle battle, Clan attackerClan, Clan defenderClan, User p1,
            User p2,
            int numberOfRounds, String winner, List<Card> p1Cards, List<Card> p2Cards) {

        Response res = createGame(game, p1, p2, numberOfRounds, winner, p1Cards, p2Cards);
        if (res.body.get("game") == null) {
            return new Response("unable to create game", -400);
        }
        battle.playAGame((Game) res.body.get("game"));
        if (battle.getNumberOfRemainingGames() == 0) {
            boolean hasFinale = battle.endBattle();
            if (!hasFinale) {
                attackerClan.endBattle();
                defenderClan.endBattle();
            }
        }
        try {
            clanDB.update(attackerClan, attackerClan.getID());
            clanDB.update(defenderClan, defenderClan.getID());
            cbDB.update(battle, battle.getID());
        } catch (Exception e) {
            return new Response("an exception happened while creating battle", -500, e);
        }

        return new Response("game created successfully", 200, res.body);
    }

}
