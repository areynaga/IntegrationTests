package be.steria.datapoc.IntegrationTests.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.activemq.broker.BrokerService;
import org.apache.commons.io.FileUtils;

import org.eclipse.jetty.server.Server;

public class TestScenario {
		
	private int nodeQuantity;
	private int centralNodeId;
	private int startPort;
	private List<Server> jettyServers;
	private BrokerService activeMQServer;
	
	public TestScenario(int nodeQuantity, int centralNodeId, int startPort) {
		this.centralNodeId = centralNodeId;
		this.nodeQuantity = nodeQuantity;
		this.setStartPort(startPort);
		jettyServers = new ArrayList<Server>();
	}
	
	
	private void startServers() throws Exception {
		activeMQServer = TestTools.startActiveMQ();
		
		for (int i=0; i<nodeQuantity; i++) {
			if (centralNodeId == i) {
				jettyServers.add(TestTools.startJettyServer(startPort+i, 
						new String[] {"src/test/resources/NodeInformation.war",
									  "src/test/resources/Logger.war",
									  "src/test/resources/PersonApp.war"}, 
						new String[] {"/NodeInformation",
									  "/Logger",
									  "/PersonApp"}));
			} else {
				jettyServers.add(TestTools.startJettyServer(startPort+i, 
						new String[] {"src/test/resources/PersonApp.war"}, 
						new String[] {"/PersonApp"}));
			}
		}
	}
	
	private void configureServers() throws Exception{
		TestTools.configureNodeInfo(startPort, nodeQuantity, centralNodeId);
		
		for (int i=0; i<nodeQuantity; i++) {
			TestTools.setNodeId(startPort+i, i);
			TestTools.startCamelRoutes(startPort+i);
		}
		
	}
	
	public void mountScenario() throws Exception {
		startServers();

		
		configureServers();
		
		
	}
	
	
	public int getServerPort(int serverId) {
		return serverId + startPort;
	}
	
	public  void deleteTestFolders() throws IOException {
		FileUtils.deleteDirectory(new File("./activemq-data"));
		FileUtils.deleteDirectory(new File("./temp"));
		System.out.println("Temp folders deleted");
	}
	
	public void stopJettyServers() throws Exception {
		for (Server server : jettyServers) {
			server.stop();
			server.destroy();
		}
	}
	
	
	public void unMountScenario() throws Exception {
		
		stopJettyServers();
		
		activeMQServer.stop();
		deleteTestFolders();
			
	}

	public int getNodeQuantity() {
		return nodeQuantity;
	}

	public void setNodeQuantity(int nodeQuantity) {
		this.nodeQuantity = nodeQuantity;
	}

	public int getCentralNodeId() {
		return centralNodeId;
	}

	public void setCentralNodeId(int centralNodeId) {
		this.centralNodeId = centralNodeId;
	}

	public List<Server> getJettyServers() {
		return jettyServers;
	}

	public void setJettyServers(List<Server> jettyServers) {
		this.jettyServers = jettyServers;
	}

	public BrokerService getActiveMQServer() {
		return activeMQServer;
	}

	public void setActiveMQServer(BrokerService activeMQServer) {
		this.activeMQServer = activeMQServer;
	}

	public int getStartPort() {
		return startPort;
	}

	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}
	
}
