/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rtpmt.android.network.tcp2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;
//import gnu.io.*;
import rtpmt.motes.packet.Packetizer;
import rtpmt.network.packet.SensorMessage.SensorInformation;

public class Communicator// ,SerialPortEventListener
{
	// passed from main GUI
	// Tcp client object to send data over network
	TCPClient tCPClient;
	// for containing the ports that will be found
	private Enumeration ports = null;
	// map the port names to CommPortIdentifiers
	private HashMap portMap = new HashMap();

	// this is the object that contains the opened port
	// private CommPortIdentifier selectedPortIdentifier = null;
	// private SerialPort serialPort = null;

	// input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	// just a boolean flag that i use for enabling
	// and disabling buttons depending on whether the program
	// is connected to a serial port or not
	private boolean bConnected = false;

	// the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	// some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;

	// a string for recording what goes on in the program
	// this string is written to the GUI
	String logText = "";

	// packet reader to read the data from the motes
	Packetizer packetReader;

	// background thread for sending data to the server.
	// private Thread bgCommunicator;

	// to indicate whether to send data to server or not
	private boolean ServerAvailable;

	public Communicator() {
		ServerAvailable = false;
	}

	// search for all the serial ports
	// pre: none
	// post: adds all the found ports to a combo box on the GUI
	/*
	 * public void searchForPorts() { ports =
	 * CommPortIdentifier.getPortIdentifiers();
	 * 
	 * while (ports.hasMoreElements()) { CommPortIdentifier curPort =
	 * (CommPortIdentifier)ports.nextElement();
	 * 
	 * //get only serial ports if (curPort.getPortType() ==
	 * CommPortIdentifier.PORT_SERIAL) { portMap.put(curPort.getName(),
	 * curPort); } } }
	 */

	// connect to the selected port in the combo box
	// pre: ports are already found by using the searchForPorts method
	// post: the connected comm port is stored in commPort, otherwise,
	// an exception is generated
	public void connect() {

		// CommPort commPort = null;
		//
		// try
		// {
		// //the method below returns an object of type CommPort
		// commPort = selectedPortIdentifier.open("RFID", TIMEOUT);
		// //the CommPort object can be casted to a SerialPort object
		// serialPort = (SerialPort)commPort;
		// //setting serial port parameters
		// //this setting is based on telosb mote specification
		// //baud rate is important here i.e(115200)
		// serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
		// for controlling GUI elements
		setConnected(true);

		// CODE ON SETTING BAUD RATE ETC OMITTED
		// XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY
		// }
		// catch (PortInUseException e)
		// {
		// logText = " is in use. (" + e.toString() + ")";
		// System.out.println(logText);
		//
		// }
		// catch (Exception e)
		// {
		// logText = "Failed to open (" + e.toString() + ")";
		// System.out.println(logText);
		// }
	}

	// open the input and output streams
	// pre: an open port
	// post: initialized intput and output streams for use to communicate data
	public boolean initIOStream() {
		// return value for whather opening the streams is successful or not
		boolean successful = false;

		// try {
		// //initialize the input and output stream
		// input = serialPort.getInputStream();
		// output = serialPort.getOutputStream();

		// writeData(0, 0);
		successful = true;
		return successful;
		// }
		// catch (IOException e) {
		// logText = "I/O Streams failed to open. (" + e.toString() + ")";
		// System.out.println(logText);
		// return successful;
		// }
	}

	/**
	 * Initialize the packet reader from the motes pre: initialize the input
	 * stream and output stream post: packetReader is initialized and ready to
	 * read
	 */
	public boolean initPacketReader() {

		// return value for whather opening the streams is successful or not
		boolean successful = false;

		try {
			packetReader = new Packetizer("Packet Reader", input);
			packetReader.open(null);
			successful = true;
		} catch (IOException ex) {
			logText = "I/O Streams failed to open. (" + ex.toString() + ")";
			System.out.println(logText);
			successful = false;
		}

		return successful;
	}

	// starts the event listener that knows whenever data is available to be
	// read
	// pre: an open serial port
	// post: an event listener for the serial port that knows when data is
	// recieved
	public void initListener() {

		if (bConnected) {
			send();
		}
		// try
		// {
		// serialPort.addEventListener(this);
		// serialPort.notifyOnDataAvailable(true);

		// if (!bgCommunicator.isAlive()) {
		// bgCommunicator.start();
		// }

		// }
		// catch (TooManyListenersException e)
		// {
		// logText = "Too many listeners. (" + e.toString() + ")";
		// System.out.println(logText);
		// }
	}

	// disconnect the serial port
	// pre: an open serial port
	// post: clsoed serial port
	public void disconnect() {

		// close the serial port
		try {
			// writeData(0, 0);

			// serialPort.removeEventListener();
			// serialPort.close();
			// input.close();
			// output.close();
			// diconnect tcp connection
			tCPClient.disconnect();
			setConnected(false);

			logText = "Disconnected.";
			System.out.println(logText);
		} catch (Exception e) {
			logText = "Failed to close " /* + serialPort.getName() */+ "("
					+ e.toString() + ")";
			System.out.println(logText);
		}
	}

	final public boolean getConnected() {
		return bConnected;
	}

	public void setConnected(boolean bConnected) {
		this.bConnected = bConnected;

	}

	// what happens when data is received
	// pre: serial event is triggered
	// post: processing on the data it reads
	/*
	 * @Override public void serialEvent(SerialPortEvent evt) {
	 * 
	 * if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) { try { String
	 * dummydata = "#:367:N:43.1049:W:77.6233:ZBBBB00000849";
	 * 
	 * 
	 * //logText = packetReader.dumpPacket();
	 * 
	 * //logText = packetReader.getTemperature(); // String data = dummydata +
	 * temperature;
	 * 
	 * // tCPClient.sendData(data);
	 * 
	 * System.out.println("Data:" + logText); logText = ""; } catch (Exception
	 * e) { logText = "Failed to read data. (" + e.toString() + ")";
	 * System.out.println(logText); } } }
	 */

	// method that can be called to send data
	// pre: open serial port
	// post: data sent to the other device
	public void writeData(int leftThrottle, int rightThrottle) {
		try {
			output.write(leftThrottle);
			output.flush();
			// this is a delimiter for the data
			output.write(DASH_ASCII);
			output.flush();

			output.write(rightThrottle);
			output.flush();
			// will be read as a byte so it is a space key
			output.write(SPACE_ASCII);
			output.flush();
		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			System.out.println(logText);
		}
	}

	//
	public void initalizeTCPClient(String ipAddress, int portNumber) {
		try {
			tCPClient = new TCPClient();
			tCPClient.connect(ipAddress, portNumber);
			ServerAvailable = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// thread to get the mote packet from the queue and send it to the server
	public void send() {
		try {
			while (getConnected()) {
				// byte[] packet = packetReader.readRawPacket();

				SensorInformation sensorInfo = packetReader.readPacket();
				for (SensorInformation.Sensor sensor : sensorInfo
						.getSensorsList()) {
					String message = sensor.getSensorType().name() + " : "
							+ sensor.getSensorValue() + " "
							+ sensor.getSensorUnit();
					System.out.println(message);
				}

				System.out.println();
				System.out.flush();

				if (true) {// ServerAvailable){
					tCPClient.sendData(sensorInfo);
				}
			}
		} catch (IOException ex) {
			logText = "Too many listeners. (" + ex.toString() + ")";
			System.out.println(logText);
		}
	}

}
