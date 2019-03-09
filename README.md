# Client


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
#Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


public class Server {
	private static int portnum,bdwidth,bdheight;
	public static String Color="";
    public static void main(String[] args) throws IOException {
        ServerSocket Server = null;
        
        try { 
            portnum = Integer.parseInt(args[0]); 
            bdwidth = Integer.parseInt(args[1]); 
            bdheight = Integer.parseInt(args[2]);
            String[] color_co = Arrays.copyOfRange(args, 3, args.length);
            for (int i = 0; i < color_co.length; i++) color_co[i] = color_co[i].toUpperCase();
            for (int i = 0; i < color_co.length; i++) Color= Color + color_co[i].toUpperCase() + " ";
        } catch (Exception e) {
            System.err.println("Invalid port, attempting default port 9999.\n Invalid color, attempting default color red");
            portnum = 9999;
            Color+="red";
        }
        try {
        	Server = new ServerSocket(portnum);
            System.out.println("Running on port " + Server.getLocalPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portnum + ".");
            try { 
            	Server = new ServerSocket(0);
                System.out.println("Listening on port " + Server.getLocalPort() + ".");
            } catch (IOException ioe) { 
                System.err.println("Fail to listen on this port.");
                System.exit(1);
            }
        }
        
        

        Trans p = new Trans();
        int clientNumber = 0;
        while (true) {
            try {
                try {
                    new Connection(Server.accept(),p).start();
                    System.out.println("Accepted new Client " + clientNumber++);
                } catch (IOException e) {
                    System.err.println("Accept failed");
                }
            } catch (Exception e) { 
            	Server.close();
                System.err.println("An error has occured");
                break;
            }
        }
        System.exit(0);
    }

    private static class Connection extends Thread{
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String inputLine, outputLine;
        private Trans protocol;



        public Connection(Socket s, Trans p) {
            this.clientSocket = s;
            this.protocol = p;
            try {
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
            } catch (IOException e) {
                System.err.println("Error creating connection to client.");
            }
        }

