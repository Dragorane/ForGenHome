package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import controllers.FileController;
import project.GenomeCounter;
import project.MainLauncher;

public class InterfaceListener implements ActionListener {

	Interface ihm;
	Thread t;
	Thread t2;
	Vector<Thread> vt = new Vector<Thread>();

	public InterfaceListener(Interface ihm) {
		this.ihm = ihm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(ihm.jb_commencer)) {
			GenomeCounter.stop = false;

			ihm.jb_stop.setEnabled(true);
			ihm.jb_commencer.setEnabled(false);

			t = new Thread() {
				public void run() {
					if (ihm.check_eukaryotes.isSelected()) {
						MainLauncher.fichiersBioinfo.add("eukaryotes.txt");
						FileController.cleaning("eukaryotes");
					}

					if (ihm.check_prokaryotes.isSelected()) {
						MainLauncher.fichiersBioinfo.add("prokaryotes.txt");
						FileController.cleaning("prokaryotes");
					}

					if (ihm.check_viruses.isSelected()) {
						MainLauncher.fichiersBioinfo.add("viruses.txt");
						FileController.cleaning("viruses");
					}
					

					for (String f : MainLauncher.fichiersBioinfo) {
						// System.out.println("Test fichier f : " + f);
						vt.add(new GenomeCounter(ihm, f));
					}

					for (Thread tTemp : vt) {
						tTemp.start();
					}
				}
			};
			t.start();
		} else {
			GenomeCounter.stop = true;
			t2 = new Thread() {
				public void run() {
					t.interrupt();

					for (Thread tTemp : vt) {
						tTemp.interrupt();
					}

					try {
						t.join();

						for (Thread tTemp : vt) {
							tTemp.join();
						}
					} catch (InterruptedException e1) {
					} finally {
						notifyButtons();
					}
				}
			};
			t2.start();

			ihm.jb_stop.setEnabled(false);
			// ihm.jb_commencer.setEnabled(true);
		}
	}

	public void notifyButtons() {
		ihm.jb_stop.setEnabled(false);
		ihm.jb_commencer.setEnabled(true);
	}

}
