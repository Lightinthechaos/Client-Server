
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import javax.swing.*;
//import javax.swing.JTextField;



public class Client extends JFrame{
	private String ip = "127.0.0.1";
	private String post1 = "POST 2 3 10 20 white Meeting next Wednesday from 2 to 3";
	private String post2 = "POST 6 6 5 5 red Pick up Fred from home at 5";
	private String get1 = "GET color=white";
	private String get2 = "GET contains= 4 6";
	private String get3 = "GET contains= 4 6 refersTo=Fred";
	private JTextField fieldName1;
	private JTextField fieldName2;
	private JTextField fieldName3;
	private JButton botton1;
	private JButton botton2;
	private JButton botton3;
	private JButton botton4;
	private JButton botton5;
	private JButton botton6;
	private JButton botton7;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	
	public Socket socket = null;
	static BufferedReader in = null;
    static PrintWriter out = null;
    
    public String[] color1;
 
   // public boolean flag = false;
	
	
	public Client() {
		createview();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(720,410);
		setTitle("A simple JFrame");
		setLocationRelativeTo(null);
		//setResizable(false);
		
	}
	private void createview() {
		
		JPanel panel1 = new JPanel();
		
		panel1.setLayout(new GridLayout(2,0));
		getContentPane().add(panel1);
		JPanel panel2 = new JPanel();
		
		panel2.setLayout(new GridLayout(6,1,13,10));
		panel2.setBackground(Color.red);
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.setBackground(Color.LIGHT_GRAY);
		panel1.add(panel2);
		panel1.add(panel3);
		
		JLabel label1 = new JLabel("IP： ");
		panel2.add(label1);
		
		fieldName1 = new JTextField();
		
		fieldName1.setPreferredSize(new Dimension(150,30));
		panel2.add(fieldName1);
		
		JLabel label2 = new JLabel("Port: ");
		panel2.add(label2);
		
		fieldName2 = new JTextField();
		fieldName2.setBackground(Color.cyan);;
		fieldName2.setPreferredSize(new Dimension(150,30));
		panel2.add(fieldName2);
		
		botton1 = new JButton("Connect");
		botton1.setPreferredSize(new Dimension(230,100));
		botton1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int port = Integer.parseInt((fieldName2.getText()));
			String ip = fieldName1.getText();
			
	        try {
				socket = new Socket(ip, port);
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out = new PrintWriter(socket.getOutputStream(),true);
	            String all_msg = in.readLine();
	            String[] msg = all_msg.split(":");         
	            color1 = msg[msg.length - 1].split(" ");          
	            JOptionPane.showMessageDialog(null, all_msg);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		});
		panel2.add(botton1);
		
