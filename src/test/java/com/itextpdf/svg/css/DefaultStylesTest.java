package com.itextpdf.svg.css;

import com.itextpdf.styledxmlparser.css.ICssResolver;
import com.itextpdf.styledxmlparser.jsoup.nodes.Element;
import com.itextpdf.styledxmlparser.jsoup.parser.Tag;
import com.itextpdf.styledxmlparser.node.INode;
import com.itextpdf.styledxmlparser.node.impl.jsoup.node.JsoupElementNode;
import com.itextpdf.svg.SvgConstants;
import com.itextpdf.svg.css.impl.SvgStyleResolver;
import com.itextpdf.svg.dummy.sdk.ExceptionInputStream;
import com.itextpdf.svg.exceptions.SvgLogMessageConstant;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.LogMessage;
import com.itextpdf.test.annotations.LogMessages;
import com.itextpdf.test.annotations.type.UnitTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

@Category(UnitTest.class)
public class DefaultStylesTest extends ExtendedITextTest {

    @Rule
    public ExpectedException junitExpectedException = ExpectedException.none();

    @Test
    public void checkDefaultStrokeValuesTest() {
        ICssResolver styleResolver = new SvgStyleResolver();
        Element svg = new Element(Tag.valueOf("svg"), "");
        INode svgNode = new JsoupElementNode(svg);
        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode, null);

        Assert.assertEquals("1", resolvedStyles.get(SvgConstants.Attributes.STROKE_OPACITY));
        Assert.assertEquals("1px", resolvedStyles.get(SvgConstants.Attributes.STROKE_WIDTH));
        Assert.assertEquals(SvgConstants.Values.NONE, resolvedStyles.get(SvgConstants.Attributes.STROKE));
        Assert.assertEquals(SvgConstants.Values.BUTT, resolvedStyles.get(SvgConstants.Attributes.STROKE_LINECAP));
        Assert.assertEquals("0", resolvedStyles.get(SvgConstants.Attributes.STROKE_DASHOFFSET));
        Assert.assertEquals(SvgConstants.Values.NONE, resolvedStyles.get(SvgConstants.Attributes.STROKE_DASHARRAY));
        Assert.assertEquals("4", resolvedStyles.get(SvgConstants.Attributes.STROKE_MITERLIMIT));
    }

    @Test
    public void checkDefaultFillValuesTest() {
        ICssResolver styleResolver = new SvgStyleResolver();
        Element svg = new Element(Tag.valueOf("svg"), "");
        INode svgNode = new JsoupElementNode(svg);
        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode, null);

        Assert.assertEquals("black", resolvedStyles.get(SvgConstants.Attributes.FILL));
        Assert.assertEquals(SvgConstants.Values.FILL_RULE_NONZERO, resolvedStyles.get(SvgConstants.Attributes.FILL_RULE));
        Assert.assertEquals("1", resolvedStyles.get(SvgConstants.Attributes.FILL_OPACITY));
    }

    @Test
    public void checkDefaultFontValuesTest() {
        ICssResolver styleResolver = new SvgStyleResolver();
        Element svg = new Element(Tag.valueOf("svg"), "");
        INode svgNode = new JsoupElementNode(svg);
        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode, null);

        Assert.assertEquals("helvetica", resolvedStyles.get(SvgConstants.Attributes.FONT_FAMILY));
        Assert.assertEquals("12px", resolvedStyles.get(SvgConstants.Attributes.FONT_SIZE));
    }

    @Test
    public void emptyStreamTest() throws IOException {
        ICssResolver styleResolver = new SvgStyleResolver(new ByteArrayInputStream(new byte[]{}));
        Element svg = new Element(Tag.valueOf("svg"), "");
        INode svgNode = new JsoupElementNode(svg);
        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode, null);

        Assert.assertEquals(0, resolvedStyles.size());
    }

    @Test
    public void emptyStylesFallbackTest() throws IOException {
        junitExpectedException.expect(IOException.class);
        new SvgStyleResolver(new ExceptionInputStream());
    }

    @Test
    public void overrideDefaultStyleTest() {
        ICssResolver styleResolver = new SvgStyleResolver();
        Element svg = new Element(Tag.valueOf("svg"), "");
        svg.attributes().put(SvgConstants.Attributes.STROKE, "white");
        INode svgNode = new JsoupElementNode(svg);
        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode, null);

        Assert.assertEquals("white", resolvedStyles.get(SvgConstants.Attributes.STROKE));
    }

    @Test
    @Ignore("RND-1089")
    public void inheritedDefaultStyleTest() {
        ICssResolver styleResolver = new SvgStyleResolver();
        Element svg = new Element(Tag.valueOf("svg"), "");
        Element circle = new Element(Tag.valueOf("circle"), "");
        INode svgNode = new JsoupElementNode(svg);
        svgNode.addChild(new JsoupElementNode(circle));

        Map<String, String> resolvedStyles = styleResolver.resolveStyles(svgNode.childNodes().get(0), null);

        Assert.assertEquals("black", resolvedStyles.get(SvgConstants.Attributes.STROKE));
    }
}