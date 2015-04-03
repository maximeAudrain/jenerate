package org.jenerate.internal.ui.dialogs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jenerate.internal.domain.data.ToStringGenerationData;
import org.jenerate.internal.domain.data.impl.ToStringGenerationDataImpl;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.strategy.method.content.impl.commonslang.CommonsLangToStringStyle;

/**
 * @author jiayun
 */
public class ToStringDialog extends AbstractOrderableFieldDialog<ToStringGenerationData> {

	public static final String SETTINGS_SECTION = "ToStringDialog";

	private static final String SETTINGS_STYLE = "CommonsLangToStringStyle";

	private Combo styleCombo;

	private String toStringStyle;

	private IDialogSettings settings;

	public ToStringDialog(final Shell parentShell, final String dialogTitle, final IType objectClass,
			final IField[] fields, final Set<IMethod> excludedMethods, final boolean disableAppendSuper,
			PreferencesManager preferencesManager, IDialogSettings dialogSettings) throws JavaModelException {

		super(parentShell, dialogTitle, objectClass, fields, excludedMethods, disableAppendSuper, preferencesManager,
				dialogSettings);

		settings = dialogSettings.getSection(SETTINGS_SECTION);
		if (settings == null) {
			settings = dialogSettings.addNewSection(SETTINGS_SECTION);
		}

		toStringStyle = settings.get(SETTINGS_STYLE);
		if (toStringStyle == null) {
			toStringStyle = CommonsLangToStringStyle.NO_STYLE.getFullLibraryString(getPreferencesManager()
					.getCurrentPreferenceValue(JeneratePreferences.USE_COMMONS_LANG3).booleanValue());
		}
	}

	@Override
	public boolean close() {
		toStringStyle = styleCombo.getText();
		settings.put(SETTINGS_STYLE, toStringStyle);
		return super.close();
	}

	@Override
	protected Composite createOptionComposite(Composite composite) {
		Composite optionComposite = super.createOptionComposite(composite);
		addStyleChoices(optionComposite);
		return optionComposite;
	}

	private Composite addStyleChoices(final Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setText("&ToString style:");

		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		label.setLayoutData(data);

		styleCombo = new Combo(composite, SWT.NONE);
		boolean useCommonsLang3 = getPreferencesManager().getCurrentPreferenceValue(
				JeneratePreferences.USE_COMMONS_LANG3).booleanValue();
		List<String> styles = new ArrayList<String>();
		for (CommonsLangToStringStyle style : CommonsLangToStringStyle.values()) {
			if (CommonsLangToStringStyle.NO_STYLE.equals(style)) {
				styles.add(CommonsLangToStringStyle.NO_STYLE.name());
			} else {
				styles.add(style.getFullLibraryString(useCommonsLang3));
			}
		}
		styleCombo.setItems(styles.toArray(new String[styles.size()]));
		styleCombo.setText(toStringStyle);

		data = new GridData(GridData.FILL_HORIZONTAL);
		styleCombo.setLayoutData(data);
		styleCombo.setToolTipText(CommonsLangToStringStyle.getToStringStyle(toStringStyle).getToolTip());

		styleCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				Combo combo = (Combo)event.getSource();
				combo.setToolTipText(CommonsLangToStringStyle.getToStringStyle(combo.getText()).getToolTip());
			}
		});
		return composite;
	}

	@Override
	public ToStringGenerationData getData() {
		//@formatter:off
		return new ToStringGenerationDataImpl.Builder()
		.withCheckedFields(getCheckedFields())
		.withElementPosition(getElementPosition())
		.withAppendSuper(getAppendSuper())
		.withGenerateComment(getGenerateComment())
		.withUseBlockInIfStatements(getUseBlockInIfStatements())
		.withUseGettersInsteadOfFields(getUseGettersInsteadOfFields())
		.withToStringStyle(CommonsLangToStringStyle.getToStringStyle(toStringStyle))
		.build();
		//@formatter:on
	}

	@Override
	public Dialog getDialog() {
		return this;
	}
}
