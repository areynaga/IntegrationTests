package be.steria.datapoc.IntegrationTests.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class StartScenario {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			int nodeQuantity = Integer.parseInt(args[0]);
			int centralNodeId = Integer.parseInt(args[1]);
			int startPort = Integer.parseInt(args[2]);
			
			TestScenario testScenario =  new TestScenario(nodeQuantity, 
					centralNodeId, startPort);
			Thread monitor = new MonitorThread(testScenario);
			monitor.start();
			
			testScenario.mountScenario();
			
			testScenario.getJettyServers().get(nodeQuantity-1).join();
			
			testScenario.getActiveMQServer().stop();
			testScenario.deleteTestFolders();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		

	}
	
	
	private static class MonitorThread extends Thread {

        private ServerSocket socket;
        private TestScenario _testScenario;

        public MonitorThread(TestScenario testScenario) {
        	this._testScenario = testScenario;
            setDaemon(true);
            setName("StopMonitor");
            try {
                socket = new ServerSocket(testScenario.getStartPort()-1, 1, InetAddress.getByName("127.0.0.1"));
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            System.out.println("*** running  'stop' thread");
            Socket accept;
            try {
                accept = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                reader.readLine();
                System.out.println("*** stopping test scenario");
                _testScenario.stopJettyServers();
                accept.close();
                socket.close();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
