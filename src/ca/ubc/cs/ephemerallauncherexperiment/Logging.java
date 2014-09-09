package ca.ubc.cs.ephemerallauncherexperiment;

import android.content.Context;

public class Logging {

    public static long startTime;
    public static long previousPageLandingTime;       // timestamp of when the previous page was reached
    public static long previousPageStartDragging;     // timestamp of when users started swiping on the previous page
    public static long idle_time;                     // total time spent on one page (without swiping)
    public static long swiping_time;                  // total time spent swiping before changing page
    public static String pages_times;                 // string of (page#, idle_time, swiping_time)

    private static final String lineSep = "\n-------------------------------------------------------\n";

    //////////////////////////    Initialize    ////////////////////////////////////

    private static String getTrialLogFileName(){
        return Utils.getTimeStamp(true) + "__" + ExperimentParameters.state.participantId+".csv";
    }

    private static String getEventsLogFileName(){
        return Utils.getTimeStamp(true) + "__" + ExperimentParameters.state.participantId + "__EVENTS" + ".csv";
    }

    public static String getDistributionsFileName(){
        return Utils.getTimeStamp(true) + "__" + ExperimentParameters.state.participantId + "__DISTRIBUTIONS" + ".log";
    }

    private static String getExperimentLogFileName(){
        return ExperimentParameters.EXPERIMENT_LOG_FILE_NAME;
    }

    public static String getExperimentBackupFileName() { return  ExperimentParameters.state.participantId+".backup"; }

