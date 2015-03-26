package org.jenerate.internal.domain.method.content.compareto;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.CommonsLangLibraryUtils;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CommonsLangCompareToMethodContent extends AbstractMethodContent<CompareToDialogData> {

    private final boolean useCommonsLang3;
    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CommonsLangCompareToMethodContent(PreferencesManager preferencesManager,
            GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate, boolean useCommonsLang3,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(preferencesManager, generatorsCommonMethodsDelegate);
        this.useCommonsLang3 = useCommonsLang3;
        this.javaInterfaceCodeAppender = javaInterfaceCodeAppender;
    }

    @Override
    public String getMethodContent(IType objectClass, CompareToDialogData data) throws Exception {
        boolean implementedOrExtendedInSuperType = javaInterfaceCodeAppender.isImplementedOrExtendedInSupertype(
                objectClass, "Comparable");
        boolean generifyPreference = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.GENERIFY_COMPARETO)).booleanValue();
        boolean generify = generifyPreference
                && generatorsCommonMethodsDelegate.isSourceLevelGreaterThanOrEqualTo5(objectClass)
                && !implementedOrExtendedInSuperType;

        if (!implementedOrExtendedInSuperType) {
            String interfaceName = "Comparable";
            if (generify) {
                interfaceName = "Comparable<" + objectClass.getElementName() + ">";
            }
            javaInterfaceCodeAppender.addSuperInterface(objectClass, interfaceName);
        }
        return MethodGenerations.generateCompareToMethodContent(data, generify, objectClass);
    }

    @Override
    public Set<String> getLibrariesToImport(CompareToDialogData data) {
        return Collections.singleton(CommonsLangLibraryUtils.getCompareToBuilderLibrary(useCommonsLang3));
    }

}
