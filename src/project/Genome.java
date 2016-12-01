package project;

import java.io.File;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/*
 * Class Genome
 * Create a genome object with a name, group, references...
 */

public class Genome {
	@Getter
	private String kingdom;
	@Getter
	private String group;
	@Getter
	private String subgroup;
	@Getter
	private String name;
	@Getter
	private String bioproject;
	@Getter
	@Setter
	private String gcf;
	@Getter
	private ArrayList<String[]> refseq;
	@Getter
	@Setter
	private String updateDate;

	@Getter
	@Setter
	private long nbSeqChrom;
	@Getter
	@Setter
	private long nbSeqChloro;
	@Getter
	@Setter
	private long nbSeqMito;
	@Getter
	@Setter
	private long nbSeqPlasm;

	private final String mainDir = "Results";

	public Genome(String kingdom, String group, String subgroup, String name, String bioproject) {
		this.kingdom = kingdom.replaceAll(" ", "_");
		this.group = group.replaceAll(" ", "_");
		this.subgroup = subgroup.replaceAll(" ", "_");
		this.name = name.replaceAll(" ", "_");
		this.bioproject = bioproject;
		this.refseq = new ArrayList<String[]>();
		this.nbSeqChrom = 0;
		this.nbSeqChloro = 0;
		this.nbSeqMito = 0;
		this.nbSeqPlasm = 0;
	}

	public String getKingdomChemin() {
		return this.mainDir + "/" + this.kingdom;
	}

	public String getGroupChemin() {
		return this.mainDir + "/" + this.kingdom + "/" + this.getGroup();
	}

	public String getSubGroupChemin() {
		return this.mainDir + "/" + this.kingdom + "/" + this.getGroup() + "/" + this.getSubgroup();
	}

	public String getChemin() {
		return this.mainDir + "/" + this.kingdom + "/" + this.getGroup() + "/" + this.getSubgroup() + "/";

	}

	public String getMainDir() {
		return mainDir;
	}

	public int nbRefSeq() {
		return this.getRefseq().size();
	}

	public boolean exists() {
		File f = new File(this.getChemin());
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSubgroup() {
		return subgroup;
	}

	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String[]> getRefseq() {
		return refseq;
	}

	public void setRefseq(ArrayList<String[]> refseq) {
		this.refseq = refseq;
	}

	public String getKingdom() {
		return kingdom;
	}

	public void setKingdom(String kingdom) {
		this.kingdom = kingdom;
	}

	public String getBioproject() {
		return bioproject;
	}

	public void setBioproject(String bioproject) {
		this.bioproject = bioproject;
	}

	public String getGcf() {
		return gcf;
	}

	public void setGcf(String gcf) {
		this.gcf = gcf;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public long getNbSeqChrom() {
		return nbSeqChrom;
	}

	public void setNbSeqChrom(long nbSeqChrom) {
		this.nbSeqChrom = nbSeqChrom;
	}

	public long getNbSeqChloro() {
		return nbSeqChloro;
	}

	public void setNbSeqChloro(long nbSeqChloro) {
		this.nbSeqChloro = nbSeqChloro;
	}

	public long getNbSeqMito() {
		return nbSeqMito;
	}

	public void setNbSeqMito(long nbSeqMito) {
		this.nbSeqMito = nbSeqMito;
	}

	public long getNbSeqPlasm() {
		return nbSeqPlasm;
	}

	public void setNbSeqPlasm(long nbSeqPlasm) {
		this.nbSeqPlasm = nbSeqPlasm;
	}

	public long getNbSeqTemp() {
		return nbSeqTemp;
	}

	public void setNbSeqTemp(long nbSeqTemp) {
		this.nbSeqTemp = nbSeqTemp;
	}

	@Override
	public String toString() {
		ArrayList<String> numRefSeq = new ArrayList<String>();
		for (String[] ref : this.refseq) {
			numRefSeq.add(ref[0]);
		}
		return this.mainDir + "/" + this.kingdom + "/" + this.getGroup() + "/" + this.getSubgroup() + "/" + this.name
				+ numRefSeq;
	}

}
