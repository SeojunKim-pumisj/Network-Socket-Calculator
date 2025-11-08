import javax.swing.SwingUtilities;
import java.io.*;
import java.net.*;
import java.util.*;

public class CalculatorClient {
	private static Socket clientSocket = null;
	private static BufferedReader in = null;
	private static BufferedWriter out = null;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new CalculatorFrame();
		});
		
		IpConfig conf = new IpConfig();
		
		String serverIP = conf.getServerIP();
		int nPort = conf.getServerPort();
		
		// Create Socket for communication to server
		try {
			clientSocket = new Socket(serverIP, nPort);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); 
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static String Calculate(String body){
		String message;
		String result = "";
		
		if (clientSocket == null || clientSocket.isClosed()) {
            return "Disconnected";
        }
		
		try {
			// parsing for protocol
			String[] arr = body.split(" ");
			
			StringBuilder operands = new StringBuilder();
			String method = null;
			
			for(String element : arr) {
				switch (element) {
					case "+" : method = "ADD";
							   break;
					case "-" : method = "SUB";
					   		   break;
					case "÷" : method = "DIV";
					   	       break;
					case "×" : method = "MUL";
					   		   break;
					default: operands.append(" " + element);
				}
			}
			
			message = method + operands.toString();
			out.write(message + "\n");
			out.flush();
			
			result = responseHandler(in.readLine());
		} 
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}
	
	public static String Disconnect() {
		String result = "";
		try {
			if (out != null) {
                out.write("DISCONNECT\n");
                out.flush();
                
                result = in.readLine();
            }
            if (clientSocket != null) clientSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
            return result;
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			return "Fail to disconnect";
		}
	}
	
	public static String responseHandler(String response) {
		String[] arr = response.split(" ");
		String result = "";
		
		// Response protocol method path
		switch (arr[0]) {
			case "ERROR" : result = errorHandler(arr[1]);
					   break;
			case "ANSWER" : result = arr[1];
			   		   break;
		}
		
		return result;
	}
	
	public static String errorHandler(String errorCode) {
		switch (errorCode) {
		case "1" : return "Invalid number of argument";
		case "2" : return "Undefined method";
		case "3" : return "Divided by zero";
		case "4" : return "Null expression";
		case "5" : return "Invalid format of argument";
		default : return "Undefined error";
		}
	}
}
