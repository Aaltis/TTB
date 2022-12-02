package fi.breakwaterworks.model;

public enum SetType {
    STRAIGHT_SET("Straight Set", false),
    DROP_SET("Drop Set", true),
    AMRAP("Amrap", false),
    MTOR("MTOR", true),
    MYO("Myo", true),
    REST_PAUSE("Rest Pause", true),
    WIDOWMAKER("WidowMaker",false);

    private String typeName;
    private boolean multiset;

    SetType(String name, boolean multiset) {
        typeName = name;
        this.multiset = multiset;
    }

    @Override
    public String toString() {
        return typeName;
    }

    public boolean isMultiset() {
        return multiset;
    }
}
