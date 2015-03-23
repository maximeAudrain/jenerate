// $Id$
package org.jenerate.internal.util;

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
import org.jenerate.internal.ui.preferences.JeneratePreference;
import org.jenerate.internal.ui.preferences.PreferencesManager;

/**
 * This class contains some code from org.eclipse.jdt.internal.corext.codemanipulation.StubUtility
 * org.eclipse.jdt.internal.corext.util.Strings
 * 
 * @author jiayun
 */
public final class JavaUtils {

    private JavaUtils() {
    }

    /**
     * Check if the method specified in the methodName and methodParameterTypeSignatures parameters is overridden and
     * concrete in a subclass of the original declared class, and that subclass is a superclass of objectClass.
     * 
     * @param objectClass
     * @param methodName
     * @param methodParameterTypeSignatures
     * @param originalClassFullyQualifiedName
     * @return
     * @throws JavaModelException
     */
    private static boolean isOverriddenInSuperclass(final IType objectClass, final String methodName,
            final String[] methodParameterTypeSignatures, final String originalClassFullyQualifiedName)
            throws JavaModelException {
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

    public static boolean isEqualsOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return isOverriddenInSuperclass(objectClass, "equals", new String[] { "QObject;" }, "java.lang.Object");
    }

    public static boolean isHashCodeOverriddenInSuperclass(final IType objectClass) throws JavaModelException {
        return isOverriddenInSuperclass(objectClass, "hashCode", new String[0], "java.lang.Object");
    }

    public static boolean isToStringConcreteInSuperclass(final IType objectClass) throws JavaModelException {
        return isOverriddenInSuperclass(objectClass, "toString", new String[0], null);
    }

    public static boolean isCompareToImplementedInSuperclass(final IType objectClass) throws JavaModelException {
        return isOverriddenInSuperclass(objectClass, "compareTo", new String[] { "QObject;" }, null);
    }

    

    public static boolean areAllFinalFields(final IField[] fields) throws JavaModelException {
        for (int i = 0; i < fields.length; i++) {
            if (!Flags.isFinal(fields[i].getFlags())) {
                return false;
            }
        }

        return true;
    }

    public static IField[] getNonStaticNonCacheFields(final IType objectClass, PreferencesManager preferencesManager)
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

    public static IField[] getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(
            final IType objectClass, PreferencesManager preferencesManager) throws JavaModelException {

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

    public static boolean isSourceLevelGreaterThanOrEqualTo5(IJavaProject project) {
        float sc = Float.parseFloat(project.getOption(JavaCore.COMPILER_SOURCE, true));
        return sc >= 1.5;
    }
}
