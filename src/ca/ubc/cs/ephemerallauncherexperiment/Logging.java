package ca.ubc.cs.ephemerallauncherexperiment;

import android.content.Context;

public class Logging {

    public static long startTime;
    public static long previousPageLandingTime;       // timestamp of when the previous page was reached
    public static long previousPageStartDragging;     // timestamp of when users started swiping on the previous page

    static final String lineSep = "\n-------------------------------------------------------\n";
    static final String halfLineSep = "\n" + lineSep.substring(0, (int)lineSep.length()/2) + "\n";

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

    public static void initialize(Context context) {

        //Initialize trial log file
        ExperimentParameters.state.currentTrialsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getTrialLogFileName());
        FileManager.writeToFile(ExperimentParameters.state.currentTrialsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.trial_log_header)), false);

        //Initialize event log file
        ExperimentParameters.state.currentEventsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getEventsLogFileName());
        FileManager.writeToFile(ExperimentParameters.state.currentEventsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.events_log_header)), false);

        //Initialize experiment log file
        ExperimentParameters.state.currentExperimentLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getExperimentLogFileName());
        if (!ExperimentParameters.state.currentExperimentLogFile.exists()){
            FileManager.writeToFile(ExperimentParameters.state.currentExperimentLogFile, Utils.appendWithComma(context.getString(R.string.experiment_log_header), context.getString(R.string.experiment_parameters_log_header)), false);
        }
        FileManager.appendLineToFile(ExperimentParameters.state.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), ExperimentParameters.state.participantId, ExperimentParameters.experimentToString()));
    }

    ////////////////////////////    Log distributions    ///////////////////////////

    public static void logDistributionsAtExperimentInit(Context context) {
        ExperimentParameters.state.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        //general
        logStr += "GENERAL \n";
        logStr += "participantID: " + ExperimentParameters.state.participantId + "\n";
        logStr += "zipfSize: " + String.valueOf(ExperimentParameters.zipfSize) + "\n";
        logStr += "zipfCoeff: " + String.valueOf(ExperimentParameters.zipfCoeff) + "\n";
        logStr += "Accuracy: " + String.valueOf(ExperimentParameters.distributions.empiricalAccuracy) + "\n";
        logStr += "Icon Set: " + ExperimentParameters.ICON_SET;
        logStr += "Label Set: " + ExperimentParameters.LABEL_SET;

        //logging conditions
        logStr += lineSep;
        logStr += "CONDITIONS \n";
        for (int i=0; i < ExperimentParameters.EFFECTS.values().length; i++)
            logStr += conditionToString(i);

        //logging zipfian
        logStr += lineSep;
        logStr += "ZIPF \n";
        for (int i=0; i < ExperimentParameters.zipfSize; i++)
            logStr += String.valueOf(ExperimentParameters.distributions.zipfian[i+1]) + " ";

        FileManager.writeLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr, false);
    }

    public static void logDistributionsAtConditionInit(Context context) {
        String logStr ="";

        //logging target positions
        logStr += lineSep;
        logStr += "TARGET POSITIONS (positions start from 1) (Zipfian rank indicated below) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithZero(ExperimentParameters.distributions.targets[tr]) + " ";
        }
        logStr+="\n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithZero(ExperimentParameters.distributions.target_ranks[tr]) + " ";
        }

        //logging highlighted icons
        logStr += lineSep;
        logStr += "HIGHLIGHTED ICONS' POSITIONS \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            for (int icon = 0; icon < ExperimentParameters.state.num_highlighted_icons(); icon++)
                logStr += Utils.padWithZero(ExperimentParameters.distributions.highlighted[tr][icon]) + " ";
            logStr += "\n";
        }

        //logging image icons and labels
        logStr += lineSep;
        logStr += "IMAGE ICONS AND LABELS\n";

        String iconAddressPrefix = ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX;
        logStr += halfLineSep;
        logStr += "CONDITION " + String.valueOf(ExperimentParameters.state.condition) + ": " + conditionToString(ExperimentParameters.state.condition) +"\n";
        for (int pos=1; pos <= ExperimentParameters.state.num_positions(); pos++){
            logStr += String.valueOf(pos) + ": " + Utils.extractIconName(context.getString(ExperimentParameters.distributions.images_ID[pos]), iconAddressPrefix) + " "  +context.getString(ExperimentParameters.distributions.labels_ID[pos]) + "; " ;
            if ((pos) % ExperimentParameters.NUM_ICONS_PER_PAGE == 0) {
                logStr += "\n";
            }

        }

        // Write things in the SAME distribution log file
        FileManager.writeLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr, false);
    }

    private static String conditionToString(int condition){
        return ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][0]]+", "
                +ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]]+", "
                +ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][2]]+" ";
    }

    public static void logPostExperimentDistributions(Context context) {
        ExperimentParameters.state.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        logStr += lineSep;
        logStr += "Post-experiment accuracy: " + String.valueOf(ExperimentParameters.distributions.empiricalAccuracy) + "\n";

        //logging selected positions
        logStr += lineSep;
        logStr += "SELECTED POSITIONS (positions start from 1) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            logStr += String.valueOf(ExperimentParameters.distributions.selected[tr+1]) + " ";
        }

        //logging highlighted icons
        logStr += lineSep;
        logStr += "ACTUAL HIGHLIGHTED ICONS' POSITIONS (with empirical MRU) \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            for (int icon = 0; icon < ExperimentParameters.state.num_highlighted_icons(); icon++)
                logStr += Utils.padWithZero(ExperimentParameters.distributions.highlighted[tr+1][icon]) + " ";
            logStr += "\n";
        }

        FileManager.writeLineToFile(ExperimentParameters.state.currentDistributionsLogFile, logStr, false);
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
        // If user stayed on the first page
        if(ExperimentParameters.state.pages_times.equals(""))
            ExperimentParameters.state.pages_times="1,"+result.duration+",0,";

        String finalTrialLog = Utils.appendWithComma(Utils.getTimeStamp(false), stateCsvLog(context),resultCsvLog(result), ExperimentParameters.state.pages_times);
        FileManager.appendLineToFile(ExperimentParameters.state.currentTrialsLogFile,finalTrialLog);
    }
}
