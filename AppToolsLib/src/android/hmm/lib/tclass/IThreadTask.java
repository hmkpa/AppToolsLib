package android.hmm.lib.tclass;

public interface IThreadTask {
	//
	public void prepare();

	//
	public void process();

	//
	public void handleResult();

	//
	public void handleError();
}