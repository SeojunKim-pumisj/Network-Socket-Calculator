import java.io.*;
import java.util.*;

public class IpConfig {
	private String serverIP = "localhost";
	private int serverPort = 12025;
	
	public IpConfig() {
		String fileName = "config.dat";
		File file = new File(fileName);
		PrintWriter pr = null;
		Scanner sc = null;
		
		if(!file.exists()) {
			try {
				pr = new PrintWriter(new FileWriter(fileName));
				pr.println(serverIP);
				pr.println(serverPort);
				System.out.println("Success to create default IpConfig file");
			} 
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			finally {
				if(pr != null) {
					pr.close();
				}
			}
		}
		
		try {
			sc = new Scanner(file);
			serverIP = sc.nextLine();
			serverPort = Integer.parseInt(sc.nextLine());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(pr != null) {
				sc.close();
			}
		}
	}
	
	public String getServerIP() {
		return this.serverIP;
	}
	
	public int getServerPort() {
		return this.serverPort;
	}
	
}
