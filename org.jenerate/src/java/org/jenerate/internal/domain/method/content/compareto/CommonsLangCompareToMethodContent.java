package org.jenerate.internal.domain.method.content.compareto;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.jenerate.internal.data.CompareToDialogData;
import org.jenerate.internal.domain.MethodContentStrategyIdentifier;
import org.jenerate.internal.domain.method.content.AbstractMethodContent;
import org.jenerate.internal.domain.method.content.CommonsLangLibraryUtils;
import org.jenerate.internal.domain.method.skeleton.impl.CompareToMethod;
import org.jenerate.internal.lang.MethodGenerations;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;
import org.jenerate.internal.util.JavaInterfaceCodeAppender;

public class CommonsLangCompareToMethodContent extends AbstractMethodContent<CompareToMethod, CompareToDialogData> {

    private final JavaInterfaceCodeAppender javaInterfaceCodeAppender;

    public CommonsLangCompareToMethodContent(MethodContentStrategyIdentifier methodContentStrategyIdentifier,
            PreferencesManager preferencesManager, GeneratorsCommonMethodsDelegate generatorsCommonMethodsDelegate,
            JavaInterfaceCodeAppender javaInterfaceCodeAppender) {
        super(methodContentStrategyIdentifier, preferencesManager, generatorsCommonMethodsDelegate);
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
        boolean useCommonsLang3 = false;
        if(MethodContentStrategyIdentifier.USE_COMMONS_LANG3.equals(methodContentStrategyIdentifier)) {
            useCommonsLang3 = true;
        }
        return Collections.singleton(CommonsLangLibraryUtils.getCompareToBuilderLibrary(useCommonsLang3));
    }

    @Override
    public Class<CompareToMethod> getRelatedMethodSkeletonClass() {
        return CompareToMethod.class;
    }

}
