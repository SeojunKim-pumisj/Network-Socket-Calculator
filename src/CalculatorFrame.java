import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorFrame extends JFrame{
	private JTextField expression; 
	
	public CalculatorFrame() {
		setLayout(null);
		setTitle("Calculator");
		setResizable(false);
		setSize(330, 570); // 330, 510
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		expression = new JTextField();
		expression.setEditable(false);
		expression.setBackground(Color.white);
		expression.setHorizontalAlignment(JTextField.RIGHT);
		expression.setFont(new Font("Arial", Font.BOLD, 50));
		expression.setBounds(8, 10, 300, 70);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
		buttonPanel.setBounds(8, 90, 300, 370);
		
		
		String buttonElements[] = { "C", " ", "+", "<--", "7", "8", "9", "÷", "4", "5", "6", "×", "1", "2", "3", "-", "+/-", "0", ".", "=" };
		JButton buttons[] = new JButton[buttonElements.length];
		ButtonFunction buttonFunction = new ButtonFunction();
		
		for(int i = 0; i < buttonElements.length; i++) {
			buttons[i] = new JButton(buttonElements[i]);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
			if(buttonElements[i].equalsIgnoreCase("=")) {
				buttons[i].setBackground(new Color(0,103,192));
				buttons[i].setForeground(Color.WHITE);
			}
			else if((i >= 4 && i <= 6) || (i >= 8 && i <= 10) || (i >= 12 && i <= 14)) {
				buttons[i].setBackground(new Color(255,255,255));
				buttons[i].setForeground(Color.BLACK);
			}
			else {
				buttons[i].setBackground(new Color(249,249,249));
				buttons[i].setForeground(Color.BLACK);
			}
			buttons[i].setBorderPainted(false);
			buttons[i].addActionListener(buttonFunction);
			buttonPanel.add(buttons[i]);
		}
		
		JButton disconnect = new JButton("DISCONNECT");
		disconnect.setFont(new Font("Arial", Font.BOLD, 20));
		disconnect.setBackground(new Color(249,249,249));
		disconnect.setForeground(Color.BLACK);
		disconnect.setBounds(8, 480, 300, 40);
		disconnect.setBorderPainted(false);
		disconnect.addActionListener(buttonFunction);
		
		add(expression);
		add(buttonPanel);
		add(disconnect);
		
		setVisible(true);
		
	}
	
	class ButtonFunction implements ActionListener{
		private boolean flag = true;
		private String currentNum = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String operation = e.getActionCommand();
			String text = expression.getText();
			
			// Control different input format
			if(operation.equals("C")) {
				currentNum = "";
				expression.setText("");
				flag = true;
			}
			else if(operation.equals(".")) {
				if(currentNum.contains(".")) return;
				
				currentNum += ".";
				expression.setText(text + ".");
			}
			else if(operation.equals("<--")) {
				if(text.isEmpty()) return;
				
				char lastChar = text.charAt(text.length() - 1);
				
				if(lastChar == ' ' && text.length() >= 3) {
					char prev = text.charAt(text.length() - 2);
					if(prev == '+' || prev == '-' || prev == '÷' || prev == '×') {
						expression.setText(text.substring(0, text.length() - 3));
						currentNum = expression.getText();
			            flag = true;
			            return;
					}
				}
				if (!currentNum.isEmpty()) {
			        currentNum = currentNum.substring(0, currentNum.length() - 1);
			    }

			    expression.setText(text.substring(0, text.length() - 1));
			}
			else if(operation.equals("+") || operation.equals("-") || operation.equals("÷") || operation.equals("×")){
				if(!flag) return;
				if(currentNum.equals("")) return;
				
				expression.setText(text + " " + operation + " ");
				flag = false;
				currentNum = "";
			}
			else if(operation.equals("+/-")) {
				if(text.isEmpty()) {
					currentNum += "-";
					expression.setText("-");
				}
				else if(text.charAt(text.length() - 1) == ' ') {
					currentNum += "-";
					expression.setText(text + "-");
				}
				else if(text.charAt(text.length() - 1) == '-') {
					currentNum = currentNum.substring(0, currentNum.length() - 1);
					expression.setText(text.substring(0, text.length() - 1));
				}
			}
			else if(operation.equals("=")) {
				if(flag) return;
				
				String body = text;
				
				String result = CalculatorClient.Calculate(body);
				
				expression.setText(result);
				flag = true;
				currentNum = result;
			}
			else if(operation.equals("DISCONNECT")) {
				String result = CalculatorClient.Disconnect();
				
				expression.setText(result);
				flag = true;
				currentNum = result;
			}
			else {
				currentNum += operation;
				expression.setText(text + operation);
			}
		}
	}
}
