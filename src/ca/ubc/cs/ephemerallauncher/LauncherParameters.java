package ca.ubc.cs.ephemerallauncher;

import ca.ubc.cs.ephemerallauncherexperiment.ExperimentParameters;
import ca.ubc.cs.ephemerallauncherexperiment.R;

public class LauncherParameters {

    public static final int NUM_PAGES = ExperimentParameters.NUM_PAGES;
    public static final int NUM_HIGHLIGHTED_ICONS = ExperimentParameters.NUM_HIGHLIGHTED_ICONS;
    public static final int NUM_ICONS_PER_PAGE = 20;
    
    public static AnimationType ANIMATION = AnimationType.PULSE_OUT;		// Will be used as initial animation type
    public static boolean ANIMATION_AFFECTS_OTHER_ICONS;				// Will be initialized automatically
    public static boolean ANIMATION_HAS_PREANIMATION_STATE;				// Will be initialized automatically
    
    public static final boolean HIGLIGHT_ICONS_EARLY = true;			// if true, the icons are highlighted when the user starts scrolling to the next page
    																	// if false, they are highlighted only when the user lands on the next page
    
    // for non-highlighted icons
    public static final int COLOR__START_DELAY = 500;			// ms 
    public static final int COLOR__FADE_IN_DURATION = 1000;  	// ms 

	// for highlighted icons
    public static final int DELAY = 0;
    public static final int TOTAL_DURATION = 600;
    // size
    public static final float SIZE__SMALL = 0.7f;
    public static final float SIZE__BIG = 1.25f;
    public static final float SIZE__REG = 1;						// original size
    public static final int ZOOM__DURATION = TOTAL_DURATION;  		// ms
    public static final int PULSE__DELAY = 300;  		
    public static final int PULSE__1STHALF_DURATION = PULSE__DELAY;
    public static final int PULSE__2NDHALF_DURATION = TOTAL_DURATION - PULSE__1STHALF_DURATION;
    // rotation
    public static final float DEGREE_BIG = 15f;	 			// rotate from 0 to -60 is counterclockwise rotation; 60f as 60 degree
    public static final float DEGREE_SMALL = -DEGREE_BIG;	// [AP] wouldn't it always be - DEGREE_SMALL?
    public static final float DEGREE_REG = 0f;
    public static final int TWIST__DELAY = 400;
    public static final int TWIST__0THDURATION_REL = 1;
    public static final int TWIST__1STDURATION_REL = 2;
    public static final int TWIST__2NDDURATION_REL = 1;
    
    public static final int TWIST_TOTAL_REL_DURATION = TWIST__0THDURATION_REL + TWIST__1STDURATION_REL + TWIST__2NDDURATION_REL;
    
    public static final int TWIST_REPEAT_NUM = 1;
    
    public static final int TWIST__0THDURATION = (int)((((float)TWIST__0THDURATION_REL)/((float)(TWIST_REPEAT_NUM * TWIST_TOTAL_REL_DURATION)))*((float)TOTAL_DURATION));
    public static final int TWIST__1STDURATION = (int)((((float)TWIST__1STDURATION_REL)/((float)(TWIST_REPEAT_NUM * TWIST_TOTAL_REL_DURATION)))*((float)TOTAL_DURATION));
    public static final int TWIST__2NDDURATION = (int)((((float)TWIST__2NDDURATION_REL)/((float)(TWIST_REPEAT_NUM * TWIST_TOTAL_REL_DURATION)))*((float)TOTAL_DURATION));
    
    
    // transparency
    public static final int TRANSPARENCY__DELAY = 100;
    public static final int TRANSPARENCY__DURATION = 1500;
    public static final float TRANSPARENCY__INTIAL = 0.4f;
    
    public static int BACKGROUND=1;		// 0=dark 1=ios 2=light
    
    public static enum AnimationType{
    	COLOR, ZOOM_IN, ZOOM_OUT, PULSE_IN, PULSE_OUT, TRANSPARENCY, BLUR, TWIST, NONE
    }
    
