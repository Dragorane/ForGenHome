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
	        		
	        		for(String f : MainLauncher.fichiersBioinfo) {
	        			//System.out.println("Test fichier f : " + f);
	        			vt.add(new GenomeCounter(ihm, f));
	        		}
	        		
	        		for(Thread tTemp : vt) {
	        			tTemp.start();
	        		}
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
		        	
		        	for(Thread tTemp : vt) {
	        			tTemp.interrupt();
	        		}
		        	
		        	try 
					{
						t.join();
						
						for(Thread tTemp : vt) {
		        			tTemp.join();
		        		}
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
