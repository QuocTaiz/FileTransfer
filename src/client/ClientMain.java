package client;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class ClientMain extends JFrame implements ActionListener{
	JTextField txtIP, txtPort;
	JButton connectButton, closeButton;
	public static String pathSave = "D:\\LocalServerFilePBL4Client";

	public static void main(String[] args) {
		ClientMain frame = new ClientMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);		
	}

	public ClientMain() {

		JLabel ipLabel = new JLabel("IP: ");
		txtIP = new JTextField("127.0.0.1");

		ipLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ipLabel.setBounds(50, 30, 150, 25);
		txtIP.setBounds(100, 30, 200, 25);

		this.add(ipLabel);
		this.add(txtIP);
		JLabel portLabel = new JLabel("PORT: ");
		txtPort = new JTextField("2208");

		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		portLabel.setBounds(50, 80, 150, 25);
		txtPort.setBounds(100, 80, 200, 25);

		this.add(portLabel);
		this.add(txtPort);
		
		
		connectButton = new JButton("Connect");
		closeButton = new JButton("Exit");
		connectButton.setBounds(85, 130, 85, 25);
		closeButton.setBounds(200,130, 85, 25);
		
	
		this.add(connectButton);
		this.add(closeButton);

		connectButton.addActionListener(this);
		closeButton.addActionListener(this);
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Client Frame");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setBounds(600, 280, 380, 220);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (e.getSource() == connectButton) {
			String host = txtIP.getText();
			int port = Integer.parseInt(txtPort.getText());
			try {
				this.dispose();
				ClientFrame clientFrame = new ClientFrame(host, port);
				clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				clientFrame.setVisible(true);
			} catch (Exception e2) {
				showMessageDialog("Ket noi voi server that bai", JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
			}
			
		} else if (e.getSource()==closeButton) {
			this.dispose();
		}
	}
	
	public static String getPathSave() {
		return pathSave;
	}
	
	public void showMessageDialog(String mess, int type) {
		JOptionPane.showMessageDialog(null, mess, "Thong bao", type);
	}

}
