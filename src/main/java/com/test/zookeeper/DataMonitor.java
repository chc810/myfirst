package com.test.zookeeper;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMonitor implements Watcher, StatCallback {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	ZooKeeper zk;

	String znode;

	Watcher chainedWatcher;

	boolean dead;

	DataMonitorListener listener;

	byte prevData[];

	public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
		this.zk = zk;
		this.znode = znode;
		this.chainedWatcher = chainedWatcher;
		this.listener = listener;
		// Get things started by checking if the node exists. We are going
		// to be completely event driven
		zk.exists(znode, true, this, null);
		zk.getChildren(znode, true, new Children2Callback() {
			
			@Override
			public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
				System.out.println("path =" + path);
				System.out.println("children= " + children);
			}
		}, null);
	}

	/**
	 * Other classes use the DataMonitor by implementing this method
	 */
	public interface DataMonitorListener {
		/**
		 * The existence status of the node has changed.
		 */
		void exists(byte data[]);

		/**
		 * The ZooKeeper session is no longer valid.
		 *
		 * @param rc
		 *            the ZooKeeper reason code
		 */
		void closing(int rc);
	}

	public void process(WatchedEvent event) {
		String path = event.getPath();
		if (event.getType() == Event.EventType.None) {
			// We are are being told that the state of the
			// connection has changed
			switch (event.getState()) {
			case SyncConnected:
				// In this particular example we don't need to do anything
				// here - watches are automatically re-registered with
				// server and any watches triggered while the client was
				// disconnected will be delivered (in order of course)
				break;
			case Expired:
				// It's all over
				dead = true;
				listener.closing(KeeperException.Code.SessionExpired);
				break;
			}
		} else if (event.getType() == Event.EventType.NodeChildrenChanged) {
			System.out.println("NodeChildrenChanged。。。。");
//				List<String> children = zk.getChildren(znode, true);
			zk.getChildren(znode, true, new Children2Callback() {
				@Override
				public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
					System.out.println("children = "+ children);
				}
			}, null);
//				System.out.println("children = "+ children);
			
		} else {
			if (path != null && path.equals(znode)) {
				// Something has changed on the node, let's find out
				zk.exists(znode, true, this, null);
			}
		}
		if (chainedWatcher != null) {
			chainedWatcher.process(event);
		}
	}

	public void processResult(int rc, String path, Object ctx, Stat stat) {
		logger.info("进入processResult....");
		boolean exists;
		switch (rc) {
		case Code.Ok:
			exists = true;
			break;
		case Code.NoNode:
			exists = false;
			break;
		case Code.SessionExpired:
		case Code.NoAuth:
			dead = true;
			listener.closing(rc);
			return;
		default:
			// Retry errors
			zk.exists(znode, true, this, null);
			return;
		}

		byte b[] = null;
		if (exists) {
			try {
				b = zk.getData(znode, false, null);
			} catch (KeeperException e) {
				// We don't need to worry about recovering now. The watch
				// callbacks will kick off any exception handling
				e.printStackTrace();
			} catch (InterruptedException e) {
				return;
			}
			
			try {
				System.out.println(new String(b,"utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if ((b == null && b != prevData) || (b != null && !Arrays.equals(prevData, b))) {
			listener.exists(b);
			prevData = b;
		}
	}
}
