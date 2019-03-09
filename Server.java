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



