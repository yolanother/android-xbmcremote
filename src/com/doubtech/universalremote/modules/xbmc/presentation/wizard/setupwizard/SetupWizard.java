package com.doubtech.universalremote.modules.xbmc.presentation.wizard.setupwizard;

import org.xbmc.android.util.ClientFactory;
import org.xbmc.android.util.HostFactory;
import org.xbmc.api.object.Host;

import com.doubtech.universalremote.modules.xbmc.presentation.wizard.Wizard;
import com.doubtech.universalremote.modules.xbmc.presentation.wizard.WizardPage;

public class SetupWizard extends Wizard<Host> {

	private Host host;

	@Override
	public void doSetupPages() {
		host = new Host();
		WizardPage<Host> page = new SetupWizardPage1(this, this);
		page.setInput(host);
		addPage(page);
		addPage(new SetupWizardPage2(this, this));
		addPage(new SetupWizardPage3(this, this));
	}

	@Override
	protected void doFinish() {
		HostFactory.addHost(this, host);
		ClientFactory.resetClient(host);
	}

}
