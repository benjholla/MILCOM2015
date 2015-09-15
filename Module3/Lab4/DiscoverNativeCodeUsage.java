package toolbox.analysis.analyzers;

import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.toolbox.commons.SetDefinitions;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class DiscoverNativeCodeUsage extends Analyzer {

	@Override
	public String getDescription() {
		return "Finds calls to native code (C or C++).";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"All native calls are using JNI and are flagged with the keyword native."};
	}

	@Override
	protected Q evaluateEnvelope() {
		return SetDefinitions.app().nodesTaggedWithAny(XCSG.Java.nativeMethod);
	}

}