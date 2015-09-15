package toolbox.analysis.analyzers;

import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class DiscoverAbortBroadcastReceivers extends Analyzer {

	@Override
	public String getDescription() {
		return "Discovers BroadcastReceivers that abort broadcast messages.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[] {"Broadcast Receivers that call the android.content.BroadcastReceiver.abortBroadcast() "
				+ "method from a onReceive method are aborting broadcasts."};
	}

	@Override
	protected Q evaluateEnvelope() {
		Q callEdges = Common.universe().edgesTaggedWithAny(XCSG.Call).retainEdges();
		Q abortBroadcast = Common.methodSelect("android.content", "BroadcastReceiver", "abortBroadcast");
		Q callersOfAbortBroadcast = callEdges.reverseStep(abortBroadcast);
		Q onReceive = Common.methods("onReceive");
		return callersOfAbortBroadcast.intersection(onReceive);
	}

}