package com.test.othertest;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * <dl>
 * <dt>JschClient</dt>
 * <dd>Description:java与linux通过ssh方式交互的客户端</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2014年7月30日</dd>
 * </dl>
 * 
 * @author 安宁
 */
public class JschClient {
	public static final String SHELL_TYPE = "shell";
	public static final String EXEC_TYPE = "exec";
	public static final String SFTP_TYPE = "sftp";

	private Log logger = LogFactory.getLog(getClass());
	/**
	 * ssh 用户名
	 */
	private String userName;
	/**
	 * ssh 密码
	 */
	private String password;
	/**
	 * ssh ip
	 */
	private String ip;
	/**
	 * ssh port，一般是22
	 */
	private int port;
	private JSch jsch = new JSch();
	private Session session;
	private int timeOut = 3000;
	private ChannelSftp  channel;

	public JschClient() {
		super();
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public JschClient(String userName, String password, String ip, int port) {
		super();
		this.userName = userName;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}

	/**
	 * @Description: 打开session，需要用户自行关闭session
	 * @return boolean
	 * @throws
	 * @author 安宁 2014年7月29日
	 */
	public boolean openSession() {
		try {
			session = jsch.getSession(userName, ip, port);
			session.setPassword(password);
			session.setTimeout(timeOut);
			session.setUserInfo(new MyUserInfo());
			session.connect();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				logger.error("打开jsch session的时候线程被中断!");
			}
			if(session.isConnected()==false){
				session.connect();
			}
			logger.info(String.format("创建session[%s@%s]成功。", userName, ip));
			return true;
		} catch (JSchException e) {
			String error = String.format("创建session[%s@%s]失败,原因:%s。", userName,
					ip, e.getMessage());
//			logger.error(error, e);
			throw new ServiceException(error, e);
		}
	}

