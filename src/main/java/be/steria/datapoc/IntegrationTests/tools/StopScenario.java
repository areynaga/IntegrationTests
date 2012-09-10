package be.steria.datapoc.IntegrationTests.tools;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class StopScenario {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int startPort = Integer.parseInt(args[0]);
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), startPort-1);
        OutputStream out = s.getOutputStream();
        System.out.println("*** sending jetty stop request");
        out.write(("\r\n").getBytes());
        out.flush();
        s.close();
	}

}
