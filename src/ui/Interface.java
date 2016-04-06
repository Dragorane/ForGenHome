package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class Interface extends JFrame
{
	public JLabel jl_name_software;
	public JLabel jl_welcome;
	
	public JPanel jp_progress;
	public JLabel jl_progress;
	public JProgressBar progress_bar;

	
	public JPanel jp_bonjour;
	public JPanel jp_nouveau;
	public JPanel jp_nouveau2;
	public JPanel jp_start;
	public JPanel jp_options;
	public JPanel jp_types;
	public JPanel jp_save;

	public JPanel jp_fichiers;
	
	public JScrollPane jsp_feedback;
	
	public JButton jb_commencer;
	public JButton jb_stop;
	
	//public JMenuBar menu_bar;
	//public JMenu menu_types;
	//public JMenu menu_parameters;
	
	//public JCheckBoxMenuItem prokaryotes;
	//public JCheckBoxMenuItem eukaryotes;
	//public JCheckBoxMenuItem viruses;
	
	//public JCheckBoxMenuItem cds;
	//public JCheckBoxMenuItem sequence;
	
	public JCheckBox check_prokaryotes;
	public JCheckBox check_eukaryotes;
	public JCheckBox check_viruses;
	
	public JCheckBox check_cds;
	public JCheckBox check_sequence;
	public JCheckBox check_hide;
	
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
		
		//this.menu_bar = new JMenuBar();
		
		//this.menu_types = new JMenu("Types");
		//this.menu_parameters = new JMenu("Parametres");
		//this.prokaryotes = new JCheckBoxMenuItem("Prokaryotes",true);
		//this.eukaryotes = new JCheckBoxMenuItem("Eukaryotes",true);
		//this.viruses = new JCheckBoxMenuItem("Viruses",true);
		//this.cds = new JCheckBoxMenuItem("Sauvegarder les genes (CDS)");
		//this.sequence = new JCheckBoxMenuItem("Sauvegarder les genomes (sequence)");
		
		this.check_cds = new JCheckBox("Sauvegarder les genes (CDS)");
		this.check_sequence = new JCheckBox("Sauvegarder les genomes (sequence)");
		this.check_hide = new JCheckBox("hidding for cheeting");
		this.check_hide.setVisible(false);
		
		this.check_eukaryotes = new JCheckBox("Eukaryotes", true);
		this.check_prokaryotes = new JCheckBox("Prokaryotes", true);
		this.check_viruses = new JCheckBox("Viruses", true);
		
		//this.fine = new JCheckBoxMenuItem("Analyse fine (tous les bioprojets)");
		//Si on veut mettre un label
		this.jl_progress = new JLabel("");
		
		this.log_area = new JTextArea(13,10);
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
		
		this.jl_name_software = new JLabel("ForGenHome ---");
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
		//menu_types.add(this.prokaryotes);
		//menu_types.add(this.eukaryotes);
		//menu_types.add(this.viruses);
		
		//menu_parameters.add(this.cds);
		//menu_parameters.add(this.sequence);
		
		//menu_parameters.add(this.fine);
		
		//menu_bar.add(menu_types);
		//menu_bar.add(menu_parameters);
		
		//this.setJMenuBar(this.menu_bar);
		
		
		this.jp_nouveau = new JPanel(new FlowLayout());
		this.jp_nouveau2 = new JPanel(new FlowLayout());
		this.jp_bonjour = new JPanel(new FlowLayout());
		this.jp_start = new JPanel(new FlowLayout());
		this.jp_options = new JPanel(new BorderLayout());
		this.jp_fichiers = new JPanel(new FlowLayout());
		this.jp_progress = new JPanel(new FlowLayout());
		
		this.jp_bonjour.add(this.jl_name_software);
		this.jp_bonjour.add(this.jl_welcome);
		
		this.jp_types = new JPanel(new GridLayout(0,1));
		
		Border border_title = BorderFactory.createTitledBorder("Types");
		Border border_padding = BorderFactory.createEmptyBorder(0, 70, 0, 10);
		CompoundBorder cb = new CompoundBorder(border_padding,border_title);
		this.jp_types.setBorder(cb);
		this.jp_types.add(this.check_eukaryotes);
		this.jp_types.add(this.check_prokaryotes);
		this.jp_types.add(this.check_viruses);
		
		
		this.jp_save = new JPanel(new GridLayout(0,1));
		Border border2_title = BorderFactory.createTitledBorder("Parametres");
		Border border2_padding = BorderFactory.createEmptyBorder(0, 10, 0, 70);
		cb = new CompoundBorder(border2_padding,border2_title);
		this.jp_save.setBorder(cb);
		this.jp_save.add(this.check_cds);
		this.jp_save.add(this.check_sequence);
		this.jp_save.add(this.check_hide);

		/*
		 * ICI CODE POUR PROGRESS BAR
		 */
		this.progress_bar= new JProgressBar();
		Dimension prefSize = this.progress_bar.getPreferredSize();

		prefSize.width = 500;
		prefSize.height = 25;
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
		temp_a.add(jp_nouveau2, BorderLayout.SOUTH);
		
		JPanel temp_c = new JPanel(new GridLayout(0,2));
		temp_c.add(jp_types, BorderLayout.EAST);
		temp_c.add(jp_save, BorderLayout.WEST);
		temp_c.setBackground(Color.YELLOW);
		
		JPanel principal = new JPanel(new BorderLayout());
		principal.add(jp_fichiers,BorderLayout.NORTH);
		principal.add(temp_c, BorderLayout.CENTER);
		principal.add(temp_a,BorderLayout.SOUTH);
		
		area_start.add(this.jsp_feedback, BorderLayout.NORTH);
		area_start.add(this.jp_start,BorderLayout.SOUTH);
		
		this.add(jp_bonjour,BorderLayout.NORTH);
		this.add(principal,BorderLayout.CENTER);
		this.add(area_start,BorderLayout.SOUTH);
		this.setResizable(false);
	}
}
