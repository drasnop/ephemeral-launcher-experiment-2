package ca.ubc.cs.ephemerallauncherexperiment;

import android.content.Context;

/**
 * Created by Antoine on 2014-08-31.
 */
public class Logging {

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
        FileManager.appendLineToFile(State.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), State.participantId, ExperimentParameters.csvFile()));
    }

    public static void logDistributions(Context context) {
        State.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        String lineSep = "\n-------------------------------------------------------\n";
        String halfLineSep = "\n" + lineSep.substring(0, lineSep.length()/2) + "\n";

        //general
        logStr += "GENERAL \n";
        logStr += "participantID: " + State.participantId + "\n";
        logStr += "zipfSize: " + String.valueOf(Distributions.zipfSize) + "\n";
        logStr += "zipfCoeff: " + String.valueOf(Distributions.zipfCoeff) + "\n";
        logStr += "Accuracy: " + String.valueOf(Distributions.accuracy) + "\n";
        logStr += "Icon Set: " + ExperimentParameters.ICON_SET;
        logStr += "Label Set: " + ExperimentParameters.LABEL_SET;

        //logging conditions
        logStr += lineSep;
        logStr += "CONDITIONS \n";
        for (int i=0; i < ExperimentParameters.EFFECTS.values().length; i++)
            logStr += ExperimentParameters.EFFECTS.values()[i].toString() + " ";

        //logging zipfian
        logStr += lineSep;
        logStr += "ZIPF \n";
        for (int i=0; i < Distributions.zipfSize; i++)
            logStr += String.valueOf(Distributions.zipfian[i+1]) + " ";

        //logging target positions
        logStr += lineSep;
        logStr += "TARGET POSITIONS (positions start from 1) (Zipfian rank indicated below) \n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            logStr += Utils.padWithZero(Distributions.targets[tr+1]) + " ";
        }
        logStr+="\n";
        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            logStr += Utils.padWithZero(Distributions.target_ranks[tr+1]) + " ";
        }

        //logging highlighted icons
        logStr += lineSep;
        logStr += "HIGHLIGHTED ICONS' POSITIONS \n";

        for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
            for (int icon = 0; icon < State.num_highlighted_icons; icon++)
                logStr += Utils.padWithZero(Distributions.highlighted[tr+1][icon]) + " ";
            logStr += "\n";
        }

        //logging image icons and labels
        logStr += lineSep;
        logStr += "IMAGE ICONS AND LABELS\n";
        String iconAddressPrefix = ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX;
        for (int c=0; c < ExperimentParameters.NUM_CONDITIONS; c++){
            logStr += halfLineSep;
            //TODO: create function for printing CONDITION properly (all variables)
            logStr += "CONDITION " + String.valueOf(c+1) + ": " + ExperimentParameters.EFFECTS.values()[c%3].toString() +"\n";
            for (int pos=0; pos < State.num_positions; pos++){
                logStr += String.valueOf(pos+1) + ": " + Utils.extractIconName(context.getString(Distributions.images_ID[c][pos]), iconAddressPrefix) + " "  +context.getString(Distributions.labels_ID[c][pos]) + "; " ;
                if ((pos+1) % ExperimentParameters.NUM_ICONS_PER_PAGE == 0) {
                    logStr += "\n";
                }

            }
        }

        FileManager.writeLineToFile(State.currentDistributionsLogFile, logStr, false);
    }

    public static void logPostExperimentDistributions(Context context) {
        State.currentDistributionsLogFile = FileManager.getFile(context,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());

        String logStr ="";

        String lineSep = "\n-------------------------------------------------------\n";

        logStr += lineSep;
        logStr += "Post-experiment accuracy: " + String.valueOf(Distributions.accuracy) + "\n";

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
            for (int icon = 0; icon < State.num_highlighted_icons; icon++)
                logStr += Utils.padWithZero(Distributions.highlighted[tr+1][icon]) + " ";
            logStr += "\n";
        }

        FileManager.writeLineToFile(State.currentDistributionsLogFile, logStr, false);
    }
}
