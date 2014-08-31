package ca.ubc.cs.ephemerallauncher;

import android.animation.Animator;

import java.util.ArrayList;





public class Animation {

    private static ArrayList<Animator> animatorList = new ArrayList<Animator>();

    public static void clearAll(){
        stopAll();
        animatorList.clear();
    }

    public static void stopAll(){
        for (Animator animation : animatorList){
            animation.end();
        }
    }
    public static void color(Icon icon){
        animatorList.add(Effects.changeToColor(icon));
    }

    public static void zoom_in(Icon icon){
        animatorList.add(Effects.changeSize(icon,LauncherParameters.ZOOM__DURATION,LauncherParameters.DELAY,LauncherParameters.SIZE__SMALL,LauncherParameters.SIZE__REG));
    }

    public static void zoom_out(Icon icon){
        animatorList.add(Effects.changeSize(icon,LauncherParameters.ZOOM__DURATION,LauncherParameters.DELAY,LauncherParameters.SIZE__BIG,LauncherParameters.SIZE__REG));
    }

    public static void pulse_in(Icon icon){
        animatorList.add(Effects.changeSize(icon,LauncherParameters.PULSE__1STHALF_DURATION,LauncherParameters.DELAY,LauncherParameters.SIZE__REG,LauncherParameters.SIZE__SMALL));
        animatorList.add(Effects.changeSize(icon,LauncherParameters.PULSE__2NDHALF_DURATION,LauncherParameters.PULSE__DELAY,LauncherParameters.SIZE__SMALL,LauncherParameters.SIZE__REG));
    }

    public static void pulse_out(Icon icon){
        animatorList.add(Effects.changeSize(icon,LauncherParameters.PULSE__1STHALF_DURATION,LauncherParameters.DELAY,LauncherParameters.SIZE__REG,LauncherParameters.SIZE__BIG));
        animatorList.add(Effects.changeSize(icon,LauncherParameters.PULSE__2NDHALF_DURATION,LauncherParameters.PULSE__DELAY,LauncherParameters.SIZE__BIG,LauncherParameters.SIZE__REG));
    }

    public static void twist(Icon icon){
		/*Effects.rotate(icon,Parameters.SHAKE__1ST_DURATION,Parameters.DELAY,Parameters.DEGREE_REG,Parameters.DEGREE_SMALL);
		Effects.rotate(icon,Parameters.SHAKE__2ND_DURATION,Parameters.SHAKE__DELAY1,Parameters.DEGREE_SMALL,Parameters.DEGREE_BIG);
		Effects.rotate(icon,Parameters.SHAKE__3RD_DURATION,Parameters.SHAKE__DELAY2,Parameters.DEGREE_BIG,Parameters.DEGREE_REG);*/
	
		/*Effects.rotate(icon, Parameters.TWIST__0THDURATION, Parameters.DELAY, Parameters.DEGREE_REG, Parameters.DEGREE_SMALL);
		Effects.rotate(icon,Parameters.TWIST__1STDURATION,Parameters.TWIST__0THDURATION,Parameters.DEGREE_BIG);
		Effects.rotate(icon,Parameters.TWIST__2NDDURATION,Parameters.TWIST__0THDURATION+Parameters.TWIST__1STDURATION,Parameters.DEGREE_REG);*/

        int repeatNum = LauncherParameters.TWIST_REPEAT_NUM;
        int duration0 = LauncherParameters.TWIST__0THDURATION;
        int duration1 = LauncherParameters.TWIST__1STDURATION;
        int duration2 = LauncherParameters.TWIST__2NDDURATION;

        int accumulatedDelay = LauncherParameters.DELAY;

        //int totalRelDuration = Parameters.TWIST__0THDURATION_REL + Parameters.TWIST__1STDURATION_REL + Parameters.TWIST__2NDDURATION_REL;
		
		/*int duration0 = (int)(((1.5f)/6f)*600f);
		int duration1 = (int)(((3f)/6f)*600f);
		int duration2 = (int)(((1.5f)/6f)*600f);*/


        for (int i=0; i < repeatNum; i++){
            animatorList.add(Effects.rotate(icon, duration0, accumulatedDelay, LauncherParameters.DEGREE_REG, LauncherParameters.DEGREE_SMALL));
            accumulatedDelay += duration0;

            animatorList.add(Effects.rotate(icon,duration1,accumulatedDelay,LauncherParameters.DEGREE_BIG));
            accumulatedDelay += duration1;

            animatorList.add(Effects.rotate(icon,duration2, accumulatedDelay,LauncherParameters.DEGREE_REG));
            accumulatedDelay += duration2;
        }
    }


    public static void fadeIn(Icon icon){
        animatorList.add(Effects.fadeIn(icon,LauncherParameters.TRANSPARENCY__DURATION,LauncherParameters.TRANSPARENCY__DELAY,LauncherParameters.TRANSPARENCY__INTIAL,1f));
    }

    public static void changeToTransparent(Icon icon){
        animatorList.add(Effects.fadeOut(icon, 0, 0,1f,LauncherParameters.TRANSPARENCY__INTIAL));
    }

}
