package grn.database.pojo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlayerRank {

    private static Map<Integer, String> ranks = new HashMap<>();

    static  {
        ranks.put(0, "UNRANKED");
        ranks.put(1, "IRON");
        ranks.put(2, "BRONZE");
        ranks.put(3, "SILVER");
        ranks.put(4, "GOLD");
        ranks.put(5, "PLATINUM");
        ranks.put(6, "DIAMOND");
        ranks.put(7, "MASTER");
        ranks.put(8, "GRANDMASTER");
        ranks.put(9, "CHALLENGER");
    }

    public static int getRankValue (String tier) {
        for (int key : ranks.keySet()) {
            String rank = ranks.get(key);
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
