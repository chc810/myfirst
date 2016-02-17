import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;


public class CmdTaskPool {
	
	private static CmdTaskPool cmdTashPool;
	private final Map<String, CmdUtil> pool = new HashMap<String, CmdUtil>();
	private static final String HOSTS = "10.130.41.246:username:pwd,10.130.41.247:username:pwd,10.130.41.248:username:pwd,10.130.41.249:username:pwd";
	
	private CmdTaskPool() {
		String[] host = HOSTS.split(",");
		for (String h : host) {
			String[] hh = h.split(":");
			CmdUtil cmdUtil = new CmdUtil(hh[0], hh[1], hh[2]);
			pool.put(hh[0], cmdUtil);
		}
	}
	
	public synchronized static CmdTaskPool getInstance() {
		if (cmdTashPool == null) {
			cmdTashPool = new CmdTaskPool();
		}
		return cmdTashPool;
	}
	
	public void doCmd(String host, String cmd) {
		CmdUtil cmdUtil = pool.get(host);
		if (cmdUtil == null) {
//			logger.info("没有初始化该host:" + host);
		}
		cmdUtil.doCmd(cmd);
	}
	
	

}
