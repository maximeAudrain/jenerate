package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.jenerate.JeneratePlugin;
import org.jenerate.internal.domain.preference.impl.JeneratePreferences;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.factory.DialogFactoryHelper;

/**
 * Default implementation of the {@link DialogFactoryHelper}. XXX test me
 * 
 * @author maudrain
 */
public final class DialogFactoryHelperImpl implements DialogFactoryHelper {

    /**
     * First method insertion position label
     */
    public static final String FIRST_METHOD_POSITION = "First method";

    /**
     * Last method insertion position label
     */
    public static final String LAST_METHOD_POSITION = "Last method";

    @Override
    public boolean isOverriddenInSuperclass(IType objectClass, String methodName,
            String[] methodParameterTypeSignatures, String originalClassFullyQualifiedName) throws JavaModelException {
        ITypeHierarchy typeHierarchy = objectClass.newSupertypeHierarchy(null);
        IType[] superclasses = typeHierarchy.getAllSuperclasses(objectClass);

        if (superclasses.length == 0) {
            return false;
        }

        for (IType superclass : superclasses) {
            if (superclass.getFullyQualifiedName().equals(originalClassFullyQualifiedName)) {
                return false;
            }

            IMethod method = superclass.getMethod(methodName, methodParameterTypeSignatures);
            if (method.exists()) {
                return !Flags.isAbstract(method.getFlags());
            }
        }

        return false;
    }

    @Override
    public IField[] getObjectClassFields(IType objectClass, PreferencesManager preferencesManager)
            throws JavaModelException {
        boolean displayFieldsOfSuperClasses = preferencesManager.getCurrentPreferenceValue(
                JeneratePreferences.DISPLAY_FIELDS_OF_SUPERCLASSES).booleanValue();
        if (displayFieldsOfSuperClasses) {
            return getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass, preferencesManager);
        }
        return getNonStaticNonCacheFields(objectClass, preferencesManager);
    }

    private IField[] getNonStaticNonCacheFields(IType objectClass, PreferencesManager preferencesManager)
            throws JavaModelException {
        Set<String> cacheFields = new HashSet<>();
        cacheFields.add(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.HASHCODE_CACHING_FIELD));
        cacheFields.add(preferencesManager.getCurrentPreferenceValue(JeneratePreferences.TOSTRING_CACHING_FIELD));

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

    @Override
    public IDialogSettings getDialogSettings() {
        return JeneratePlugin.getDefault().getDialogSettings();
    }

    @Override
    public LinkedHashMap<String, IJavaElement> getInsertPositions(IType objectClass, Set<IMethod> excludedMethods)
            throws JavaModelException {
        LinkedHashMap<String, IJavaElement> toReturn = new LinkedHashMap<>();

        IJavaElement[] members = filterOutExcludedElements(objectClass.getChildren(), excludedMethods).toArray(
                new IJavaElement[0]);
        IMethod[] methods = filterOutExcludedElements(objectClass.getMethods(), excludedMethods)
                .toArray(new IMethod[0]);

        toReturn.put(FIRST_METHOD_POSITION, methods.length > 0 ? methods[0] : null);
        toReturn.put(LAST_METHOD_POSITION, null);

        for (int i = 0; i < methods.length; i++) {
            IMethod curr = methods[i];
            String methodLabel = getMethodLabel(curr);
            toReturn.put("After " + methodLabel, findSibling(curr, members));
        }
        return toReturn;
    }

    private String getMethodLabel(final IMethod method) {
        StringBuffer result = new StringBuffer("`");

        String[] params = method.getParameterTypes();

        result.append(method.getElementName());
        result.append("(");
        for (int i = 0; i < params.length; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(Signature.toString(params[i]));
        }
        result.append(")`");

        return result.toString();
    }

    private Collection<IJavaElement> filterOutExcludedElements(IJavaElement[] src, Set<IMethod> excludedElements) {

        if (excludedElements == null || excludedElements.size() == 0) {
            return Arrays.asList(src);
        }

        Collection<IJavaElement> result = new ArrayList<>();
        for (int i = 0, size = src.length; i < size; i++) {
            if (!excludedElements.contains(src[i])) {
                result.add(src[i]);
            }
        }

        return result;
    }

    private IJavaElement findSibling(final IMethod curr, final IJavaElement[] members) throws JavaModelException {
        IJavaElement res = null;
        int methodStart = curr.getSourceRange().getOffset();
        for (int i = members.length - 1; i >= 0; i--) {
            IMember member = (IMember) members[i];
            if (methodStart >= member.getSourceRange().getOffset()) {
                return res;
            }
            res = member;
        }
        return null;
    }

}
