package android.hmm.lib.tree;

import java.util.List;

/**
 * 数据源提供接口
 * @author HMM
 */
public interface ITreeDataProvider {
	public List<?> getDataSource();

	public void foward(String caption) throws Exception;
}
