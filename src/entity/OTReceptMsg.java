
package entity;

public class OTReceptMsg {

	private int idx;

	private String msg;

	public OTReceptMsg(int idx, String msg) {
		this.idx = idx;
		this.msg = msg;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

