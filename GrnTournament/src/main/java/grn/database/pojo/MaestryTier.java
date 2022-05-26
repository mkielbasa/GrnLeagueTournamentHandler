package grn.database.pojo;

public enum MaestryTier {

    S(200000),
    A(100000),
    B(50000),
    C(25000),
    D(15000),
    F(0);

    private static final MaestryTier[] tiers = {S,A,B,C,D,F};

    private long minimum;

    private MaestryTier (long minimum) {
        this.minimum = minimum;
    }

    public static MaestryTier getTier(long maestry) {
        for (int i=0; i<tiers.length; i++) {
            MaestryTier tier = tiers[i];
            if (maestry > tier.minimum)
                return tier;
        }
        return null;
    }
}
