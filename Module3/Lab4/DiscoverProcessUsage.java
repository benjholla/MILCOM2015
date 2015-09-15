package toolbox.analysis.analyzers;

import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class DiscoverProcessUsage extends Analyzer {

	@Override
	public String getDescription() {
		return "Finds calls to methods that would allow the application to run a shell command.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"The only way to run shell commands is by directly invoking the Runtime.exec method."};
	}

	@Override
	protected Q evaluateEnvelope() {
		Q runtimeType = Common.typeSelect("java.lang", "Runtime");
		Q declaresEdges = Common.universe().edgesTaggedWithAny(XCSG.Contains).retainEdges();
		Q runtimeMethods = declaresEdges.forwardStep(runtimeType).nodesTaggedWithAny(XCSG.Method);
		Q execMethods = runtimeMethods.intersection(Common.methods("exec"));
		Q callEdges = Common.universe().edgesTaggedWithAny(XCSG.Call).retainEdges();
		Q callersOfExecMethods = callEdges.reverseStep(execMethods);
		return callersOfExecMethods.intersection(SetDefinitions.app());
	}

}