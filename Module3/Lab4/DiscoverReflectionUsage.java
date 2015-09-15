package toolbox.analysis.analyzers;

import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.Common;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class DiscoverReflectionUsage extends Analyzer {

	@Override
	public String getDescription() {
		return "Find usages of Java Reflection.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"All uses of Reflection are through java.lang.reflect package."};
	}

	@Override
	protected Q evaluateEnvelope() {
		// get all the java.lang.reflect methods
		Q declaresEdges = Common.universe().edgesTaggedWithAny(XCSG.Contains).retainEdges();
		Q reflectionPackage = Common.universe().pkg("java.lang.reflect");
		Q relectionMethods = declaresEdges.forward(reflectionPackage).nodesTaggedWithAny(XCSG.Method);
		Q callEdges = Common.universe().edgesTaggedWithAny(XCSG.Call).retainEdges();
		Q callersOfReflectiveMethods = callEdges.reverseStep(relectionMethods);
		return callersOfReflectiveMethods.intersection(SetDefinitions.app());
	}

}