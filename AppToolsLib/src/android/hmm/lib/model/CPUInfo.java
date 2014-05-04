package android.hmm.lib.model;

import java.util.List;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2013-11-8
 * Description:  
 */
public class CPUInfo {

	private String _Name;
	private String _Serial;
	private String _Hardware;
	private int _NumCores;
	private String MinCpuFreq;
	private String MaxCpuFreq;
	private String CurCpuFreq;
	private List<String> listProcessor;

	public void setName(String name) {
		this._Name = name;
	}

	public String getName() {
		return _Name;
	}

	public void setSerial(String serial) {
		_Serial = serial;
	}

	public String getSerial() {
		return _Serial;
	}

	public void setHardware(String hardware) {
		_Hardware = hardware;
	}

	public String getHardware() {
		return _Hardware;
	}

	public void setListProcessor(List<String> listProcessor) {
		this.listProcessor = listProcessor;
	}

	public List<String> getListProcessor() {
		return listProcessor;
	}

	public void setNumCores(int _NumCores) {
		this._NumCores = _NumCores;
	}

	public int getNumCores() {
		return _NumCores;
	}

	public void setMinCpuFreq(String minCpuFreq) {
		MinCpuFreq = minCpuFreq;
	}

	public String getMinCpuFreq() {
		return MinCpuFreq;
	}

	public void setMaxCpuFreq(String maxCpuFreq) {
		MaxCpuFreq = maxCpuFreq;
	}

	public String getMaxCpuFreq() {
		return MaxCpuFreq;
	}

	public void setCurCpuFreq(String CurCpuFreq) {
		this.CurCpuFreq = CurCpuFreq;
	}

	public String getCurCpuFreq() {
		return CurCpuFreq;
	}
}
