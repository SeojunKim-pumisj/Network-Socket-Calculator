import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CalculatorServer {

    public static void main(String[] args) {
        ServerSocket listener = null;
        Socket socket = null;
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        try {
            listener = new ServerSocket(12025);
            System.out.println("Connecting...");
            
            while(true) {
            	socket = listener.accept();
                System.out.println("Connected!");
                
                // Create new thread for multiple clients
                threadPool.execute(new threadHandler(socket));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (listener != null)
                    listener.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
        threadPool.shutdown();
    }
}

class threadHandler implements Runnable {
    private Socket socket;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    
    public threadHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String message = "";
            
            while (true) {
                String expression = in.readLine();
                System.out.println(expression);
                
                // If It is input error
                if(expression == null) {
                	out.write("ERROR 4\n");
                	out.flush();
                }
                
                if (expression.equals("DISCONNECT")) {
                    out.write("END\n");
                	System.out.println("Disconnected");
                    break;
                }
                
                String result = calc(expression);
                
                if(result.contains("ERROR")) {
                	out.write(result + "\n");
                	out.flush();
                }
                else {
                	message = "ANSWER " + result + "\n";
                	out.write(message);
                	out.flush();
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try { 
            	if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
            } 
            catch (IOException e) {
            	System.out.println(e.getMessage());
            }
        }
    }
    
    
    public static String formatResult(double value) {
        if (value == Math.floor(value)) {
            return Integer.toString((int) value);
        } else {
            return Double.toString(Math.round(value * 100000)/100000.0);
        }
    }
    
    public static String calc(String expression) {
        StringTokenizer st = new StringTokenizer (expression);
        String result = "";
        
        // If It is argument number error
        if (st.countTokens() != 3) return "ERROR 1";
        
        try {
        	String operator = st.nextToken();
            double op1 = Double.parseDouble(st.nextToken());
            double op2 = Double.parseDouble(st.nextToken());
            
            switch (operator) {
                case "ADD":
                	result = formatResult(op1 + op2);
                    break;
                case "SUB":
                	result = formatResult(op1 - op2);
                    break;
                case "MUL":
                	result = formatResult(op1 * op2);
                    break;
                case "DIV":
                	if(op2 == 0) {
                		result = "ERROR 3";
                		break;
                	}
                	result = formatResult(op1 / op2);
                    break;
                default:
                	result = "ERROR 2";
            }
        }
        catch (NumberFormatException e) {
        	return "ERROR 5";
        }
        
        return result;
    }
}

