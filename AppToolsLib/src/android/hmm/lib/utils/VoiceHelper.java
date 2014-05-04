package android.hmm.lib.utils;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class VoiceHelper {

	private AudioManager mAudioManager;
	protected Context context;
	public static final int type_sys = 0x010;
	public static final int type_mic = type_sys + 1;
	public static final int type_rig = type_sys + 2;
	public static final int type_alr = type_sys + 3;
	public static final int type_cal = type_sys + 4;
	
	private int SYSTEMMAX = 0;
	private int SYSTEMCURRENT = 0;
	
	private int MUSICMAX = 0;
	private int MUSICCURRENT = 0;
	
	private int RINGMAX = 0;
	private int RINGCURRENT = 0;
	
	private int ALARMMAX = 0;
	private int ALARMCURRENT = 0;
	
	private int CALLMAX = 0;
	private int CALLCURRENT = 0;
	
	public VoiceHelper(Context context){
		this.context = context;
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void initDate(){
		//系统音量
		SYSTEMMAX = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
		SYSTEMCURRENT = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        Log.d("SYSTEM", "max : " + SYSTEMMAX + "current : " + SYSTEMCURRENT);

        //铃声音量
        RINGMAX = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
        RINGCURRENT = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
        Log.d("RING", "max : " + RINGMAX + "current : " + RINGCURRENT);

        //音乐音量
        MUSICMAX = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        MUSICCURRENT = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        Log.d("MUSIC", "max : " + MUSICMAX + "current : " + MUSICCURRENT);

        //提示声音音量
        ALARMMAX = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
        ALARMCURRENT = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
        Log.d("ALARM", "max : " + ALARMMAX + "current : " + ALARMCURRENT);

        //通话音量
        CALLMAX = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
        CALLCURRENT = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
        Log.d("VIOCE_CALL", "max : " + CALLMAX + "current : " + CALLCURRENT);
	}
	
    public void setAudio(int type,int numer){
    	int streamType = 0;
    	int max = getMaxAudio(type);
		if (numer < 0 || numer > max) {
			return;
		}
    	switch (type) {
		case type_sys:
			streamType = AudioManager.STREAM_SYSTEM;
			break;
		case type_mic:
			streamType = AudioManager.STREAM_MUSIC;	
			break;
		case type_alr:
			streamType = AudioManager.STREAM_ALARM;
			break;
		case type_cal:
			streamType = AudioManager.STREAM_VOICE_CALL;
			break;
		case type_rig:
			streamType = AudioManager.STREAM_RING;
			break;
		default:
			break;
		}
    	mAudioManager.setStreamVolume(streamType, numer, AudioManager.FLAG_PLAY_SOUND);
    }
    
    public int getCurrentAudio(int type){
    	int curr = 0;
    	switch (type) {
		case type_sys:
			curr = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
			break;
		case type_mic:
			curr = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );	
			break;
		case type_alr:
			curr = mAudioManager.getStreamVolume( AudioManager.STREAM_ALARM );
			break;
		case type_cal:
			curr = mAudioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
			break;
		case type_rig:
			curr = mAudioManager.getStreamVolume( AudioManager.STREAM_RING );
			break;
		default:
			break;
		}
    	return curr;
    }
    
    public int getMaxAudio(int type){
    	int Max = 0;
    	switch (type) {
		case type_sys:
			Max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
			break;
		case type_mic:
			Max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );	
			break;
		case type_alr:
			Max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_ALARM );
			break;
		case type_cal:
			Max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_VOICE_CALL );
			break;
		case type_rig:
			Max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_RING );
			break;
		default:
			break;
		}
    	return Max;
    }
    
    /*this is for a10s/rk3066
    public void switchAudioOutMode(int mode){
    	mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    	mAudioManager.switchAudioOutMode(mode);
    }
    
    public String getAudioOutMode(){
    	String mode = Integer.toString(mAudioManager.getAudioOutMode());
    	return mode;
    }
    
    public void setRingerMode(int mode){
    	mAudioManager.setRingerMode(mode); 
    	mAudioManager.setRingerMode(AudioManager.AUDIO_OUT_CHANNEL_HDMI);
    	mAudioManager.setRingerMode(AudioManager.AUDIO_OUT_CHANNEL_HDMI);
    	mAudioManager.setMode(AudioManager.AUDIO_OUT_CHANNEL_HDMI);
    }*/
	
}
