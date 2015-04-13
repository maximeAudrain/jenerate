package org.jenerate.internal.strategy.method.content.impl.commonslang;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import org.jenerate.internal.domain.data.EqualsHashCodeGenerationData;
import org.jenerate.internal.domain.identifier.impl.MethodContentStrategyIdentifier;
import org.jenerate.internal.strategy.method.skeleton.impl.EqualsMethodSkeleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Junit test for the {@link CommonsLangEqualsMethodContent}
 *
 * @author maudrain
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonsLangEqualsMethodContentTest extends
AbstractMethodContentTest<CommonsLangEqualsMethodContent, EqualsMethodSkeleton, EqualsHashCodeGenerationData> {

	@Override
	public EqualsHashCodeGenerationData getConcreteData() {
		return mock(EqualsHashCodeGenerationData.class);
	}

	@Override
	public void callbackAfterSetUp() throws Exception {
		when(data.getCompareReferences()).thenReturn(false);
		methodContent = new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG,
				preferencesManager);
	}

	@Test
	public void testGetMethodContentStrategyIdentifier() {
		assertEquals(MethodContentStrategyIdentifier.USE_COMMONS_LANG, methodContent.getStrategyIdentifier());
	}

	@Test
	public void testGetRelatedMethodSkeletonClass() {
		assertEquals(EqualsMethodSkeleton.class, methodContent.getRelatedMethodSkeletonClass());
	}

	@Test
	public void testGetLibrariesToImportWithCommonsLang() {
		Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
		assertEquals(1, librariesToImport.size());
		assertEquals(
				CommonsLangMethodContentLibraries
				.getEqualsBuilderLibrary(MethodContentStrategyIdentifier.USE_COMMONS_LANG),
				librariesToImport.iterator().next());
	}

	@Test
	public void testGetLibrariesToImportWithCommonsLang3() {
		methodContent = new CommonsLangEqualsMethodContent(MethodContentStrategyIdentifier.USE_COMMONS_LANG3,
				preferencesManager);
		Set<String> librariesToImport = methodContent.getLibrariesToImport(data);
		assertEquals(1, librariesToImport.size());
		assertEquals(
				CommonsLangMethodContentLibraries
				.getEqualsBuilderLibrary(MethodContentStrategyIdentifier.USE_COMMONS_LANG3),
				librariesToImport.iterator().next());
	}

	@Test
	public void testGetMethodContentDefault() throws Exception {
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if ( !(other instanceof Test) ) return false;Test castOther = (Test) other;\n"
				+ "return new EqualsBuilder().append(field1, castOther.field1)"
				+ ".append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithCompareReferences() throws Exception {
		when(data.getCompareReferences()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if (this == other) return true;if ( !(other instanceof Test) ) return false;"
				+ "Test castOther = (Test) other;\nreturn new EqualsBuilder()"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithClassComparison() throws Exception {
		when(data.getClassComparison()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if (other == null) return false;"
				+ "if ( !getClass().equals(other.getClass())) return false;"
				+ "Test castOther = (Test) other;\nreturn new EqualsBuilder()"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithCompareReferencesAndClassComparison() throws Exception {
		when(data.getCompareReferences()).thenReturn(true);
		when(data.getClassComparison()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if (this == other) return true;"
				+ "if (other == null) return false;"
				+ "if ( !getClass().equals(other.getClass())) return false;"
				+ "Test castOther = (Test) other;\nreturn new EqualsBuilder()"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithAppendSuper() throws Exception {
		when(data.getAppendSuper()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if ( !(other instanceof Test) ) return false;Test castOther = (Test) other;\n"
				+ "return new EqualsBuilder().appendSuper(super.equals(other))"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithUseBlocksInIfStatements() throws Exception {
		when(data.getUseBlockInIfStatements()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if ( !(other instanceof Test) ){\n return false;\n}\nTest castOther = (Test) other;\n"
				+ "return new EqualsBuilder().append(field1, castOther.field1)"
				+ ".append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithCompareReferencesAndUseBlocksInIfStatements() throws Exception {
		when(data.getCompareReferences()).thenReturn(true);
		when(data.getUseBlockInIfStatements()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if (this == other){\n return true;\n}\nif ( !(other instanceof Test) ){"
				+ "\n return false;\n}\nTest castOther = (Test) other;\nreturn new EqualsBuilder()"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}
	@Test
	public void testGetMethodContentWithClassComparisonAndUseBlocksInIfStatements() throws Exception {
		when(data.getClassComparison()).thenReturn(true);
		when(data.getUseBlockInIfStatements()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if (other == null){\n return false;\n}\n"
				+ "if ( !getClass().equals(other.getClass())){\n return false;\n}\n"
				+ "Test castOther = (Test) other;\nreturn new EqualsBuilder()"
				+ ".append(field1, castOther.field1).append(field2, castOther.field2).isEquals();\n", content);
	}

	@Test
	public void testGetMethodContentWithUseGettersInsteadOfFields() throws Exception {
		when(data.getUseGettersInsteadOfFields()).thenReturn(true);
		String content = methodContent.getMethodContent(objectClass, data);
		assertEquals("if ( !(other instanceof Test) ) return false;Test castOther = (Test) other;\n"
				+ "return new EqualsBuilder().append(isField1(), castOther.isField1())"
				+ ".append(getField2(), castOther.getField2()).isEquals();\n", content);
	}
}
