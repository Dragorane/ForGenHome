package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controllers.FileController;
import project.MainLauncher;
import project.GenomeCounter;

public class InterfaceListener implements ActionListener {

	Interface ihm;
	Thread t;
	Thread t2;
	
	public InterfaceListener(Interface ihm)
	{
		this.ihm=ihm;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(ihm.jb_commencer))
		{
			GenomeCounter.stop = false;

			ihm.jb_stop.setEnabled(true);
			ihm.jb_commencer.setEnabled(false);
	
			 t = new Thread() 
		      {
		        public void run() 
		        {
		        	if(ihm.eukaryotes.isSelected())
		        	{
		        		ihm.progress_bar.setMaximum(ihm.progress_bar.getMaximum()+2600);
		        		MainLauncher.fichiersBioinfo.add("eukaryotes.txt");
		        	}
		        	
		        	if(ihm.prokaryotes.isSelected())
		        	{
		        		ihm.progress_bar.setMaximum(ihm.progress_bar.getMaximum()+56300);
		        		MainLauncher.fichiersBioinfo.add("prokaryotes.txt");
		        	}
		        	
		        	if(ihm.viruses.isSelected())
		        	{
		        		ihm.progress_bar.setMaximum(ihm.progress_bar.getMaximum()+5100);
		        		MainLauncher.fichiersBioinfo.add("viruses.txt");
		        	}
	        		FileController.cleaning();
	        		GenomeCounter.demarrerTraitement(ihm);
		        }
		      };
		      t.start();
		}
		else
		{
			GenomeCounter.stop = true;
			t2 = new Thread() 
		      {
		        public void run() 
		        {
		        	t.interrupt();
		        	try 
					{
						t.join();
					}
					catch (InterruptedException e1) {}
		        	finally {
		        	      notifyButtons();
		        	}
		        }
		      };
	      t2.start();

			ihm.jb_stop.setEnabled(false);
			//ihm.jb_commencer.setEnabled(true);
		}
	}
	
	public void notifyButtons()
	{
		ihm.jb_stop.setEnabled(false);
		ihm.jb_commencer.setEnabled(true);
	}

}
