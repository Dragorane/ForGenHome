package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import ui.Interface;

/*
 * Class DownloadTool
 * Allow to get files on the ncbi's website
 */

public class DownloadTool {
	static Interface ihm = null;

	public static void setIHM(Interface i) {
		if (i != null && i != ihm) {
			ihm = i;
		}
	}

	public static void getFile(String host, String name, int nbRelances) {
		InputStream input = null;
		FileOutputStream output = null;

		try {
			// Connection

			URL url = new URL(host);
			URLConnection connection = url.openConnection();

			ihm.addLog("--- Debut du telechargement ---");

			// Retrieve and saving in a temp file

			input = connection.getInputStream();
			if (input != null) {
				String fileName = name;
				output = new FileOutputStream(fileName);
				byte[] buffer = new byte[1024];
				int read = 0;

				while ((read = input.read(buffer)) > 0) {
					output.write(buffer, 0, read);
				}
				output.flush();

			}

			ihm.addLog("--- Fin du telechargement ---");
		} catch (Exception e) {
			try {
				if (nbRelances <= 3) {
					ihm.addLog("Erreur de telechargement sur " + name + " a l'adresse " + host);
					ihm.addLog("Tentative de relance numero " + nbRelances + " dans 5 secondes");
					Thread.sleep(5000);
					getFile(host, name, nbRelances + 1);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("Erreur getFile : " + e.toString());
			e.printStackTrace();
		} finally {
			try {
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
