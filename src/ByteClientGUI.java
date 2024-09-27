import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ByteClientGUI extends JFrame {
	private JTextField t_input;
	private JTextArea t_display;
	private JButton b_connect;
	private JButton b_disconnect;
	private JButton b_send;
	private JButton b_exit;
	private String serverAddress;
	private int serverPort;
	private Socket socket;
	private OutputStream out;
	
	public ByteClientGUI(String s, int i) {
		super();
		this.serverAddress=s;
		this.serverPort=i;
		this.setTitle("ByteClientGUI");
		this.setBounds(200,300,700,500);
		this.setLayout(new BorderLayout());
		
		buildGUI();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}	
	
	private void buildGUI() {
		JPanel displayPanel,inputPanel,controlPanel,bottomPanel;
		displayPanel=createDisplayPanel();
		inputPanel=createInputPanel();
		controlPanel=createControlPanel();
		bottomPanel=new JPanel(new GridLayout(0,1));
		
		bottomPanel.add(inputPanel);
		bottomPanel.add(controlPanel);
		
		this.add(displayPanel,BorderLayout.CENTER);
		this.add(bottomPanel,BorderLayout.SOUTH);
		
		setDisplay_disconn();
	}
	
	private JPanel createDisplayPanel() {
		JPanel panel=new JPanel(new BorderLayout());
		t_display=new JTextArea();
		JScrollPane scroll=new JScrollPane(t_display);
		
		t_display.setEditable(false);
		
		panel.add(scroll);
		return panel;
	}
	
	private JPanel createInputPanel() {
		JPanel panel=new JPanel(new BorderLayout());
		t_input=new JTextField();
		b_send=new JButton();
		
		b_send.setText("보내기");
		
		t_input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printDisplay("나: " + t_input.getText());
				sendMessage();
			}
		});
		
		b_send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				printDisplay("나: " + t_input.getText());
				sendMessage();				
			}
		});
		
		panel.add(t_input,BorderLayout.CENTER);
		panel.add(b_send,BorderLayout.EAST);
		return panel;
	}
	
	private JPanel createControlPanel() {
		JPanel panel=new JPanel(new GridLayout(1,0));
		b_connect=new JButton();
		b_disconnect=new JButton();
		b_exit=new JButton();
		
		b_connect.setText("접속하기");
		b_disconnect.setText("접속끊기");
		b_exit.setText("종료하기");
		
		b_connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				setDisplay_conn();
				connectToServer();
			}
			
		});
		
		b_disconnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setDisplay_disconn();
				disconnect();	
			}
			
		});
		
		b_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		panel.add(b_connect);
		panel.add(b_disconnect);
		panel.add(b_exit);
		return panel;
	}
	
	private void connectToServer() {
		try {
			socket = new Socket(serverAddress, serverPort);
			out = socket.getOutputStream();
		}catch(IOException e) {
			System.err.println("클라이언트 접속 오류> "+e.getMessage());
			System.exit(-1);
		}
	}
	
	private void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage() {
		
		try{
			String message = t_input.getText();
			int msg = Integer.parseInt(message);
			t_input.setText("");
			out.write(msg);
		}
		catch(NumberFormatException e) {
			printDisplay("정수를 입력하시오.");
			t_input.setText("");
		}
		catch(IOException e) {
			System.err.println("클라이언트 쓰기 오류> "+e.getMessage());
			System.exit(-1);
		}
		
	}
	
	private void printDisplay(String s) {
		t_display.append(s+'\n');
		t_display.setCaretPosition(t_display.getDocument().getLength());
	}
	
	private void setDisplay_conn() {
		b_connect.setEnabled(false);
		b_disconnect.setEnabled(true);
		b_exit.setEnabled(false);
		b_send.setEnabled(true);
		t_input.setEnabled(true);
		
	}
	private void setDisplay_disconn() {
		b_connect.setEnabled(true);
		b_disconnect.setEnabled(false);
		b_exit.setEnabled(true);
		b_send.setEnabled(false);
		t_input.setEnabled(false);		
	}
	
	public static void main(String[] args) {
		new ByteClientGUI("localhost",54321);
	}
}
