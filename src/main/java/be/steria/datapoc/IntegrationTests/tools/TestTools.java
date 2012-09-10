package be.steria.datapoc.IntegrationTests.tools;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.activemq.broker.BrokerService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import be.steria.datapoc.client.PersonService;
import be.steria.datapoc.client.PersonServiceService;
import be.steria.datapoc.client.QueryPersons;
import be.steria.datapoc.client.QueryPersonsService;
import be.steria.datapoc.services.NodeControler;
import be.steria.datapoc.services.NodeInfoService;



public class TestTools {
	
	public static void main(String [] args) {
		try {
			QueryPersons qryPersons = getQueryPersonsServiceClient(8080);
			System.out.println("Size: " + qryPersons.getAllPersons().size());
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	
	public static PersonService getPersonServiceClient(int port) throws Exception {
		
		PersonServiceService personServiceService = new PersonServiceService(
				new URL("http://localhost:" + port + "/PersonApp/services/PersonService?wsdl"), 
				new QName("http://datapoc.steria.be/services", "PersonServiceService")); 
		
		return personServiceService.getPersonServicePort();
		
	}
	
	public static QueryPersons getQueryPersonsServiceClient(int port) throws Exception {
		
		QueryPersonsService queryPersonsService = new QueryPersonsService(
				new URL("http://localhost:" + port + "/PersonApp/services/QueryPersons?wsdl"),
				new QName("http://services.datapoc.steria.be/", "QueryPersonsService"));
		
		return queryPersonsService.getQueryPersonsPort();
	}
	
	
	public static void configureNodeInfo(int startPort, int nodeQuantity, int centralNode) throws Exception{
		JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
        clientFactory.setAddress("http://localhost:" + (startPort+centralNode) + "/NodeInformation/services/NodeInformation");
		clientFactory.setServiceClass(NodeInfoService.class);
		NodeInfoService nodeInfoService = (NodeInfoService) clientFactory.create();
		
		List<String> addresses = new ArrayList<String>();
		for (int i=0; i<nodeQuantity; i++)
			addresses.add("http://localhost:" + (startPort+i) + "/PersonApp/services/PersonService" );
		
		nodeInfoService.configure(centralNode, addresses);
		
	}
	
	public static void setNodeId(int port, int nodeId) {
		JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
        clientFactory.setAddress("http://localhost:" + port + "/PersonApp/services/NodeControler");
		clientFactory.setServiceClass(NodeControler.class);
		NodeControler nodeControler = (NodeControler) clientFactory.create();
		nodeControler.setCurrentNodeId(nodeId);
	}
	
	public static void startCamelRoutes(int port) throws Exception {
		JaxWsProxyFactoryBean clientFactory = new JaxWsProxyFactoryBean();
        clientFactory.setAddress("http://localhost:" + port + "/PersonApp/services/NodeControler");
		clientFactory.setServiceClass(NodeControler.class);
		NodeControler nodeControler = (NodeControler) clientFactory.create();
		nodeControler.startCamel();
	}
	
	
	public static BrokerService startActiveMQ() throws Exception{
		BrokerService broker = new BrokerService();
        // configure the broker
        broker.addConnector("tcp://localhost:61616");
        broker.setBrokerName("testBroker");
        broker.setUseJmx(false);
        broker.start();
        return broker;
	}
	
	public static Server startJettyServer(int port, String[] warFiles, 
			String[] contextPaths) throws Exception {
		
		Server server = new Server(port);
		Handler [] handlers = new Handler[warFiles.length];
		WebAppContext webAppContext = null;
		
		for (int i=0; i<warFiles.length; i++) {
			webAppContext = new WebAppContext();
			webAppContext.setWar(warFiles[i]);
			webAppContext.setContextPath(contextPaths[i]);
			handlers[i] = webAppContext;
		}
		
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(handlers);
		server.setHandler(contexts);
		server.start();
		
		return server;
	}
}
