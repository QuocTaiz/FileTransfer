package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import entity.DataFile;

import javax.swing.JTextArea;


public class ClientFrame extends JFrame implements ActionListener{
	JTextField txtSearch;
	private static JTextArea textArea;
	JButton connectButton, disconnectButton, searchButton, downloadFileButton, uploadFileButton, deleteFileButton;
	public static JList<String> list;
	
	public static Socket socket;
	private static String host;
	private static int port;
	boolean isReceiving = false;
	
	public ClientFrame(String host , int port) throws Exception {
		this.host = host;
		this.port = port;
		try {
			
			JLabel searchLabel = new JLabel("Search: ");
			txtSearch = new JTextField();
			searchButton = new JButton();
			searchLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			searchLabel.setBounds(50, 30, 150, 25);
			txtSearch.setBounds(110, 30, 200, 25);
			searchButton.setBounds(290, 15, 130, 50);
	
			getContentPane().add(searchButton);
			getContentPane().add(txtSearch);
			getContentPane().add(searchLabel);
			
			
			JLabel disconnectLabel = new JLabel("Disconnect:");
			disconnectLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			disconnectLabel.setBounds(650, 80, 90, 20);
			disconnectButton = new JButton();
			disconnectButton.setBounds(680, 120, 50, 50);
			getContentPane().add(disconnectLabel);
			getContentPane().add(disconnectButton);
			JScrollPane listScrollPane = new JScrollPane();
			listScrollPane.setBounds(29, 120, 342, 410);
			getContentPane().add(listScrollPane);
			
					
			list = new JList<>();
			listScrollPane.setViewportView(list);
	
			JButton img = new JButton();
			img.setBounds(415, 20, 200, 200);
			getContentPane().add(img);
			
			downloadFileButton = new JButton();
			downloadFileButton.setBounds(80, 60, 50, 50);
	
			getContentPane().add(downloadFileButton);
	
			uploadFileButton = new JButton();
			uploadFileButton.setBounds(180, 60, 50, 50);
	
			getContentPane().add(uploadFileButton);
	
			deleteFileButton = new JButton();
			deleteFileButton.setBounds(280, 60, 50, 50);
			
			getContentPane().add(deleteFileButton);
	
			// Add event
			searchButton.addActionListener(this);
			downloadFileButton.addActionListener(this);
			uploadFileButton.addActionListener(this);
			deleteFileButton.addActionListener(this);
			disconnectButton.addActionListener(this);
	
			setupIcon(downloadFileButton, "download");
			setupIcon(searchButton, "search");
			setupIcon(uploadFileButton, "up");
			setupIcon(disconnectButton, "disconnect");
			setupIcon(img, "img");
			setupIcon(deleteFileButton, "delete");
			// setting Frame
	
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("Client Frame");
			this.setLocationRelativeTo(null);
			getContentPane().setLayout(null);
			this.setBounds(400, 100, 780, 600);
					
			JScrollPane listScrollPane_1 = new JScrollPane();
			listScrollPane_1.setBounds(381, 274, 342, 256);
			getContentPane().add(listScrollPane_1);
			
			textArea = new JTextArea();
			textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
			listScrollPane_1.setViewportView(textArea);
			
			JLabel LogLabel = new JLabel("Logs");
			LogLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			LogLabel.setBounds(391, 239, 45, 25);
			getContentPane().add(LogLabel);
			this.setVisible(true);
			

			
		} catch (Exception e) {
			e.printStackTrace();
			showMessageDialog("Ket noi voi server that bai", JOptionPane.PLAIN_MESSAGE);
		}
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		 if (e.getSource() == disconnectButton) {
			try {
				socket.close();
				this.dispose();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} else if (e.getSource() == searchButton) {  //search button
			try {
				socket = new Socket(host, port);
				String keyword = txtSearch.getText();
				String mess = "SEARCH_FILE--"+keyword;
				//send
				SendObjectClient soc = new SendObjectClient(socket, mess);
				soc.start();
				//receive
				ReceiveObjectClient roc = new ReceiveObjectClient(socket);
				roc.start();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} else if (e.getSource() == downloadFileButton) {   //download button
			try {
				if (list.getSelectedIndex() != -1) {
					String fileName = list.getSelectedValue();
					System.out.println("Da chon file "+fileName);
					String mess = "DOWNLOAD--"+fileName;
					//send
					SendObjectClient soc = new SendObjectClient(socket, mess);
					soc.start();
					//receive
					ReceiveObjectClient roc = new ReceiveObjectClient(socket);
					roc.start();
				} else {
					showMessageDialog("Vui long chon file de download", JOptionPane.PLAIN_MESSAGE);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if (e.getSource() == uploadFileButton) {   //upload button
			try {
				socket = new Socket(host, port);
				JFileChooser jfc = new JFileChooser(ClientMain.getPathSave());
				jfc.showOpenDialog(this);
				File fileUpload = jfc.getSelectedFile();
				DataFile dataFileUpload = new DataFile(fileUpload.getName(), ClientMain.getPathSave());
				//send
				SendObjectClient soc = new SendObjectClient(socket, dataFileUpload);
				soc.start();
				//receive
				ReceiveObjectClient roc = new ReceiveObjectClient(socket);
				roc.start();
			} catch (Exception e2) {
				e2.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == deleteFileButton) {   // delete button
			try {
				if (list.getSelectedIndex() != -1) {
					String fileName = list.getSelectedValue();
					String mess = "DELETE--"+fileName;
					SendObjectClient soc = new SendObjectClient(socket, mess);
					soc.start();
				} else {
					showMessageDialog("Vui long chon file de download", JOptionPane.PLAIN_MESSAGE);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private void setupIcon(JButton button, String img) {
		try {
			Image icon = ImageIO.read(getClass().getResource("/img/" + img + ".png"));
			ImageIcon imageIcon = new ImageIcon(icon);
			button.setIcon(imageIcon);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public void showMessageDialog(String mess, int type) {
		JOptionPane.showMessageDialog(this, mess, "Thong bao", type);
	}
	
	public static void appendLog(String mess) {
		textArea.append(mess+"\n");
	}
}

class ReceiveObjectClient extends Thread{
	private Socket socket;
	private ObjectInputStream ois;
	
	public ReceiveObjectClient(Socket socket){
		try {
			this.socket = socket;
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		try {
			
				System.out.println("Receiving...");
				Object obj = ois.readObject();
				if (obj instanceof String) {
					String mess = obj.toString();
					ClientFrame.appendLog(mess);
				} else if (obj instanceof DataFile) {
					System.out.println("Nhan duoc DataFile");
					DataFile dataFile = (DataFile)obj;
					saveDataFile(dataFile);
				} else {
					String[] listFile = (String[])obj;
					ClientFrame.list.setListData(listFile);
					System.out.println("Nhan duoc list file" + listFile.toString());
				}
			
		} catch (Exception e) {
			try {
				socket.close();
				ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	public void saveDataFile(DataFile dataFile) {
		if (!checkFileExist(dataFile.fileName)) {
			dataFile.saveFile(ClientMain.getPathSave());
		} else {
			ClientFrame.appendLog("File da ton tai, khong the download!");
		}
	}
	
	public boolean checkFileExist(String fileName) {
		return new File(ClientMain.getPathSave()+"\\"+fileName).exists();
	}
}

class SendObjectClient extends Thread{
	private Socket socket;
	private Object obj;
	private ObjectOutputStream oos;
	
	public SendObjectClient(Socket socket, Object obj) throws Exception {
		this.socket = socket;
		this.obj = obj;
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		try {
			oos.writeObject(obj);
			ClientFrame.appendLog("Client da gui obj");
		} catch (Exception e) {
			try {
				socket.close();
				oos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