    public static void switchAnimationTo(AnimationType type, PagerAdapter pagerAdapter){	
    	ANIMATION=type;
 	
    	switch(ANIMATION){
    	case COLOR:
    	case TRANSPARENCY:
    	case BLUR:	
    		ANIMATION_AFFECTS_OTHER_ICONS=true;
    		ANIMATION_HAS_PREANIMATION_STATE=true;
    		break;
		default:
			ANIMATION_AFFECTS_OTHER_ICONS=false;
			ANIMATION_HAS_PREANIMATION_STATE=false;
			break;
    	}
    	
    	// Play animation
    	pagerAdapter.getCurrentPage().getGridView().startPreAnimation();
    	pagerAdapter.getCurrentPage().getGridView().startEphemeralAnimation();
    }
    
	// IDs of our images	
	public static final Integer[] images_ID= { R.drawable.icon_0, 
		R.drawable.icon_1, R.drawable.icon_2, R.drawable.icon_3, 
		R.drawable.icon_4, R.drawable.icon_5, R.drawable.icon_6, 
		R.drawable.icon_7, R.drawable.icon_8, R.drawable.icon_9, 
		R.drawable.icon_10, R.drawable.icon_11, R.drawable.icon_12, 
		R.drawable.icon_13, R.drawable.icon_14, R.drawable.icon_15, 
		R.drawable.icon_16, R.drawable.icon_17, R.drawable.icon_18, 
		R.drawable.icon_19, R.drawable.icon_20, R.drawable.icon_21, 
		R.drawable.icon_22, R.drawable.icon_23, R.drawable.icon_24, 
		R.drawable.icon_25, R.drawable.icon_26, R.drawable.icon_27, 
		R.drawable.icon_28, R.drawable.icon_29, R.drawable.icon_30, 
		R.drawable.icon_31, R.drawable.icon_32, R.drawable.icon_33, 
		R.drawable.icon_34, R.drawable.icon_35, R.drawable.icon_36, 
		R.drawable.icon_37, R.drawable.icon_38, R.drawable.icon_39, 
		R.drawable.icon_40, R.drawable.icon_41, R.drawable.icon_42, 
		R.drawable.icon_43, R.drawable.icon_44, R.drawable.icon_45, 
		R.drawable.icon_46, R.drawable.icon_47, R.drawable.icon_48, 
		R.drawable.icon_49, R.drawable.icon_50, R.drawable.icon_51, 
		R.drawable.icon_52, R.drawable.icon_53, R.drawable.icon_54, 
		R.drawable.icon_55, R.drawable.icon_56, R.drawable.icon_57, 
		R.drawable.icon_58, R.drawable.icon_59, R.drawable.icon_60, 
		R.drawable.icon_61, R.drawable.icon_62, R.drawable.icon_63, 
		R.drawable.icon_64, R.drawable.icon_65, R.drawable.icon_66, 
		R.drawable.icon_67, R.drawable.icon_68, R.drawable.icon_69, 
		R.drawable.icon_70, R.drawable.icon_71, R.drawable.icon_72, 
		R.drawable.icon_73, R.drawable.icon_74, R.drawable.icon_75, 
		R.drawable.icon_76, R.drawable.icon_77, R.drawable.icon_78, 
		R.drawable.icon_79, R.drawable.icon_80, R.drawable.icon_81, 
		R.drawable.icon_82, R.drawable.icon_83, R.drawable.icon_84, 
		R.drawable.icon_85, R.drawable.icon_86, R.drawable.icon_87, 
		R.drawable.icon_88, R.drawable.icon_89, R.drawable.icon_90, 
		R.drawable.icon_91, R.drawable.icon_92, R.drawable.icon_93, 
		R.drawable.icon_94, R.drawable.icon_95, R.drawable.icon_96, 
		R.drawable.icon_97, R.drawable.icon_98, R.drawable.icon_99, 
		R.drawable.icon_100, R.drawable.icon_101, R.drawable.icon_102, 
		R.drawable.icon_103, R.drawable.icon_104, R.drawable.icon_105, 
		R.drawable.icon_106, R.drawable.icon_107, R.drawable.icon_108, 
		R.drawable.icon_109, R.drawable.icon_110, R.drawable.icon_111, 
		R.drawable.icon_112, R.drawable.icon_113, R.drawable.icon_114, 
		R.drawable.icon_115, R.drawable.icon_116, R.drawable.icon_117, 
		R.drawable.icon_118, R.drawable.icon_119, R.drawable.icon_120, 
		R.drawable.icon_121, R.drawable.icon_122, R.drawable.icon_123, 
		R.drawable.icon_124, R.drawable.icon_125, R.drawable.icon_126, 
		R.drawable.icon_127, R.drawable.icon_128, R.drawable.icon_129, 
		R.drawable.icon_130, R.drawable.icon_131, R.drawable.icon_132, 
		R.drawable.icon_133, R.drawable.icon_134, R.drawable.icon_135, 
		R.drawable.icon_136, R.drawable.icon_137, R.drawable.icon_138, 
		R.drawable.icon_139, R.drawable.icon_140, R.drawable.icon_141, 
		R.drawable.icon_142, R.drawable.icon_143, R.drawable.icon_144, 
		R.drawable.icon_145, R.drawable.icon_146, R.drawable.icon_147, 
		R.drawable.icon_148, R.drawable.icon_149, R.drawable.icon_150, 
		R.drawable.icon_151, R.drawable.icon_152, R.drawable.icon_153, 
		R.drawable.icon_154, R.drawable.icon_155, R.drawable.icon_156, 
		R.drawable.icon_157, R.drawable.icon_158, R.drawable.icon_159, 
		R.drawable.icon_160, R.drawable.icon_161, R.drawable.icon_162, 
		R.drawable.icon_163, R.drawable.icon_164, R.drawable.icon_165, 
		R.drawable.icon_166, R.drawable.icon_167, R.drawable.icon_168, 
		R.drawable.icon_169, R.drawable.icon_170, R.drawable.icon_171, 
		R.drawable.icon_172, R.drawable.icon_173, R.drawable.icon_174, 
		R.drawable.icon_175, R.drawable.icon_176, R.drawable.icon_177, 
		R.drawable.icon_178, R.drawable.icon_179, R.drawable.icon_180, 
		R.drawable.icon_181, R.drawable.icon_182, R.drawable.icon_183, 
		R.drawable.icon_184, R.drawable.icon_185, R.drawable.icon_186, 
		R.drawable.icon_187, R.drawable.icon_188, R.drawable.icon_189, 
		R.drawable.icon_190, R.drawable.icon_191, R.drawable.icon_192, 
		R.drawable.icon_193, R.drawable.icon_194, R.drawable.icon_195, 
		R.drawable.icon_196, R.drawable.icon_197, R.drawable.icon_198, 
		R.drawable.icon_199, R.drawable.icon_200, R.drawable.icon_201, 
		R.drawable.icon_202, R.drawable.icon_203, R.drawable.icon_204, 
		R.drawable.icon_205, R.drawable.icon_206, R.drawable.icon_207, 
		R.drawable.icon_208, R.drawable.icon_209, R.drawable.icon_210, 
		R.drawable.icon_211, R.drawable.icon_212, R.drawable.icon_213, 
		R.drawable.icon_214, R.drawable.icon_215, R.drawable.icon_216, 
		R.drawable.icon_217, R.drawable.icon_218, R.drawable.icon_219, 
		R.drawable.icon_220, R.drawable.icon_221, R.drawable.icon_222, 
		R.drawable.icon_223, R.drawable.icon_224, R.drawable.icon_225, 
		R.drawable.icon_226, R.drawable.icon_227, R.drawable.icon_228, 
		R.drawable.icon_229, R.drawable.icon_230, R.drawable.icon_231, 
		R.drawable.icon_232, R.drawable.icon_233, R.drawable.icon_234, 
		R.drawable.icon_235, R.drawable.icon_236, R.drawable.icon_237, 
		R.drawable.icon_238, R.drawable.icon_239, R.drawable.icon_240, 
		R.drawable.icon_241, R.drawable.icon_242, R.drawable.icon_243, 
		R.drawable.icon_244, R.drawable.icon_245, R.drawable.icon_246, 
		R.drawable.icon_247, R.drawable.icon_248, R.drawable.icon_249, 
		R.drawable.icon_250, R.drawable.icon_251, R.drawable.icon_252, 
		R.drawable.icon_253, R.drawable.icon_254, R.drawable.icon_255, 
		R.drawable.icon_256, R.drawable.icon_257, R.drawable.icon_258, 
		R.drawable.icon_259, R.drawable.icon_260, R.drawable.icon_261, 
		R.drawable.icon_262, R.drawable.icon_263, R.drawable.icon_264, 
		R.drawable.icon_265, R.drawable.icon_266, R.drawable.icon_267, 
		R.drawable.icon_268, R.drawable.icon_269, R.drawable.icon_270, 
		R.drawable.icon_271, R.drawable.icon_272, R.drawable.icon_273, 
		R.drawable.icon_274, R.drawable.icon_275, R.drawable.icon_276, 
		R.drawable.icon_277, R.drawable.icon_278, R.drawable.icon_279, 
		R.drawable.icon_280, R.drawable.icon_281, R.drawable.icon_282, 
		R.drawable.icon_283, R.drawable.icon_284, R.drawable.icon_285, 
		R.drawable.icon_286, R.drawable.icon_287, R.drawable.icon_288, 
		R.drawable.icon_289, R.drawable.icon_290, R.drawable.icon_291, 
		R.drawable.icon_292, R.drawable.icon_293, R.drawable.icon_294, 
		R.drawable.icon_295, R.drawable.icon_296, R.drawable.icon_297, 
		R.drawable.icon_298, R.drawable.icon_299, R.drawable.icon_300};
	