        public void run(){
        	
            int code = -1;
            out.println("Board width: " + Integer.toString(bdwidth) + " Board height: " + Integer.toString(bdheight) + " Available Color: " + Color);
            try {
                while ((inputLine = in.readLine()) != null || code != -1) {
                    try {
                    	
                        if (inputLine.startsWith("POST")) {
                            code = Trans.POST;
                        }else if (inputLine.startsWith("GET_PIN")) {
                            code = Trans.GET_PIN;
                        }else if (inputLine.startsWith("GET")) {
                            code = Trans.GET_COM;
                        }else if (inputLine.startsWith("PIN")) {
                            code = Trans.PIN;
                        }else if (inputLine.startsWith("UNPIN")) {
                            code = Trans.UPIN;
                        }else if (inputLine.startsWith("CLEAR")) {
                            code = Trans.CLEAR;
                        }else if (inputLine.startsWith("DISCONNECT")) {
                            code = Trans.DISCONNECT;
                        }
                        else {
                            code = Trans.FAULT;
                        }

                        outputLine = protocol.processInput(code, inputLine);
                        out.println(outputLine);

                    } catch (NullPointerException e) { 
                        System.err.println("Client was disconnected.");
                        clientSocket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Client was disconnected.");
            }
        }
    }
    
    static class Note{
        public String command,color,message;
        public int x,y,width,height,status;
        public int range_x,range_y;
        public String get_request(){
            return command;
        }
       

        public void set_left(int lowerleft){
            this.x = lowerleft;
        }

        public void set_right(int lowerrght){
            this.y = lowerrght;
        }

        public void set_width(int width){
            this.width = width;
        }

        public void set_height(int height){
            this.height = height;
        }

        public void set_color(String color){
            this.color = color;
        }

        public void set_message(String message){
            this.message = message;
        }
        
        public boolean hasnull() {
        	return command == null || color == null || message == null;
        }
        
        public void set_range() {
        	this.range_x = x + width;
        	this.range_y = y + height;
        }
    }
    
    static class Data{
    	private int x,y;
    	
    	public void set_data(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}	
    }
    

    
    static class Trans {
        public static final int POST = 0;
        public static final int GET_PIN = 1;
        public static final int GET_COM = 2;
        public static final int PIN = 3;
        public static final int UPIN = 4;
        public static final int CLEAR = 5;
        public static final int DISCONNECT = 6;
        public static final int FAULT = 7;
        public int notes_num = 0;

        public static final String[] requestkeyword = {"POST","GET","PIN","UNPIN","CLEAR","DISCONNECT"};
        public static final String[] notekword = {"POST","LOWERLEFT","LOWERRIGHT", "WIDTH", "HEIGHT", "COLOR","MESSAGE"};

        public ArrayList<Note> board = new ArrayList<Note>();  
        public ArrayList<Note> pined = new ArrayList<Note>();
        public ArrayList<Data> pin_address = new ArrayList<Data>();

        public static final String POST_SUCCESS = "Note post successfully";
        public static final String POST_FAILURE = "Note post successfully";
        public static final String GET_FAILURE = "Note get failed";
        public static final String PIN_SUCCESS = "Note pinned successfully";
        public static final String PIN_FAILURE = "Note pinned failed";
        public static final String UNPIN_SUCCESS = "Note unpinned successfully";
        public static final String UNPIN_FAILURE = "Note unpinned failed";
        public static final String CLEAR_SUCCESS = "Note cleared successfully";
        public static final String CLEAR_FAILURE = "Note cleared failed";
        public static final String DISCONNECT_SUCCESS = "Disconnet successfully";
        public static final String DISCONNECT_FAILURE = "Disconnet failed";
        public static final String DISPOSE_FAILURE = "Input disposed failed";


        public String processInput(int command, String input){
            String result = "";
            switch(command){
                case POST:
                    result = post(input);
                    break;
                case GET_PIN:
                    result = get_pin();
                    break;
                case GET_COM:
                    result = get_request(input);
                    break;
                case PIN:
                    result = pin(input);
                    break;
                case UPIN:
                    result = unpin(input);
                    break;
                case CLEAR:
                    result = clear();
                    break;
                case DISCONNECT:
                    result = disconnect();
                    break;
                default:
                    break;
            }
            return result;
        }
        
        private String post(String input) {
        	Note note = parse_note(input);
        	if (note == null || note.hasnull()) {
        		return POST_FAILURE;
        	}
        	else {
        		this.board.add(note);
        		notes_num+=1;
        		return POST_SUCCESS;
        		
        	}
        }
        
        private String get_pin() {
        	String temp = "";
        	int i = 1;
        	for (Data d: pin_address) {
        		temp = temp + "pin" + Integer.toString(i) + Integer.toString(d.x) + "," + Integer.toString(d.y) + "";
        		i++;
        	}
        	return temp;
        }
        
        private String get_request(String input) {
        	String[] tokens = input.split(" ");
        	if (tokens.length < 2) {
        		return GET_FAILURE;
        	}
        	else if(tokens.length == 2) {
        		String check_1 = tokens[1];
        		String check[] = check_1.split("=");
        		String key = check[0];
        		if (key.toLowerCase().equals("color")) {
        			String colored = Search_color(check[1]);
        			if(colored != null) {
        				return colored;
        			}
        			else {
        				return GET_FAILURE + "color";
        			}
        		}
        		else if(key.toLowerCase().equals("refersto")) {
        			String search = Search_message(check[1]);
        			if(search != null) {
        				return search;
        			}
        			else {
        				return GET_FAILURE + "message";
        			}
        		}
        		else return GET_FAILURE + "length == 2";
        	}
        	else if (tokens.length == 3){
        		String search = search_color_message(input);
    			if(search != null) {
    				return search;
    			}
    			else {
    				return GET_FAILURE + "search color + message";
    			}
        	}
        	else if (tokens.length == 4){
        		String search = search_coor(input);
    			if(search != null) {
    				return search;
    			}
    			else {
    				return GET_FAILURE + "search coor";
    			}
        	}
        	else if (tokens.length == 5) {
        		if(tokens[2].startsWith("GET color")){
            		String search = search_color_coor(input);
        			if(search != null) {
        				return search;
        			}
        			else {
        				return GET_FAILURE + "color+coor";
        			}
        		}
        		else{
        			String search = search_coor_message(input);
        			if(search != null) {
        				return search;
        			}
        			else {
        				return GET_FAILURE + "coor+message";
        			}
        		}

        	}
        	
        	else if (tokens.length == 6) {
        		String search = search_all(input);
    			if(search != null) {
    				return search;
    			}
    			else {
    				return GET_FAILURE + "color+coor";
    			}
        	}
        	else return GET_FAILURE + "search fail";
        }
        
        
        
        private String Search_message(String input) {
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.message.contains(input)){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;	
        		}
        		else {
        			return null;
        		}
        		i++;
        		
        	}
        	return temp;
        }
        
        private String Search_color(String input) {
    		int i = 1;
    		String temp_color = "";
    		String color = input.toUpperCase();
    		for(Note n : board) {
    			if (n.color.equals(color)) {
    				temp_color = temp_color + "Note" + Integer.toString(i) + ": " + "Message: " + n.message + "\n";
    			}
    			i++;
    		}
    		
        	if (temp_color.equals("")) {
        		return null;
        	}
        	else {
        	return temp_color;
        	}
        }
        
