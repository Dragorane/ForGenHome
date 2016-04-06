package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class Interface extends JFrame
{
	public JLabel jl_welcome;
	
	public JPanel jp_progress;
	public JLabel jl_progress;
	public JProgressBar progress_bar;

	
	public JPanel jp_bonjour;
	public JPanel jp_nouveau;
	public JPanel jp_nouveau2;
	public JPanel jp_start;
	public JPanel jp_options;

	public JPanel jp_fichiers;
	
	public JScrollPane jsp_feedback;
	
	public JButton jb_commencer;
	public JButton jb_stop;
	
	public JMenuBar menu_bar;
	public JMenu menu_types;
	public JMenu menu_parameters;
	
	public JCheckBoxMenuItem prokaryotes;
	public JCheckBoxMenuItem eukaryotes;
	public JCheckBoxMenuItem viruses;
	
	public JCheckBoxMenuItem cds;
	public JCheckBoxMenuItem sequence;
	
	public JTextArea log_area;
	
	public void addLog(String message){
		if(log_area.getText().isEmpty()){
			log_area.setText(message);
		}
		else{
			log_area.setText(log_area.getText()+"\n"+message);
		}
		
	}
	
	/**
	 * The constructor of the IHM
	 * @param arg0
	 * @throws Exception
	 */
	public Interface(String arg0) throws Exception 
	{
		super(arg0);
		
		this.menu_bar = new JMenuBar();
		
		this.menu_types = new JMenu("Types");
		this.menu_parameters = new JMenu("Parametres");
		this.prokaryotes = new JCheckBoxMenuItem("Prokaryotes",true);
		this.eukaryotes = new JCheckBoxMenuItem("Eukaryotes",true);
		this.viruses = new JCheckBoxMenuItem("Viruses",true);
		this.cds = new JCheckBoxMenuItem("Sauvegarder les genes (CDS)");
		this.sequence = new JCheckBoxMenuItem("Sauvegarder les genomes (sequence)");
		//this.fine = new JCheckBoxMenuItem("Analyse fine (tous les bioprojets)");
		//Si on veut mettre un label
		this.jl_progress = new JLabel("");
		
		this.log_area = new JTextArea(12,10);
		this.log_area.setFont(new Font("Serif", Font.PLAIN, 14));//police
		final DefaultCaret caret = (DefaultCaret)log_area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);//autoscrolling
		
		jsp_feedback = new JScrollPane(this.log_area);
        
	    JScrollBar scrollBar = jsp_feedback.getVerticalScrollBar();
	    final BoundedRangeModel model = scrollBar.getModel();
	      scrollBar.addAdjustmentListener(new AdjustmentListener() {

	         public void adjustmentValueChanged(AdjustmentEvent e) {
	            if (model.getValue() == model.getMaximum() - model.getExtent()) {
	               caret.setDot(log_area.getText().length());
	               caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	            } else {
	               caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	            }
	         }
	      });
	      
		this.jsp_feedback.setSize(15, 12);
		//fin
		
		this.jl_welcome = new JLabel("Développé par  JF.Muller, M.Lorentz, L.Laisne, T.Gimenes, O.Perardelle, A.Enouf");
		
		//this.jl_fin = new JLabel("Cochez pour une analyse fine (tous les bioprojects)");
		//this.jcb_fin = new JCheckBox();

		this.jb_commencer = new JButton("Commencer");
		this.jb_stop = new JButton("Arreter");	
		this.jb_stop.setEnabled(false);
		
		addToFrame();

		ActionListener start = new InterfaceListener(this);
		this.jb_commencer.addActionListener(start); 
		this.jb_stop.addActionListener(start); 
	}


	/**
	 * Add all panel to the frame
	 * @throws Exception
	 */
	public void addToFrame()
	{
		menu_types.add(this.prokaryotes);
		menu_types.add(this.eukaryotes);
		menu_types.add(this.viruses);
		
		menu_parameters.add(this.cds);
		menu_parameters.add(this.sequence);
		//menu_parameters.add(this.fine);
		
		menu_bar.add(menu_types);
		menu_bar.add(menu_parameters);
		
		this.setJMenuBar(this.menu_bar);
		
		
		this.jp_nouveau = new JPanel(new FlowLayout());
		this.jp_bonjour = new JPanel(new FlowLayout());
		this.jp_start = new JPanel(new FlowLayout());
		this.jp_options = new JPanel(new BorderLayout());
		this.jp_fichiers = new JPanel(new FlowLayout());
		this.jp_progress = new JPanel(new FlowLayout());
		

		this.jp_bonjour.add(this.jl_welcome);

		/*
		 * ICI CODE POUR PROGRESS BAR
		 */
		this.progress_bar= new JProgressBar();
		Dimension prefSize = this.progress_bar.getPreferredSize();

		prefSize.width = 500;
		prefSize.height = 35;
		this.progress_bar.setPreferredSize(prefSize);
		progress_bar.setMaximum(10);
		progress_bar.setMinimum(0);
		progress_bar.setStringPainted(true);
		
		this.jp_progress.add(this.jl_progress);
		this.jp_progress.add(this.progress_bar);
		
		JPanel area_start = new JPanel(new BorderLayout());

		this.jp_options.add(jp_progress, BorderLayout.NORTH);


		this.jp_start.add(this.jb_commencer);
		this.jp_start.add(this.jb_stop);

		
		JPanel temp_a = new JPanel(new BorderLayout());
		temp_a.add(jp_nouveau,BorderLayout.NORTH);
		temp_a.add(jp_options,BorderLayout.CENTER);
		
		JPanel temp_b = new JPanel(new FlowLayout());

		this.jp_fichiers.add(temp_b,BorderLayout.CENTER);
		
		JPanel principal = new JPanel(new BorderLayout());
		principal.add(jp_fichiers,BorderLayout.NORTH);

		principal.add(temp_a,BorderLayout.CENTER);
		
		area_start.add(this.jsp_feedback, BorderLayout.NORTH);
		area_start.add(this.jp_start,BorderLayout.SOUTH);
		
		this.add(jp_bonjour,BorderLayout.NORTH);
		this.add(principal,BorderLayout.CENTER);
		this.add(area_start,BorderLayout.SOUTH);
	}
}
