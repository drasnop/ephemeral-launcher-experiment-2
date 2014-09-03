package ca.ubc.cs.ephemerallauncherexperiment;

// Just a PODS to hold the results of a trial
public class Result {
    public boolean ifHighlighted;
    public double duration;
    public int row;
    public int column;
    public String iconName;
    public String iconLabel;

    public Result (double duration, int row, int column, String iconName, String iconLabel) {
        this.ifHighlighted = Distributions.isHighlighted(Distributions.selected[State.trial]);
        this.duration = duration;
        this.row = row;
        this.column = column;
        this.iconName = iconName;
        this.iconLabel = iconLabel;
    }
}