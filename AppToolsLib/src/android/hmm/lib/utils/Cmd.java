package android.hmm.lib.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

/**
 * @author HEMING
 * 测试通过
 * 通过return new ShellCmd().canSU()即可获得。 
 * 注意这个方法最好放在异步中做，因为不同的机器检查时间不同。
 */
public class Cmd {
	
	private Boolean can_su;
	public SH sh;
	public SH su;

	public Cmd() {
		sh = new SH("sh");
		su = new SH("su");
	}

	public boolean canSU() {
		return canSU(false);
	}

	public boolean canSU(boolean force_check) {
		if (can_su == null || force_check) {
			CmdResult r = su.runWaitFor("id");
			StringBuilder out = new StringBuilder();
			if (r.stdout != null){
				out.append(r.stdout).append(" ; ");
			}
			if (r.stderr != null){
				out.append(r.stderr);
			}
			can_su = r.success();
		}
		return can_su;
	}

	public SH suOrSH() {
		return canSU() ? su : sh;
	}

	public class CmdResult {
		private String stdout;
		private String stderr;
		private Integer exit_value;

		public CmdResult(Integer exit_value_in, String stdout_in, String stderr_in) {
			exit_value = exit_value_in;
			stdout = stdout_in;
			stderr = stderr_in;
		}
		public CmdResult(Integer exit_value_in) {
			this(exit_value_in, null, null);
		}
		public boolean success() {
			return exit_value != null && exit_value == 0;
		}
		public String getStdoutTxt(){
			return stdout;
		}
		public String getStderrTxt(){
			return stderr;
		}
		public int getExitValue(){
			return exit_value;
		}
	}

	public class SH {
		private String SHELL = "sh";

		public SH(String SHELL_in) {
			SHELL = SHELL_in;
		}

		public Process run(String s) {
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(SHELL);
				DataOutputStream toProcess = new DataOutputStream(process.getOutputStream());
				toProcess.writeBytes("exec " + s + "\n");
				toProcess.flush();
			} catch (Exception e) {
				process = null;
			}
			return process;
		}

		@SuppressWarnings("deprecation")
		private String getStreamLines(InputStream is) {
			String out = null;
			StringBuffer buffer = null;
			DataInputStream dis = new DataInputStream(is);
			try {
				if (dis.available() > 0) {
					buffer = new StringBuffer(dis.readLine());
					while (dis.available() > 0)
						buffer.append("\n").append(dis.readLine());
				}
				dis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (buffer != null)
				out = buffer.toString();
			return out;
		}

		public CmdResult runWaitFor(String s) {
			Process process = run(s);
			Integer exit_value = null;
			String stdout = null;
			String stderr = null;
			if (process != null) {
				try {
					exit_value = process.waitFor();
					stdout = getStreamLines(process.getInputStream());
					stderr = getStreamLines(process.getErrorStream());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			return new CmdResult(exit_value, stdout, stderr);
		}
	}
}
