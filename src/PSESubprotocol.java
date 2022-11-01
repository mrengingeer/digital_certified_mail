
import entity.OTReceptMsg;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PSESubprotocol {

	public static boolean pseStep1(PartyBase sender,List<Integer> aSecretStrList){
		sender.output.println("#PSE1.0#");
		try {
			String line = sender.inputBr.readLine();
			if(!line.equals("#OK#")){
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		int n = aSecretStrList.size()/2;
		for (int i = 0; i < n; i++) {
			String ai= String.valueOf(aSecretStrList.get(i));String aiN= String.valueOf(aSecretStrList.get(i+n));
			ObliviousTransfer.OT1to2Sender(sender,ai,aiN);
		}
		return true;
	}

	public static boolean receptMsgFromOtherByOT(PartyBase receiver){
		try {
			if(!receiver.inputBr.readLine().equals("#PSE1.0#")){
				return false;
			}
			receiver.output.println("#OK#");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		for (int i = 0; i < receiver.keyCount; i++) {
			OTReceptMsg msg = ObliviousTransfer.OT1to2Receiver(receiver);
			receiver.secretByOTList.add(msg);
		}
		return true;
	}

	public static boolean pseSendStep2(PartyBase sender){
		sender.output.println("#PSE2.0#");
		List<Integer> aiList = sender.randomKeyInts;
		try {
			String line = sender.inputBr.readLine();
			if(!line.equals("#OK#")){
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		sender.output.println(aiList.size());

		for (Integer ai:aiList){
			sender.output.println(ai);
		}
		return true;
	}

	public static boolean pseReceptStep2(PartyBase receiver){
		try {
			if(!receiver.inputBr.readLine().equals("#PSE2.0#")){
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		receiver.output.println("#OK#");
		int n =0;
		try {
			 n = Integer.parseInt(receiver.inputBr.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		for (int i = 0; i < n; i++) {
			try {
				receiver.secretByTransmitList.add(receiver.inputBr.readLine());
			} catch (IOException e) {
				e.printStackTrace();
				receiver.secretByTransmitList.clear();
				return false;
			}
		}
		return true;
	}

}

