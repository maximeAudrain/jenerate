package org.jenerate.internal.ui.dialogs.factory.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.jenerate.internal.domain.data.MethodGenerationData;
import org.jenerate.internal.domain.preference.impl.JeneratePreference;
import org.jenerate.internal.manage.PreferencesManager;
import org.jenerate.internal.ui.dialogs.FieldDialog;
import org.jenerate.internal.ui.dialogs.factory.DialogFactory;

public abstract class AbstractDialogFactory<T extends FieldDialog<U>, U extends MethodGenerationData> implements
        DialogFactory<T, U> {

    protected final PreferencesManager preferencesManager;

    public AbstractDialogFactory(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
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
    protected boolean isOverriddenInSuperclass(IType objectClass, String methodName,
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

    protected IField[] getObjectClassFields(IType objectClass) throws JavaModelException {
        boolean displayFieldsOfSuperClasses = ((Boolean) preferencesManager
                .getCurrentPreferenceValue(JeneratePreference.DISPLAY_FIELDS_OF_SUPERCLASSES)).booleanValue();
        if (displayFieldsOfSuperClasses) {
            return getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(objectClass);
        }
        return getNonStaticNonCacheFields(objectClass);
    }

    private IField[] getNonStaticNonCacheFields(IType objectClass) throws JavaModelException {
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

    private IField[] getNonStaticNonCacheFieldsAndAccessibleNonStaticFieldsOfSuperclasses(IType objectClass)
            throws JavaModelException {
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

        result.addAll(Arrays.asList(getNonStaticNonCacheFields(objectClass)));

        return result.toArray(new IField[result.size()]);
    }

}