	public static final Integer[] images_gs_ID = {R.drawable.icon_0_gs, R.drawable.icon_1_gs,
		R.drawable.icon_2_gs, R.drawable.icon_3_gs, R.drawable.icon_4_gs,
		R.drawable.icon_5_gs, R.drawable.icon_6_gs, R.drawable.icon_7_gs,
		R.drawable.icon_8_gs, R.drawable.icon_9_gs, R.drawable.icon_10_gs,
		R.drawable.icon_11_gs, R.drawable.icon_12_gs, R.drawable.icon_13_gs,
		R.drawable.icon_14_gs, R.drawable.icon_15_gs, R.drawable.icon_16_gs,
		R.drawable.icon_17_gs, R.drawable.icon_18_gs, R.drawable.icon_19_gs};
	
	public static final Integer[] images_b_ID = {R.drawable.icon_0_b, R.drawable.icon_1_b,
		R.drawable.icon_2_b, R.drawable.icon_3_b, R.drawable.icon_4_b,
		R.drawable.icon_5_b, R.drawable.icon_6_b, R.drawable.icon_7_b,
		R.drawable.icon_8_b, R.drawable.icon_9_b, R.drawable.icon_10_b,
		R.drawable.icon_11_b, R.drawable.icon_12_b, R.drawable.icon_13_b,
		R.drawable.icon_14_b, R.drawable.icon_15_b, R.drawable.icon_16_b,
		R.drawable.icon_17_b, R.drawable.icon_18_b, R.drawable.icon_19_b};
	
