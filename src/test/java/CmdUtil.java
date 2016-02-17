import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CmdUtil {
	
	private final String host;
	private final String username;
	private final String pwd;
	
	private final Executor executor = Executors.newFixedThreadPool(10);
	
	
	public CmdUtil(String host, String username, String pwd) {
		super();
		this.host = host;
		this.username = username;
		this.pwd = pwd;
	}
	
	class ConcurrentCmdTask extends Thread {

		private String cmd;
		
		public ConcurrentCmdTask(String cmd) {
			this.cmd = cmd;
		}
		
		
		public void run() {
			//此处使用之前cmdUtil里的代码
			
		}
		
	}


	public void doCmd(String cmd) {
		
		executor.execute(new ConcurrentCmdTask(cmd));
	}

}


