package project;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ui.Interface;
import utils.DownloadTool;

/*
 * Class MainLauncher
 * Used to run the project
 */

public class MainLauncher {
	public static ArrayList<String> fichiersBioinfo;

	public static void main(String[] args) throws Exception {

		fichiersBioinfo = new ArrayList<String>();

		UIManager.setLookAndFeel(new MetalLookAndFeel());

		Interface bio = new Interface("Projet BioInformatique 2016 --- ForGenHome");
		DownloadTool.setIHM(bio);

		bio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		bio.pack();

		bio.setSize(800, 500);

		bio.setVisible(true);

	}
}
