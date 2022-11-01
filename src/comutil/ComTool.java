package comutil;

import java.util.List;

import entity.OTReceptMsg;

public class ComTool {

	public static boolean compareOTAndTransmit(List<OTReceptMsg> otReceptMsgs, List<String> transmits){
		int n = otReceptMsgs.size();
		for (int i = 0; i < n; i++) {
			OTReceptMsg otReceptMsg = otReceptMsgs.get(i);
			String targetStr = otReceptMsg.getIdx() == 0?transmits.get(i):transmits.get(i+n);
			if(!targetStr.equals(otReceptMsg.getMsg())){
				return false;
			}
		}
		return true;
	}
}