		botton2 = new JButton("Disconnect");
		botton2.setPreferredSize(new Dimension(230, 100));
		botton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket.close();
					JOptionPane.showMessageDialog(null, "Disconnect successfully");
				}catch(Exception fg){
					System.err.println("disconect error");
				}
			}
		});
		panel2.add(botton2);
		
		JLabel label = new JLabel("Enter: ");
		panel2.add(label);
		
		fieldName3 = new JTextField();
		fieldName3.setPreferredSize(new Dimension(200,30));
		panel2.add(fieldName3);
		
		botton3 = new JButton("POST");
		botton3.setPreferredSize(new Dimension(150,30));
		botton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = fieldName3.getText();
				String[] split = name.split(" ");
							
				boolean flag = false;
				
				if (split.length >= 7) {
					for (int j=0; j<color1.length;j++) {
						if (split[5].matches(color1[j].toLowerCase()) == true) {//you ke neng you cuo
							flag = true;
							break;
						}
					}
					if (split[0].equals("POST") == true && Character.isDigit(split[1].charAt(0)) == true
							&& Character.isDigit(split[2].charAt(0)) == true && Character.isDigit(split[3].charAt(0)) == true
							&& Character.isDigit(split[4].charAt(0)) && flag == true){
						out.println(name);
						try {
							JOptionPane.showMessageDialog(null, in.readLine());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//input correct, server use
					}
					else {
						JOptionPane.showMessageDialog(null, "Please enter valid input1");
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Please enter valid input2");
				}
				
			}
		});
		panel2.add(botton3);
		
		botton4 = new JButton("GET");
		botton4.setPreferredSize(new Dimension(150,30));
		botton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "DO you want to get pin(Yes) or other request(No)", "clear", JOptionPane.YES_NO_OPTION);
				if (a == 0) {
					out.println("GET_PIN");
					try {
						textArea.setText(in.readLine());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					String c = JOptionPane.showInputDialog(null, "Enter your request");
					if (c == null) {
						//JOptionPane.showMessageDialog(null, "Please enter valid input6");
					}
					else {
						String[] split1 = c.split("=");
						String first_reque = "GET color";
						String sec_reque = "GET contains";
						String thir_reque = "GET refersTo";
						String sec_reque_refer = "refersTo";
						//String thi_reque = "Get color";		
						if(c.contains("=") == false) {
							JOptionPane.showMessageDialog(null, "Please enter valid input");
						}
						else {
							
							if (split1.length == 2) {
								if (split1[0].equals(first_reque)) {
									boolean flag = false;
									
									for (int color_index = 0;color_index < color1.length; color_index++) {
										if (split1[1].equals(color1[color_index].toLowerCase())) {
											flag = true;
											break;
										}
									}
									
									if (flag == true) {
										
										out.println(c);
										try {
											
											textArea.setText(in.readLine());
											
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									else {
										JOptionPane.showMessageDialog(null, "Please enter valid input");
									}
									
									
								}
								else if(split1[0].equals(sec_reque)) {
									
									String[] split22 = split1[1].split(" ");
									if (split22.length == 3 && Character.isDigit(split22[1].charAt(0)) == true && 
											Character.isDigit(split22[2].charAt(0)) == true) {
										
										out.println(c);
										
										try {
											
											textArea.setText(in.readLine());
											
											
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									else {
										JOptionPane.showMessageDialog(null, "Please enter valid input");
									}
								}
								else if (split1[0].equals(thir_reque)) {
									out.println(c);
									try {
										textArea.setText(in.readLine());
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								else {
									JOptionPane.showMessageDialog(null, "Please enter valid input");
								}
							}
							else if(split1.length == 3) {
								String[] split_1 = split1[1].split(" ");
								
								if (split_1.length == 2 && split1[0].equals(first_reque)) {
									boolean flag = false;
									for (int color_index = 0;color_index<color1.length;color_index++) {
										if (split_1[0].matches(color1[color_index].toLowerCase())) {
											
											flag = true;
											break;
										}
									}
									String[] split22 = split1[2].split(" ");
	
									if (split_1[1].equals("contains") && Character.isDigit(split22[1].charAt(0)) == true && 
											Character.isDigit(split22[2].charAt(0)) == true && flag == true) {
										// server use
										out.println(c);
										try {
											textArea.setText(in.readLine());
											
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									else if (split_1[1].equals("referesTo") && flag == true) {
										//server use
										out.println(c);
										try {
											textArea.setText(in.readLine());
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									else {
										JOptionPane.showMessageDialog(null, "Please enter valid input");
									}
								}
								else if(split1[0].equals(sec_reque) && split_1.length == 4 && Character.isDigit(split_1[1].charAt(0)) == true && 
										Character.isDigit(split_1[2].charAt(0)) == true && split_1[3].equals(sec_reque_refer)) {
									//server use
									out.println(c);
									try {
										textArea.setText(in.readLine());
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}	
								else {
									JOptionPane.showMessageDialog(null, "Please enter valid input");
								}
							}
							else if(split1.length == 4) {
								String[] sec_part = split1[1].split(" ");
								String[] third_part = split1[2].split(" ");
								boolean flag = false;
								for (int color_index = 0;color_index<color1.length;color_index++) {
									if (sec_part[0].matches(color1[color_index].toLowerCase())) {
										flag = true;
										break;
									}
								}

								if(split1[0].equals(first_reque) && flag == true && sec_part.length == 2 && sec_part[1].equals("contains") &&
										third_part.length == 4 && Character.isDigit(third_part[1].charAt(0)) == true && 
										Character.isDigit(third_part[2].charAt(0)) == true && third_part[3].equals(sec_reque_refer)){
									//server use
									out.println(c);
									try {
										textArea.setText(in.readLine());
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
	
								}
								else {
									JOptionPane.showMessageDialog(null, "Please enter valid input");
								}
							}
							else {
								JOptionPane.showMessageDialog(null, "Please enter valid input");
							}												
						}
					}				
					//textArea.setText(c);
				}
			}
		});
		panel2.add(botton4);
		
		botton5 = new JButton("PIN");
		botton5.setPreferredSize(new Dimension(150,30));
		botton5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String b = JOptionPane.showInputDialog(null, "Enter dimension that you want to PIN");
				//String[] nums = b.split(" ");
				if(b == null) {
					//JOptionPane.showMessageDialog(null, "Please enter valid coordinates(less than 1000)");
				}
				else {
					String[] nums = b.split(",");
					if (nums.length < 2 || Character.isDigit(nums[0].charAt(0)) == false || Character.isDigit(nums[0].charAt(0)) == false
							||Integer.parseInt(nums[0]) > 1000 || Integer.parseInt(nums[1]) >1000) {
						JOptionPane.showMessageDialog(null, "Please enter valid coordinates(less than 1000)");
					}
					else {
						out.println("PIN " + b);
						try {
							JOptionPane.showMessageDialog(null, in.readLine());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		panel2.add(botton5);
		
		botton6 = new JButton("UNPIN");
		botton6.setPreferredSize(new Dimension(150,30));
		botton6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String l = JOptionPane.showInputDialog(null, "Enter dimension that you want to UNPIN");
				if(l == null) {
					//JOptionPane.showMessageDialog(null, "Input can not be empty");
				}
				else {
					String[] numu = l.split(",");
					if (numu.length < 2 || Character.isDigit(numu[0].charAt(0)) == false || Character.isDigit(numu[0].charAt(0)) == false
							||Integer.parseInt(numu[0]) > 1000 || Integer.parseInt(numu[1]) >1000) {
						JOptionPane.showMessageDialog(null, "Please enter valid coordinates(less than 1000)");
					}
					else {
						//server use
						out.println("UNPIN " + l);
						try {
							JOptionPane.showMessageDialog(null, in.readLine());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		panel2.add(botton6);
		
		botton7 = new JButton("CLEAR");
		botton7.setPreferredSize(new Dimension(150,30));
		botton7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("CLEAR");
				try {
					JOptionPane.showMessageDialog(null, in.readLine());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel3.add(botton7, BorderLayout.WEST);
		
		
		textArea = new JTextArea(10,44);
		scrollPane = new JScrollPane(textArea, scrollPane.VERTICAL_SCROLLBAR_ALWAYS, scrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPane.setSize(100,100);
		textArea.setEditable(false);
		panel3.add(scrollPane, BorderLayout.CENTER);
		
	}
	public static void main(String[] args) {
		//String serverAddress = "129.97.7.120";//new Scanner(System.in).nextLine();
        //Socket socket = new Socket(serverAddress, 9898);
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					new Client().setVisible(true);
				}
			}
		);
	}
}
