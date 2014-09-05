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
        return Utils.getTimeStamp(true) + "__" + State.participantId+".csv";
    }

    private static String getEventsLogFileName(){
        return Utils.getTimeStamp(true) + "__" + State.participantId + "__EVENTS" + ".csv";
    }

    public static String getDistributionsFileName(){
        return Utils.getTimeStamp(true) + "__" + State.participantId + "__DISTRIBUTIONS" + ".log";
    }

    private static String getExperimentLogFileName(){
        return ExperimentParameters.EXPERIMENT_LOG_FILE_NAME;
    }

    public static void initialize(Context context) {

        //Initialize trial log file
        State.currentTrialsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getTrialLogFileName());
        FileManager.writeToFile(State.currentTrialsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.trial_log_header)), false);

        //Initialize event log file
        State.currentEventsLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getEventsLogFileName());
        FileManager.writeToFile(State.currentEventsLogFile, Utils.appendWithComma(context.getString(R.string.state_log_header), context.getString(R.string.events_log_header)), false);

        //Initialize experiment log file
        State.currentExperimentLogFile = FileManager.getFile(context, ExperimentParameters.LOG_FOLDER, getExperimentLogFileName());
        if (!State.currentExperimentLogFile.exists()){
            FileManager.writeToFile(State.currentExperimentLogFile, Utils.appendWithComma(context.getString(R.string.experiment_log_header), context.getString(R.string.experiment_parameters_log_header)), false);
        }
        FileManager.appendLineToFile(State.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), State.participantId, ExperimentParameters.experimentToString()));
    }

    ////////////////////////////    Log distributions    ///////////////////////////

    public static void logDistributionsAtExperimentInit(Context context) {
        State.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        //general
        logStr += "GENERAL \n";
        logStr += "participantID: " + State.participantId + "\n";
        logStr += "zipfSize: " + String.valueOf(ExperimentParameters.zipfSize) + "\n";
        logStr += "zipfCoeff: " + String.valueOf(ExperimentParameters.zipfCoeff) + "\n";
        logStr += "Accuracy: " + String.valueOf(Distributions.empiricalAccuracy) + "\n";
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
            logStr += String.valueOf(Distributions.zipfian[i+1]) + " ";

        FileManager.writeLineToFile(State.currentDistributionsLogFile, logStr, false);
    }

    public static void logDistributionsAtConditionInit(Context context) {
        String logStr ="";

        //logging target positions
        logStr += lineSep;
        logStr += "TARGET POSITIONS (positions start from 1) (Zipfian rank indicated below) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithZero(Distributions.targets[tr]) + " ";
        }
        logStr+="\n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            logStr += Utils.padWithZero(Distributions.target_ranks[tr]) + " ";
        }

        //logging highlighted icons
        logStr += lineSep;
        logStr += "HIGHLIGHTED ICONS' POSITIONS \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS+1; tr++){
            for (int icon = 0; icon < State.num_highlighted_icons(); icon++)
                logStr += Utils.padWithZero(Distributions.highlighted[tr][icon]) + " ";
            logStr += "\n";
        }

        //logging image icons and labels
        logStr += lineSep;
        logStr += "IMAGE ICONS AND LABELS\n";

        String iconAddressPrefix = ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX;
        logStr += halfLineSep;
        logStr += "CONDITION " + String.valueOf(State.condition) + ": " + conditionToString(State.condition) +"\n";
        for (int pos=1; pos <= State.num_positions(); pos++){
            logStr += String.valueOf(pos) + ": " + Utils.extractIconName(context.getString(Distributions.images_ID[pos]), iconAddressPrefix) + " "  +context.getString(Distributions.labels_ID[pos]) + "; " ;
            if ((pos) % ExperimentParameters.NUM_ICONS_PER_PAGE == 0) {
                logStr += "\n";
            }

        }

        // Write things in the SAME distribution log file
        FileManager.writeLineToFile(State.currentDistributionsLogFile, logStr, false);
    }

    private static String conditionToString(int condition){
        return ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][0]]+", "
                +ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]]+", "
                +ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][2]]+" ";
    }

    public static void logPostExperimentDistributions(Context context) {
        State.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        logStr += lineSep;
        logStr += "Post-experiment accuracy: " + String.valueOf(Distributions.empiricalAccuracy) + "\n";

        //logging selected positions
        logStr += lineSep;
        logStr += "SELECTED POSITIONS (positions start from 1) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            logStr += String.valueOf(Distributions.selected[tr+1]) + " ";
        }

        //logging highlighted icons
        logStr += lineSep;
        logStr += "ACTUAL HIGHLIGHTED ICONS' POSITIONS (with empirical MRU) \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            for (int icon = 0; icon < State.num_highlighted_icons(); icon++)
                logStr += Utils.padWithZero(Distributions.highlighted[tr+1][icon]) + " ";
            logStr += "\n";
        }

        FileManager.writeLineToFile(State.currentDistributionsLogFile, logStr, false);
    }

    ///////////////////////////    Log trial    ///////////////////////////////////

    public static String stateCsvLog(Context context){
        return Utils.appendWithComma(State.participantId, String.valueOf(State.block), String.valueOf(State.accuracy),String.valueOf(State.num_pages), State.effect.toString(),
                String.valueOf(State.trial), String.valueOf(startTime),
                String.valueOf(State.targetIconPage), String.valueOf(State.targetIconRow), String.valueOf(State.targetIconColumn),
                Utils.extractIconName(context.getString(Distributions.images_ID[Distributions.targets[State.trial]]), ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX), context.getString(Distributions.labels_ID[Distributions.targets[State.trial]]),
                String.valueOf(Distributions.target_ranks[State.trial]));
    }

    public static String resultCsvLog(Result result){
        String highlightedStr = result.ifHighlighted? "Highlighted" : "Normal";
        String successStr = State.success? "Success" : "Failure";
        String timeoutStr = State.timeout? "Timeout" : "InTime";
        String missedStr = State.missed? "Miss" : "Hit";
        return Utils.appendWithComma(highlightedStr, String.valueOf(result.duration), String.valueOf(State.page), String.valueOf(result.row), String.valueOf(result.column), successStr, timeoutStr, missedStr, String.valueOf(State.num_pages_visited),result.iconName, result.iconLabel);
    }

    public static void logTrial(Context context, Result result){
        // If user stayed on the first page
        if(State.pages_times.equals(""))
            State.pages_times="1,"+result.duration+",0,";

        String finalTrialLog = Utils.appendWithComma(Utils.getTimeStamp(false), stateCsvLog(context),resultCsvLog(result), State.pages_times);
        FileManager.appendLineToFile(State.currentTrialsLogFile,finalTrialLog);
    }
}
