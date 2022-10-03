package Customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import Network.Protocol;
import javafx.application.Application;

public class Main {
	public static void main(String[] args) {
			Application.launch(Login.class);
	}
}