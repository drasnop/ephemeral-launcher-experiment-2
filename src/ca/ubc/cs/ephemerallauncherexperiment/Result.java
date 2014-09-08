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
        // WRONG! what we want to know is whether the target icon was highlighted, not the one that was selected
        // this.ifHighlighted = ExperimentParameters.distributions.isHighlighted(ExperimentParameters.distributions.selected[ExperimentParameters.state.trial]);
        this.ifHighlighted = ExperimentParameters.distributions.isHighlighted(ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]);
        this.duration = duration;
        this.row = row;
        this.column = column;
        this.iconName = iconName;
        this.iconLabel = iconLabel;
    }
}