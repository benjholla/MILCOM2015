package toolbox.analysis.analyzers;

import com.ensoftcorp.atlas.core.query.Q;
import com.ensoftcorp.atlas.core.script.CommonQueries;
import com.ensoftcorp.atlas.core.xcsg.XCSG;
import com.ensoftcorp.open.android.essentials.permissions.Permission;
import com.ensoftcorp.open.android.essentials.permissions.mappings.PermissionMapping;
import com.ensoftcorp.open.toolbox.commons.analysis.Analyzer;

public class DiscoverReadPhoneStateUsage extends Analyzer {

	@Override
	public String getDescription() {
		return "Shows callers of READ_PHONE_STATE permission methods.";
	}

	@Override
	public String[] getAssumptions() {
		return new String[]{"Apps better have a good reason for calling phone state permissions", 
				"Phone state permissions are called directly (excluding native code, reflection, or shell commands)"};
	}

	@Override
	protected Q evaluateEnvelope() {
		PermissionMapping.applyTags(19);
		Q readPhoneStatePermissionMethods = PermissionMapping.getMethods(Permission.READ_PHONE_STATE, 19);
		return CommonQueries.interactions(appContext, readPhoneStatePermissionMethods, XCSG.Call);
	}

}