	/**
	 * @Description: 关闭session void
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public void closeSession() {
		session.disconnect();
	}

	/**
	 * @Description: 上传操作
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public boolean upLoad(String remotePath, String localPath) {
		ChannelSftp channelSftp = openChannelSftp();
		try {
			channelSftp.connect();
			channelSftp.put(localPath, remotePath);
		} catch (Exception e) {
			String error = String.format("上传文件到[%s@%s]失败,原因:%s。", userName, ip,
					e.getMessage());
			logger.error(error, e);
			throw new ServiceException(error, e);
		} finally {
			channelSftp.disconnect();
		}
		return true;
	}
	/**
	 * @Description: 会重试的上传，保证上传的文件是正确的
	 * @param remotePath
	 * @param localPath
	 * @param tryTimes 最多可重试时间，如果不允许重试则置0
	 * @return boolean
	 * @throws
	 * @author 安宁 2014年10月12日
	 */
	public boolean upLoad(String remotePath,String localPath ,int tryTimes){
		File localFile = new File(localPath);
		boolean check = checkFileMd5(remotePath, localFile.getParentFile().getPath(),localFile.getName());
		if(!check){
			upLoad(remotePath, localPath);
			check = checkFileMd5(remotePath, localFile.getParentFile().getPath(),localFile.getName());
		}
		while(!check && tryTimes>0){
			check = upLoad(remotePath, localPath,tryTimes-1);
		}
		return check;
	}
	public boolean checkFileMd5(String remotePath, String localPath,String fileName){
		List<String> list = new ArrayList<String>();
		list.add("md5sum "+remotePath+fileName);
		String md5 = "";
		String localMd5 = "1";
		String result = exeShellCommand(list);
		Pattern pa = Pattern.compile("(\\w{32})\\s");
		Matcher ma = pa.matcher(result);
		md5 = "";
		if(ma.find()){
			md5 = ma.group(1);
		}
		localMd5 = "";
		if(localMd5.equals(md5)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @Description: 下载操作
	 * @param remotePath
	 * @param localPath
	 * @return boolean
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public boolean downLoad(String remotePath, String localPath) {
		ChannelSftp channelSftp = openChannelSftp();
		try {
			channelSftp.connect();
			channelSftp.get(remotePath, localPath);
			return true;
		} catch (Exception e) {
			String error = String.format("下载文件到[%s@%s]失败,原因:%s。", userName, ip,
					e.getMessage());
			logger.error(error, e);
			throw new ServiceException(error, e);
		} finally {
			channelSftp.disconnect();
		}
	}

	/**
	 * @Description: 顺序执行一批命令
	 * @param commandList
	 * @return String
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public String exeShellCommand(List<String> commandList) {
		ChannelShell channelShell = openChannelShell();
		boolean retry = false;
		try {
			PipedInputStream pipeIn = new PipedInputStream();  
	        PipedOutputStream pipeOut = new PipedOutputStream( pipeIn );
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        channelShell.setInputStream(pipeIn);  
	        channelShell.setOutputStream(outputStream);
			channelShell.connect(timeOut);
			outputStream.reset();
			StringBuilder sb = new StringBuilder();
			String str = new String(outputStream.toByteArray());
			sb.append(str).append("\r\n");
			for (String command : commandList) {
				command += ECHO_STR;
				logger.debug(command);
				pipeOut.write(command.getBytes());
				TimeUnit.SECONDS.sleep(1);
				int count = 0;
				while( !(str = new String(outputStream.toByteArray())).contains(ECHO_STR_RESULT)){
					count ++ ;
					if(count > 10 ){
						retry = true;
					}
					TimeUnit.MILLISECONDS.sleep(300);
				}
				str = str.replaceAll(ECHO_STR_RESULT, "");
				sb.append(str).append("\r\n");
				outputStream.reset();
			}
            pipeIn.close(); 
	        pipeOut.close();  
	        pipeIn.close(); 
	        outputStream.close();
	        channelShell.disconnect();  
			return sb.toString();
		} catch (Exception e) {
			String error = String.format("执行命令到[%s@%s]失败,原因:%s。",userName,ip,e.getMessage());
			logger.error(error,e);
			throw new ServiceException(error, e);
		}finally{
			channelShell.disconnect();
			if(retry){
				exeShellCommand(commandList);
			}
		}

	}

	/**
	 * @Description: 执行一条命令
	 * @param command
	 * @return String
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public String exeExecCommand(String command) {
		ChannelShell channelShell = openChannelShell();
		PipedInputStream pipeIn = null;
		PipedOutputStream pipeOut = null;
		ByteArrayOutputStream outputStream = null;
		
		try {
			pipeIn = new PipedInputStream();  
	        pipeOut = new PipedOutputStream( pipeIn );
	        outputStream = new ByteArrayOutputStream();
	        channelShell.setInputStream(pipeIn);  
	        channelShell.setOutputStream(outputStream);
			channelShell.connect(timeOut);
			outputStream.reset();
			StringBuilder sb = new StringBuilder();
			String str = new String(outputStream.toByteArray());
			sb.append(str).append("\r\n");
			
			command += ECHO_STR;
			logger.debug(command);
			pipeOut.write(command.getBytes());
			while( !(str = new String(outputStream.toByteArray())).contains(ECHO_STR_RESULT)){
				TimeUnit.MILLISECONDS.sleep(10);
			}
			str = str.replaceAll(ECHO_STR_RESULT, "");
			sb.append(str).append("\r\n");
			outputStream.reset();
			
            pipeIn.close(); 
	        pipeOut.close();  
	        pipeIn.close(); 
	        outputStream.close();
	        channelShell.disconnect();  
			return sb.toString();
		} catch (Exception e) {
			String error = String.format("执行命令到[%s@%s]失败,原因:%s。",userName,ip,e.getMessage());
//			logger.error(error,e);
			throw new ServiceException(error, e);
		}finally{
			channelShell.disconnect();
			if(pipeIn != null){
				try {
					pipeIn.close();
				} catch (IOException e) {
					logger.error("关流失败"+e.getMessage(), e);
					throw new ServiceException(e.getMessage(), e);
				} 
			}
			if(pipeOut != null){
				try {
					pipeOut.close();
				} catch (IOException e) {
					logger.error("关流失败"+e.getMessage(), e);
					throw new ServiceException(e.getMessage(), e);
				} 
			}
			if(pipeIn != null){
				try {
					pipeIn.close();
				} catch (IOException e) {
					logger.error("关流失败"+e.getMessage(), e);
					throw new ServiceException(e.getMessage(), e);
				}
			}
		}

	}
	public String exeExecCommandOnce(String command) {
		ChannelExec channelExec = openChannelExec();
		InputStream inputStream = null;
		InputStreamReader in = null;
		BufferedReader inReader = null;
		try {
			channelExec.setCommand(command);
			channelExec.setInputStream(null);
			channelExec.connect(timeOut);
			logger.debug(command);
			inputStream = channelExec.getInputStream();
			in = new InputStreamReader(
					inputStream);
			inReader = new BufferedReader(in);
			String result = parseExecResult(inReader);
			logger.debug(result);
			
			return result;
		} catch (Exception e) {
			String error = String.format("执行命令到[%s@%s]失败,原因:%s。", userName, ip,
					e.getMessage());
//			logger.error(error, e);
			throw new ServiceException(error, e);
		} finally {
			try {
				inReader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage(), e);
			}
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage(), e);
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage(), e);
			}
			channelExec.disconnect();
		}
	}

	private String parseExecResult(BufferedReader lines) throws IOException {
		StringBuffer output = new StringBuffer();
		char[] buf = new char[512];
		int nRead;
		while ((nRead = lines.read(buf, 0, buf.length)) > 0) {
			output.append(buf, 0, nRead);
		}
		return output.toString();
	}

	/**
	 * @Description: 打开shell的channel，可交互channel
	 * @return ChannelShell
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	private ChannelShell openChannelShell() {
		try {
			return (ChannelShell) session.openChannel(SHELL_TYPE);
		} catch (JSchException e) {
			String error = String.format("创建%s_channel[%s@%s]失败,原因:%s。",
					SHELL_TYPE, userName, ip, e.getMessage());
			logger.error(error, e);
			throw new ServiceException(error, e);
		}
	}

	/**
	 * @Description: 打开sftp的channel，可以上传与下载
	 * @return ChannelSftp
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public ChannelSftp openChannelSftp() {
		try {
			return (ChannelSftp) session.openChannel(SFTP_TYPE);
		} catch (JSchException e) {
			String error = String.format("创建%s_channel[%s@%s]失败,原因:%s。",
					SFTP_TYPE, userName, ip, e.getMessage());
			logger.error(error, e);
			throw new ServiceException(error, e);
		}
	}

	/**
	 * @Description: 打开linux命令的channel，只执行一条命令
	 * @return ChannelExec
	 * @throws
	 * @author 安宁 2014年7月30日
	 */
	public ChannelExec openChannelExec() {
		try {
			return (ChannelExec) session.openChannel(EXEC_TYPE);
		} catch (JSchException e) {
			String error = String.format("创建%s_channel[%s@%s]失败,原因:%s。",
					EXEC_TYPE, userName, ip, e.getMessage());
			logger.error(error, e);
			throw new ServiceException(error, e);
		}
	}

	/**
	 * 交互执行命令的后缀。会自动添加在每一条linux指令后，并发往remote linux 执行
	 */
	public static final String ECHO_STR = ";echo 'qn_ok'$((1+1))\n";
	/**
	 * 结果标示，如返回的字符串中，包含该字符串。则代表一条指令结束。
	 */
	public static final String ECHO_STR_RESULT = "qn_ok2";

	public static void main(String[] args) throws JSchException, SftpException, IOException {
		JschClient client = new JschClient("acd", "ccodacd", "10.130.24.133",
				60022);
		client.openSession();
		ChannelSftp  channel = client.openChannelSftp();
		channel.connect();
//		channel.cd("/ACD");
//		channel.cd("/ACD/dwef");
//		channel.stat(path);
		channel.cd("/ACD/20150521");
		Vector<ChannelSftp.LsEntry> v = channel.ls(".");
		
		for(ChannelSftp.LsEntry oListItem : v){ 
			if (!oListItem.getAttrs().isDir()) {
				System.out.println(oListItem.getFilename());
				InputStream in = channel.get(oListItem.getFilename());
				byte b[] = new byte[1024];   
		        int len = 0;   
		        int temp=0;          //所有读取的内容都使用temp接收   
		        while((temp=in.read())!=-1){    //当没有读取完时，继续读取   
		            b[len]=(byte)temp;   
		            len++;   
		        }   
		        in.close();   
		        System.out.println(new String(b,0,len));   
			}
		} 

//		for (int i = 0; i < 100; i++) {
//			List<String> list = new ArrayList<String>();
//			list.add("rm /tmp/3330_21_0000000093_ENT_RECORD_BX_TABLE.unl.tmp");
//			client.exeShellCommand(list);
////			client.exeShellCommand("rm /tmp/3330_21_0000000093_ENT_RECORD_BX_TABLE.unl.tmp");
//		}
		
		client.closeSession();
	}


	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return "qctest";
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			return null;
		}
	}


	public ChannelSftp getChannel() {
		return channel;
	}

	public void afterPropertiesSet() throws Exception {
		logger.info("开始初始化session。。。。");
		openSession();
		channel = this.openChannelSftp();
		channel.connect();
		logger.info("结束初始化session。。。。");
	}

	public void destroy() throws Exception {
		this.closeSession();
	}
}