	public static final Integer[] labels_ID = {R.string.icon_0_label, 
		R.string.icon_1_label, R.string.icon_2_label, R.string.icon_3_label, 
		R.string.icon_4_label, R.string.icon_5_label, R.string.icon_6_label, 
		R.string.icon_7_label, R.string.icon_8_label, R.string.icon_9_label, 
		R.string.icon_10_label, R.string.icon_11_label, R.string.icon_12_label, 
		R.string.icon_13_label, R.string.icon_14_label, R.string.icon_15_label, 
		R.string.icon_16_label, R.string.icon_17_label, R.string.icon_18_label, 
		R.string.icon_19_label, R.string.icon_20_label, R.string.icon_21_label, 
		R.string.icon_22_label, R.string.icon_23_label, R.string.icon_24_label, 
		R.string.icon_25_label, R.string.icon_26_label, R.string.icon_27_label, 
		R.string.icon_28_label, R.string.icon_29_label, R.string.icon_30_label, 
		R.string.icon_31_label, R.string.icon_32_label, R.string.icon_33_label, 
		R.string.icon_34_label, R.string.icon_35_label, R.string.icon_36_label, 
		R.string.icon_37_label, R.string.icon_38_label, R.string.icon_39_label, 
		R.string.icon_40_label, R.string.icon_41_label, R.string.icon_42_label, 
		R.string.icon_43_label, R.string.icon_44_label, R.string.icon_45_label, 
		R.string.icon_46_label, R.string.icon_47_label, R.string.icon_48_label, 
		R.string.icon_49_label, R.string.icon_50_label, R.string.icon_51_label, 
		R.string.icon_52_label, R.string.icon_53_label, R.string.icon_54_label, 
		R.string.icon_55_label, R.string.icon_56_label, R.string.icon_57_label, 
		R.string.icon_58_label, R.string.icon_59_label, R.string.icon_60_label, 
		R.string.icon_61_label, R.string.icon_62_label, R.string.icon_63_label, 
		R.string.icon_64_label, R.string.icon_65_label, R.string.icon_66_label, 
		R.string.icon_67_label, R.string.icon_68_label, R.string.icon_69_label, 
		R.string.icon_70_label, R.string.icon_71_label, R.string.icon_72_label, 
		R.string.icon_73_label, R.string.icon_74_label, R.string.icon_75_label, 
		R.string.icon_76_label, R.string.icon_77_label, R.string.icon_78_label, 
		R.string.icon_79_label, R.string.icon_80_label, R.string.icon_81_label, 
		R.string.icon_82_label, R.string.icon_83_label, R.string.icon_84_label, 
		R.string.icon_85_label, R.string.icon_86_label, R.string.icon_87_label, 
		R.string.icon_88_label, R.string.icon_89_label, R.string.icon_90_label, 
		R.string.icon_91_label, R.string.icon_92_label, R.string.icon_93_label, 
		R.string.icon_94_label, R.string.icon_95_label, R.string.icon_96_label, 
		R.string.icon_97_label, R.string.icon_98_label, R.string.icon_99_label, 
		R.string.icon_100_label, R.string.icon_101_label, R.string.icon_102_label, 
		R.string.icon_103_label, R.string.icon_104_label, R.string.icon_105_label, 
		R.string.icon_106_label, R.string.icon_107_label, R.string.icon_108_label, 
		R.string.icon_109_label, R.string.icon_110_label, R.string.icon_111_label, 
		R.string.icon_112_label, R.string.icon_113_label, R.string.icon_114_label, 
		R.string.icon_115_label, R.string.icon_116_label, R.string.icon_117_label, 
		R.string.icon_118_label, R.string.icon_119_label, R.string.icon_120_label, 
		R.string.icon_121_label, R.string.icon_122_label, R.string.icon_123_label, 
		R.string.icon_124_label, R.string.icon_125_label, R.string.icon_126_label, 
		R.string.icon_127_label, R.string.icon_128_label, R.string.icon_129_label, 
		R.string.icon_130_label, R.string.icon_131_label, R.string.icon_132_label, 
		R.string.icon_133_label, R.string.icon_134_label, R.string.icon_135_label, 
		R.string.icon_136_label, R.string.icon_137_label, R.string.icon_138_label, 
		R.string.icon_139_label, R.string.icon_140_label, R.string.icon_141_label, 
		R.string.icon_142_label, R.string.icon_143_label, R.string.icon_144_label, 
		R.string.icon_145_label, R.string.icon_146_label, R.string.icon_147_label, 
		R.string.icon_148_label, R.string.icon_149_label, R.string.icon_150_label, 
		R.string.icon_151_label, R.string.icon_152_label, R.string.icon_153_label, 
		R.string.icon_154_label, R.string.icon_155_label, R.string.icon_156_label, 
		R.string.icon_157_label, R.string.icon_158_label, R.string.icon_159_label, 
		R.string.icon_160_label, R.string.icon_161_label, R.string.icon_162_label, 
		R.string.icon_163_label, R.string.icon_164_label, R.string.icon_165_label, 
		R.string.icon_166_label, R.string.icon_167_label, R.string.icon_168_label, 
		R.string.icon_169_label, R.string.icon_170_label, R.string.icon_171_label, 
		R.string.icon_172_label, R.string.icon_173_label, R.string.icon_174_label, 
		R.string.icon_175_label, R.string.icon_176_label, R.string.icon_177_label, 
		R.string.icon_178_label, R.string.icon_179_label, R.string.icon_180_label, 
		R.string.icon_181_label, R.string.icon_182_label, R.string.icon_183_label, 
		R.string.icon_184_label, R.string.icon_185_label, R.string.icon_186_label, 
		R.string.icon_187_label, R.string.icon_188_label, R.string.icon_189_label, 
		R.string.icon_190_label, R.string.icon_191_label, R.string.icon_192_label, 
		R.string.icon_193_label, R.string.icon_194_label, R.string.icon_195_label, 
		R.string.icon_196_label, R.string.icon_197_label, R.string.icon_198_label, 
		R.string.icon_199_label, R.string.icon_200_label, R.string.icon_201_label, 
		R.string.icon_202_label, R.string.icon_203_label, R.string.icon_204_label, 
		R.string.icon_205_label, R.string.icon_206_label, R.string.icon_207_label, 
		R.string.icon_208_label, R.string.icon_209_label, R.string.icon_210_label, 
		R.string.icon_211_label, R.string.icon_212_label, R.string.icon_213_label, 
		R.string.icon_214_label, R.string.icon_215_label, R.string.icon_216_label, 
		R.string.icon_217_label, R.string.icon_218_label, R.string.icon_219_label, 
		R.string.icon_220_label, R.string.icon_221_label, R.string.icon_222_label, 
		R.string.icon_223_label, R.string.icon_224_label, R.string.icon_225_label, 
		R.string.icon_226_label, R.string.icon_227_label, R.string.icon_228_label, 
		R.string.icon_229_label, R.string.icon_230_label, R.string.icon_231_label, 
		R.string.icon_232_label, R.string.icon_233_label, R.string.icon_234_label, 
		R.string.icon_235_label, R.string.icon_236_label, R.string.icon_237_label, 
		R.string.icon_238_label, R.string.icon_239_label, R.string.icon_240_label, 
		R.string.icon_241_label, R.string.icon_242_label, R.string.icon_243_label, 
		R.string.icon_244_label, R.string.icon_245_label, R.string.icon_246_label, 
		R.string.icon_247_label, R.string.icon_248_label, R.string.icon_249_label, 
		R.string.icon_250_label, R.string.icon_251_label, R.string.icon_252_label, 
		R.string.icon_253_label, R.string.icon_254_label, R.string.icon_255_label, 
		R.string.icon_256_label, R.string.icon_257_label, R.string.icon_258_label, 
		R.string.icon_259_label, R.string.icon_260_label, R.string.icon_261_label, 
		R.string.icon_262_label, R.string.icon_263_label, R.string.icon_264_label, 
		R.string.icon_265_label, R.string.icon_266_label, R.string.icon_267_label, 
		R.string.icon_268_label, R.string.icon_269_label, R.string.icon_270_label, 
		R.string.icon_271_label, R.string.icon_272_label, R.string.icon_273_label, 
		R.string.icon_274_label, R.string.icon_275_label, R.string.icon_276_label, 
		R.string.icon_277_label, R.string.icon_278_label, R.string.icon_279_label, 
		R.string.icon_280_label, R.string.icon_281_label, R.string.icon_282_label, 
		R.string.icon_283_label, R.string.icon_284_label, R.string.icon_285_label, 
		R.string.icon_286_label, R.string.icon_287_label, R.string.icon_288_label, 
		R.string.icon_289_label, R.string.icon_290_label, R.string.icon_291_label, 
		R.string.icon_292_label, R.string.icon_293_label, R.string.icon_294_label, 
		R.string.icon_295_label, R.string.icon_296_label, R.string.icon_297_label, 
		R.string.icon_298_label, R.string.icon_299_label, R.string.icon_300_label, 
		R.string.icon_301_label, R.string.icon_302_label, R.string.icon_303_label, 
		R.string.icon_304_label, R.string.icon_305_label, R.string.icon_306_label, 
		R.string.icon_307_label, R.string.icon_308_label, R.string.icon_309_label, 
		R.string.icon_310_label, R.string.icon_311_label, R.string.icon_312_label, 
		R.string.icon_313_label, R.string.icon_314_label, R.string.icon_315_label, 
		R.string.icon_316_label, R.string.icon_317_label, R.string.icon_318_label 
};
}