    public static void initialize(Context context) {

        //Initialize experiment log file
        ExperimentParameters.state.currentExperimentLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getExperimentLogFileName());
        if (!ExperimentParameters.state.currentExperimentLogFile.exists()){
            FileManager.writeToFile(ExperimentParameters.state.currentExperimentLogFile, Utils.appendWithComma(context.getString(R.string.experiment_log_header), context.getString(R.string.experiment_parameters_log_header)), false);
        }
        FileManager.appendLineToFile(ExperimentParameters.state.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), ExperimentParameters.state.participantId, ExperimentParameters.experimentToString()));

        //Initialize distributions log file
        ExperimentParameters.state.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        //Initialize trial log file
        ExperimentParameters.state.currentTrialsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getTrialLogFileName());
        FileManager.writeToFile(ExperimentParameters.state.currentTrialsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.trial_log_header)), false);

        //Initialize event log file
        ExperimentParameters.state.currentEventsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getEventsLogFileName());
        FileManager.writeToFile(ExperimentParameters.state.currentEventsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.events_log_header)), false);
    }

    ////////////////////////////    Log distributions    ///////////////////////////

    public static void logDistributionsAtExperimentInit() {

        String logStr ="";

        //general
        logStr += "GENERAL \n";
        logStr += "participantID: " + ExperimentParameters.state.participantId + "\n";
        logStr += "zipfSize: " + String.valueOf(ExperimentParameters.zipfSize) + "\n";
        logStr += "zipfCoeff: " + String.valueOf(ExperimentParameters.zipfCoeff) + "\n";

        //logging zipfian
        logStr += "\n";
        logStr += "ZIPF \n";
        for (int i=0; i < ExperimentParameters.zipfSize; i++)
            logStr += String.valueOf(ExperimentParameters.distributions.zipfian[i+1]) + " ";

        //logging conditions
        logStr += lineSep;
        logStr += "CONDITIONS \n";
        for (int i=0; i < ExperimentParameters.NUM_CONDITIONS; i++)
            logStr += conditionToString(i)+"\n";

        //logging order
        logStr += "\n";
        logStr += "ORDER of conditions for this participant: \n";
        for(int i=0; i<ExperimentParameters.ORDERS.length;i++)
            logStr += ExperimentParameters.ORDERS[ExperimentParameters.state.participant][i]+" ";

        FileManager.writeLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr, false);
    }

    public static void logDistributionsAtConditionInit(Context context) {
        String logStr = lineSep;
        logStr += "CONDITION " + conditionToString(ExperimentParameters.state.condition) +"\n";

        //logging target positions
        logStr+="\n";
        logStr += "TARGET POSITIONS (positions start from 1) (Zipfian rank indicated below) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithSpaces(ExperimentParameters.distributions.targets[tr]) + " ";
        }
        logStr+="\n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithSpaces(ExperimentParameters.distributions.target_ranks[tr]) + " ";
        }

        //logging highlighted icons
        logStr += "\n\n";
        logStr += "HIGHLIGHTED ICONS' POSITIONS \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            for (int icon = 0; icon < ExperimentParameters.state.num_highlighted_icons(); icon++)
                logStr += Utils.padWithSpaces(ExperimentParameters.distributions.highlighted[tr][icon]) + " ";
            logStr += "\n";
        }

        //logging image icons and labels
        String iconAddressPrefix = ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX;
        logStr += "\n";
        for (int pos=1; pos <= ExperimentParameters.state.num_positions(); pos++){
            logStr += String.valueOf(pos) + ": " + Utils.extractIconName(context.getString(ExperimentParameters.distributions.images_ID[pos]), iconAddressPrefix) + " "  +context.getString(ExperimentParameters.distributions.labels_ID[pos]) + "; " ;
            if ((pos) % ExperimentParameters.NUM_ICONS_PER_PAGE == 0) {
                logStr += "\n";
            }
        }

        // Write things in the SAME distribution log file
        FileManager.appendLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr);
    }

    public static void logPostExperimentDistributions() {

        String logStr = lineSep;

        // Not very useful anymore, but eh
        ExperimentParameters.distributions.computeAccuracy();
        logStr += "Post-experiment accuracy: " + String.valueOf(ExperimentParameters.distributions.empiricalAccuracy) + "\n";

        //logging selected positions
        logStr += lineSep;
        logStr += "SELECTED POSITIONS (positions start from 1) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            logStr += String.valueOf(ExperimentParameters.distributions.selected[tr+1]) + " ";
        }

        // Write things in the SAME distribution log file
        FileManager.appendLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr);
    }

    private static String conditionToString(int condition){
        return condition + ": " + ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][0]]+", "
                +ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]]+", "
                +ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][2]];
    }

    ///////////////////////////    Log trial    ///////////////////////////////////

    public static String stateCsvLog(Context context){
        return Utils.appendWithComma(ExperimentParameters.state.participantId, String.valueOf(ExperimentParameters.state.block), String.valueOf(ExperimentParameters.state.accuracy),String.valueOf(ExperimentParameters.state.num_pages), ExperimentParameters.state.effect.toString(),
                String.valueOf(ExperimentParameters.state.trial), String.valueOf(startTime),
                String.valueOf(ExperimentParameters.state.targetIconPage), String.valueOf(ExperimentParameters.state.targetIconRow), String.valueOf(ExperimentParameters.state.targetIconColumn),
                Utils.extractIconName(context.getString(ExperimentParameters.distributions.images_ID[ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]]), ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX), context.getString(ExperimentParameters.distributions.labels_ID[ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]]),
                String.valueOf(ExperimentParameters.distributions.target_ranks[ExperimentParameters.state.trial]));
    }

    public static String resultCsvLog(Result result){
        String highlightedStr = result.ifHighlighted? "Highlighted" : "Normal";
        String successStr = ExperimentParameters.state.success? "Success" : "Failure";
        String timeoutStr = ExperimentParameters.state.timeout? "Timeout" : "InTime";
        String missedStr = ExperimentParameters.state.missed? "Miss" : "Hit";
        return Utils.appendWithComma(highlightedStr, String.valueOf(result.duration), String.valueOf(ExperimentParameters.state.page), String.valueOf(result.row), String.valueOf(result.column), successStr, timeoutStr, missedStr, String.valueOf(ExperimentParameters.state.num_pages_visited),result.iconName, result.iconLabel);
    }

    public static void logTrial(Context context, Result result){
        String finalTrialLog = Utils.appendWithComma(Utils.getTimeStamp(false), stateCsvLog(context),resultCsvLog(result), Logging.pages_times);
        FileManager.appendLineToFile(ExperimentParameters.state.currentTrialsLogFile,finalTrialLog);
    }
}
