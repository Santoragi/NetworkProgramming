import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ByteServerGUI extends JFrame{
	private int port;
	private ServerSocket serverSocket;
	private JTextArea t_display;
	
	public ByteServerGUI(int i) {
		super();
		this.port=i;
		this.setTitle("ByteServer GUI");
		this.setBounds(300,400,700,500);
		this.setLayout(new BorderLayout());
		
		buildGUI();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void buildGUI() {
		JPanel displayPanel,controlPanel,bottomPanel;
		displayPanel=createDisplayPanel();
		controlPanel=createControlPanel();
		
		bottomPanel=new JPanel(new GridLayout(0,1));
		bottomPanel.add(controlPanel);
				
		this.add(displayPanel,BorderLayout.CENTER);
		this.add(bottomPanel,BorderLayout.SOUTH);
		
	}
	
	private JPanel createDisplayPanel() {
		JPanel panel=new JPanel(new BorderLayout());
		t_display=new JTextArea();
		JScrollPane scroll=new JScrollPane(t_display);
		
		t_display.setEditable(false);
		
		panel.add(scroll,BorderLayout.CENTER);
		return panel;
	}
	
	private JPanel createControlPanel() {
		JPanel panel=new JPanel(new BorderLayout());
		JButton btn_exit=new JButton();
		
		btn_exit.setText("종료");
		
		btn_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					System.exit(0);
			}
		});
		
		panel.add(btn_exit,BorderLayout.CENTER);
		return panel;
	}
	
	private void startServer() {
		
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			printDisplay("서버가 시작되었습니다.");
			while (true) {
				clientSocket = serverSocket.accept();
				printDisplay("클라이언트가 연결되었습니다.");
				receiveMessages(clientSocket);
			}
		}catch(IOException e) {
			printDisplay("서버 오류> "+e.getMessage());
			System.exit(-1);
		}
		finally {
			try {
				if(clientSocket != null)clientSocket.close();
			}catch(IOException e) {
				printDisplay("서버 닫기 오류> "+e.getMessage());
				System.exit(-1);
			}	
	
		}
	}
	
	private void printDisplay(String s) {
		t_display.append(s+'\n');
		t_display.setCaretPosition(t_display.getDocument().getLength());
	}
	
	private void receiveMessages(Socket socket) {
		InputStream in;
		
		try {
			in=socket.getInputStream();
			
			int message;
			while ((message = in.read()) != -1) {
				printDisplay("클라이언트 메시지: " + message);
			}
	
			printDisplay("클라이언트가 연결을 종료했습니다.");
		}catch(IOException e) {
			System.err.println("서버 읽기 오류> "+e.getMessage());
			System.exit(-1);
		}
		finally {
			try {
				socket.close();
			}catch(IOException e) {
				System.err.println("서버 닫기 오류> "+ e.getMessage());
				System.exit(-1);
			}
		}
	}
	
	public static void main(String[] args) {
		int port = 54321;
		ByteServerGUI server=new ByteServerGUI(port);
		server.startServer();
	}
	
}
