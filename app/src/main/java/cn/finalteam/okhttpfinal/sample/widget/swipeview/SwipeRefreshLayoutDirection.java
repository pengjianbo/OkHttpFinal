package cn.finalteam.okhttpfinal.sample.widget.swipeview;

/**
 * Created by oliviergoutay on 1/23/15.
 */
public enum SwipeRefreshLayoutDirection {

    TOP(0),
    BOTTOM(1),
    BOTH(2);

    private int mValue;

    SwipeRefreshLayoutDirection(int value) {
        this.mValue = value;
    }

    public static SwipeRefreshLayoutDirection getFromInt(int value) {
        for (SwipeRefreshLayoutDirection direction : SwipeRefreshLayoutDirection.values()) {
            if (direction.mValue == value) {
                return direction;
            }
        }
        return BOTH;
    }

}
