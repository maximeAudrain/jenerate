package org.jenerate.internal.lang.generators.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.lang.generators.GeneratorsCommonMethodsDelegate;
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * XXX get rid of me once the code is tested and the strategy pattern in effect. Also see if code redundancy can be
 * removed.
 * 
 * @author maudrain
 */
public class GeneratorsCommonMethodsDelegateImpl implements GeneratorsCommonMethodsDelegate {

    @Override
    public boolean isOverriddenInSuperclass(IType objectClass, String methodName,
            String[] methodParameterTypeSignatures, String originalClassFullyQualifiedName) throws JavaModelException {
        ITypeHierarchy typeHierarchy = objectClass.newSupertypeHierarchy(null);
        IType[] superclasses = typeHierarchy.getAllSuperclasses(objectClass);

        if (superclasses.length == 0) {
            return false;
        }

        for (int i = 0; i < superclasses.length; i++) {
            if (superclasses[i].getFullyQualifiedName().equals(originalClassFullyQualifiedName)) {
                return false;
            }

            IMethod method = superclasses[i].getMethod(methodName, methodParameterTypeSignatures);
            if (method.exists()) {
                if (Flags.isAbstract(method.getFlags())) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean areAllFinalFields(IField[] fields) throws JavaModelException {
        for (int i = 0; i < fields.length; i++) {
            if (!Flags.isFinal(fields[i].getFlags())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isSourceLevelGreaterThanOrEqualTo5(IType objectClass) {
        IJavaProject project = objectClass.getJavaProject();
        float sc = Float.parseFloat(project.getOption(JavaCore.COMPILER_SOURCE, true));
        return sc >= 1.5;
    }

    /**
     * Move closer to dialog provider
     */
    @Override
    public IField[] getObjectClassFields(IType objectClass, PreferencesManager preferencesManager)
            throws JavaModelException {
        boolean displayFieldsOfSuperClasses = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES)).booleanValue();
        IField[] fields;
        if (displayFieldsOfSuperClasses) {
            fields = getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass,
                    preferencesManager);
        } else {
            fields = getNonStaticNonCacheFields(objectClass, preferencesManager);
        }
        return fields;
    }

    private IField[] getNonStaticNonCacheFields(IType objectClass, PreferencesManager preferencesManager)
            throws JavaModelException {
        Set<String> cacheFields = new HashSet<>();
        cacheFields.add((String) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.HASHCODE_CACHING_FIELD));
        cacheFields.add((String) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.TOSTRING_CACHING_FIELD));

        IField[] fields;
        fields = objectClass.getFields();

        List<IField> result = new ArrayList<>();

        for (int i = 0, size = fields.length; i < size; i++) {
            if (!Flags.isStatic(fields[i].getFlags()) && !cacheFields.contains(fields[i].getElementName())) {
                result.add(fields[i]);
            }
        }

        return result.toArray(new IField[result.size()]);
    }

    private IField[] getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(IType objectClass,
            PreferencesManager preferencesManager) throws JavaModelException {
        List<IField> result = new ArrayList<>();

        ITypeHierarchy typeHierarchy = objectClass.newSupertypeHierarchy(null);
        IType[] superclasses = typeHierarchy.getAllSuperclasses(objectClass);

        for (int i = 0; i < superclasses.length; i++) {
            IField[] fields = superclasses[i].getFields();

            boolean samePackage = objectClass.getPackageFragment().getElementName()
                    .equals(superclasses[i].getPackageFragment().getElementName());

            for (int j = 0; j < fields.length; j++) {

                if (!samePackage && !Flags.isPublic(fields[j].getFlags()) && !Flags.isProtected(fields[j].getFlags())) {
                    continue;
                }

                if (!Flags.isPrivate(fields[j].getFlags()) && !Flags.isStatic(fields[j].getFlags())) {
                    result.add(fields[j]);
                }
            }
        }

        result.addAll(Arrays.asList(getNonStaticNonCacheFields(objectClass, preferencesManager)));

        return result.toArray(new IField[result.size()]);
    }
}
