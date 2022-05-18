package grn.database.pojo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlayerRank {

    private static Map<Integer, String> ranks = new HashMap<>();
    private static Map<Integer, String> subRanks = new HashMap<>();

    static  {
        ranks.put(0, "UNRANKED");
        ranks.put(0, "IRON");
        ranks.put(1, "BRONZE");
        ranks.put(2, "SILVER");
        ranks.put(3, "GOLD");
        ranks.put(4, "PLATINUM");
        ranks.put(5, "DIAMOND");
        ranks.put(6, "MASTER");
        ranks.put(7, "GRANDMASTER");
        ranks.put(8, "CHALLENGER");

        subRanks.put(4, "I");
        subRanks.put(3, "II");
        subRanks.put(2, "III");
        subRanks.put(1, "IV");
    }

    public static int getRankValue (String tier) {
        for (int key : ranks.keySet()) {
            String rank = ranks.get(key);
            if (rank.equals(tier))
                return key;
        }
        return 0;
    }

    public static int getSubRankValue (String tier) {
        for (int key : subRanks.keySet()) {
            String rank = subRanks.get(key);
            if (rank.equals(tier))
                return key;
        }
        return -1;
    }

    public static String getRankIcon (String tier) {
        if (tier == null)
            return "unranked.png";
        return tier.toLowerCase(Locale.ROOT) + ".png";
    }
}
