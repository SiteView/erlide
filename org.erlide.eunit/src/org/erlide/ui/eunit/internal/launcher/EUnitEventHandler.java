package org.erlide.ui.eunit.internal.launcher;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.core.ILaunch;
import org.erlide.core.util.Tuple;
import org.erlide.eunit.EUnitPlugin;
import org.erlide.jinterface.backend.Backend;
import org.erlide.jinterface.backend.events.EventHandler;
import org.erlide.jinterface.util.ErlLogger;
import org.erlide.ui.eunit.internal.model.ITestRunListener2;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangTuple;

public class EUnitEventHandler extends EventHandler {

	private final OtpErlangPid eventPid;
	private final ILaunch launch;
	private final Backend backend;
	private final ListenerList /* <ITestRunListener2> */listeners = new ListenerList();

	public EUnitEventHandler(final OtpErlangPid eventPid, final ILaunch launch,
			final Backend backend) {
		this.eventPid = eventPid;
		this.launch = launch;
		this.backend = backend;
		EUnitPlugin.getModel().addEventHandler(this);
	}

	enum EUnitMsgWhat {
		test_begin, test_end, test_cancel, group_begin, group_end, group_cancel;

		static Set<String> allNames() {
			final EUnitMsgWhat[] values = values();
			final HashSet<String> result = new HashSet<String>(values.length);
			for (final EUnitMsgWhat value : values) {
				result.add(value.name());
			}
			return result;
		}
	}

	private interface AllListeners {
		void apply(ITestRunListener2 listener);
	}

	private void allListeners(final AllListeners application) {
		for (final Object listener : listeners.getListeners()) {
			application.apply((ITestRunListener2) listener);
		}
	}

	Set<String> allMsgWhats = EUnitMsgWhat.allNames();

	@Override
	protected void doHandleMsg(final OtpErlangObject msg) throws Exception {
		ErlLogger.debug("EUnitEventHandler %s", msg);
		if (msg instanceof OtpErlangTuple) {
			final Tuple<EUnitMsgWhat, OtpErlangTuple> eunitMsg = getEUnitMsg(msg);
			if (eunitMsg == null) {
				ErlLogger.error("EUnitEventHandler unknown msg '%s'", msg);
				return;
			}
			final EUnitMsgWhat what = eunitMsg.o1;
			final OtpErlangTuple argument = eunitMsg.o2;
			AllListeners al = null;
			switch (what) {
			case group_begin:
				al = new AllListeners() {

					public void apply(final ITestRunListener2 listener) {
						listener.testStarted("id", "name");
					}
				};
			case group_end:
			case group_cancel:
			case test_begin:
			case test_end:
			case test_cancel:
			}
			if (al != null) {
				allListeners(al);
			}
		}
	}

	private Tuple<EUnitMsgWhat, OtpErlangTuple> getEUnitMsg(
			final OtpErlangObject msg) {
		if (!(msg instanceof OtpErlangTuple)) {
			return null;
		}
		final OtpErlangTuple t = (OtpErlangTuple) msg;
		if (t.arity() != 3) {
			return null;
		}
		final OtpErlangPid jpid = (OtpErlangPid) t.elementAt(1);
		if (!jpid.equals(eventPid)) {
			return null;
		}
		final OtpErlangAtom whatA = (OtpErlangAtom) t.elementAt(0);
		final String what = whatA.atomValue();
		if (!allMsgWhats.contains(what)) {
			return null;
		}
		final OtpErlangTuple argumentT = (OtpErlangTuple) t.elementAt(2);
		return new Tuple<EUnitMsgWhat, OtpErlangTuple>(
				EUnitMsgWhat.valueOf(what), argumentT);
	}

	public ILaunch getLaunch() {
		return launch;
	}

	public void addListener(final ITestRunListener2 listener) {
		listeners.add(listener);
	}

	public void removeListener(final ITestRunListener2 listener) {
		listeners.remove(listener);
	}

	public void shutdown() {
		listeners.clear();
		backend.getEventDaemon().removeHandler(this);
	}
}
