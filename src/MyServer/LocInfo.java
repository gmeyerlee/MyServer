package MyServer;

import java.util.ArrayList;

public class LocInfo {
	private String id;
	private ArrayList<Integer> loc;
	LocInfo(String id,ArrayList loc){
		this.id=id;
		this.loc=loc;
	}
	public String getid() {
		return this.id;
	}
	public ArrayList getloc() {
		return this.loc;
	}
	public void setid(String id) {
		this.id=id;
	}
	public void setloc(ArrayList loc) {
		this.loc=loc;
	}
	public int totalsize() {
		int mysize=0;
		for (int i=0;i<loc.size();i++) {
			double power=loc.get(i)-1;
			mysize+=Math.pow(2, power)*1000;
		}
		return mysize;
	}
}