        private String search_color_message(String input) {
    		String sp[] = input.split(" ");
    		String coor_1 = sp[1];
    		String coor_2 = sp[2];
    		String subcoor_1[] = coor_1.split("=");
    		String subcoor_2[] = coor_2.split("=");
    		String key_1 = subcoor_1[1];
    		String key_2 = subcoor_2[1];
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.message.contains(key_2) && n.color.equals(key_1)){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;
        		}
        		i++;
        	}
        	if (temp.equals("")) {
        		return null;
        	}
        	else {
        	return temp;
        	}
        }
        
        private String search_coor(String input) {
    		String sp[] = input.split(" ");
    		int coor_2 = Integer.parseInt(sp[2]);
    		int coor_3 = Integer.parseInt(sp[3]);
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.x <= coor_2 && n.range_x >= coor_2 && n.y <= coor_3 && n.range_y >= coor_3){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;
        		}
        		i++;
        	}
        	if (temp.equals("")) {
        		return null;
        	}
        	else {
        	return temp;
        	}
        }
        
        private String search_coor_message(String input) {
    		String sp[] = input.split(" ");
    		int coor_1 = Integer.parseInt(sp[2]);
    		int coor_2 = Integer.parseInt(sp[3]);
    		String coor_3 = (sp[4]);
    		String subcoor_1[] = coor_3.split("=");
    		String key_1 = subcoor_1[1];
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.x <= coor_1 && n.range_x >= coor_1 && n.y <= coor_2 && n.range_y >= coor_2 && n.message.contains(key_1)){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;
        		}
        		i++;
        	}
        	if (temp.equals("")) {
        		return null;
        	}
        	else {
        	return temp;
        	}
        }
        
        
        private String search_color_coor(String input) {
    		String sp[] = input.split(" ");
    		String coor_1 = sp[1];
    		int coor_2 = Integer.parseInt(sp[3]);
    		int coor_3 = Integer.parseInt(sp[4]);
    		String subcoor_1[] = coor_1.split("=");
    		String key_1 = subcoor_1[1];
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.x<coor_2 && n.range_x> coor_2 && n.color.equals(key_1) && coor_3 > n.y && n.range_y > coor_3){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;
        		}
        		i++;
        	}
        	if (temp.equals("")) {
        		return null;
        	}
        	else {
        	return temp;
        	}
        }
        
        private String search_all(String input) {
    		String sp[] = input.split(" ");
    		String coor_1 = sp[1];
    		int coor_2 = Integer.parseInt(sp[3]);
    		int coor_3 = Integer.parseInt(sp[4]);
    		String coor_4 = sp[5];
    		
    		String subcoor_1[] = coor_1.split("=");
    		String subcoor_2[] = coor_4.split("=");
    		String key_1 = subcoor_1[1];
    		String key_2 = subcoor_2[1];
        	String temp = "";
        	int i=1;
        	for(Note n : board) {
        		if (n.x<coor_2 && n.range_x> coor_2 && n.color.equals(key_1) && coor_3 > n.y && n.range_y > coor_3 && n.message.contains(key_2)){
        			temp = temp + "Note" + Integer.toString(i) + ": " + n.message;
        		}
        		i++;
        	}
        	if (temp.equals("")) {
        		return null;
        	}
        	else {
        	return temp;
        	}
        }
        

        	
        private String pin(String input) {
        	if(board.isEmpty()) return PIN_FAILURE;
        	String[] key = input.split(" ");
        	Data d = new Data();
        	d.set_data(Integer.parseInt(key[1].split(",")[0]), Integer.parseInt(key[1].split(",")[1]));
        	for(Note n : board) {
        		if(n.x <= d.x && d.x <= n.range_x && n.y <= d.y && d.y <= n.range_y) {
        			pined.add(n);
        			n.status += 1;
        			pin_address.add(d);
        			
        		}
        	}
        	return PIN_SUCCESS;
        }
        
        private String unpin(String input) {
        	if(board.isEmpty()) return UNPIN_FAILURE + "Empty board";
        	if(pin_address.isEmpty()) return UNPIN_FAILURE + "There is no pin on board";
        	String[] key = input.split(" ");
        	Data e = new Data();
        	e.set_data(Integer.parseInt(key[1].split(",")[0]), Integer.parseInt(key[1].split(",")[1]));
        	for(Data n : pin_address) {
        		if(n.x == e.x &&  n.y == e.y) {
        			pin_address.remove(n);
        			return UNPIN_SUCCESS;
        		}
        	}
        	return UNPIN_FAILURE + "there is no pin here";
        	
        }
        
        
        private String clear() {
        	for(Note n : board) {
        		if(n.status == 0) {
        			board.remove(n);
                	return CLEAR_SUCCESS;
        		}
        	}
        	return CLEAR_FAILURE;
        }
        
        private String disconnect() {
        	try {
	        	board.clear();
	        	return DISCONNECT_SUCCESS;
        	}
        	catch(Exception e){
        		return DISCONNECT_FAILURE;
        	}
        }
        
        
        private Note parse_note(String in) {
        	
        	String[] keys = in.split(" ");
        	int j = keys.length;
        	if (j>5) {
	        	Note note = new Note();
	        	note.status = 0;
	        	note.command = keys[0];
	        	note.set_left(Integer.parseInt(keys[1]));
	        	note.set_right(Integer.parseInt(keys[2]));
	        	note.set_width(Integer.parseInt(keys[3]));
	        	note.set_height(Integer.parseInt(keys[4]));
	        	note.set_color(keys[5].toUpperCase());
	        	String temp = "";
	        	int i = 6;
	        	while(i<keys.length) {
	        		temp += keys[i];
	        		temp += " ";
	        		i++;
	        	}
	        	temp.trim();
	        	note.set_message(temp);
	        	note.set_range();
	        	return note;
        	}
        	else {
        		return null;
        	}
        }
        

}


}





