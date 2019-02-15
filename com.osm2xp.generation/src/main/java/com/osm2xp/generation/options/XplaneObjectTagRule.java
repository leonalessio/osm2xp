package com.osm2xp.generation.options;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;

/**
 * XplaneObjectTagRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XplaneObjectTagRule", propOrder = { "angle", "randomAngle",
		"polygonAngle", "sizeCheck", "xVectorMinLength", "xVectorMaxLength",
		"yVectorMinLength", "yVectorMaxLength", "areaCheck", "minArea",
		"maxArea", "simplePolygonOnly", "usePolygonAngle", "rotationPointX",
		"rotationPointY" })
public class XplaneObjectTagRule extends TagsRule {

	protected int angle;
	protected boolean randomAngle;
	protected boolean polygonAngle;
	protected boolean sizeCheck;
	protected int xVectorMinLength;
	protected int xVectorMaxLength;
	protected int yVectorMinLength;
	protected int yVectorMaxLength;
	protected boolean areaCheck;
	protected int minArea;
	protected int maxArea;
	protected boolean simplePolygonOnly;
	protected boolean usePolygonAngle;
	protected int rotationPointX;
	protected int rotationPointY;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public XplaneObjectTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public XplaneObjectTagRule(final Tag tag,
			final List<ObjectFile> objectsFiles, final int angle,
			final boolean randomAngle, final boolean polygonAngle,
			final boolean sizeCheck, final int xVectorMaxLength,
			final int xVectorMinLength, final int yVectorMinLength,
			final int yVectorMaxLength, final boolean areaCheck,
			final int minArea, final int maxArea,
			final boolean simplePolygonOnly, final boolean usePolygonAngle) {
		super(tag, objectsFiles);
		this.angle = angle;
		this.randomAngle = randomAngle;
		this.polygonAngle = polygonAngle;
		this.sizeCheck = sizeCheck;
		this.xVectorMinLength = xVectorMinLength;
		this.yVectorMaxLength = yVectorMaxLength;
		this.xVectorMaxLength = xVectorMaxLength;
		this.yVectorMinLength = yVectorMinLength;
		this.areaCheck = areaCheck;
		this.minArea = minArea;
		this.maxArea = maxArea;
		this.simplePolygonOnly = simplePolygonOnly;
		this.usePolygonAngle = usePolygonAngle;
		this.rotationPointX = 50;
		this.rotationPointY = 50;
	}

	/**
	 * Gets the value of the angle property.
	 * 
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * Sets the value of the angle property.
	 * 
	 */
	public void setAngle(int value) {
		this.angle = value;
	}

	/**
	 * Gets the value of the randomAngle property.
	 * 
	 */
	public boolean isRandomAngle() {
		return randomAngle;
	}

	/**
	 * Sets the value of the randomAngle property.
	 * 
	 */
	public void setRandomAngle(boolean value) {
		this.randomAngle = value;
	}

	/**
	 * Gets the value of the polygonAngle property.
	 * 
	 */
	public boolean isPolygonAngle() {
		return usePolygonAngle;
	}

	/**
	 * Sets the value of the polygonAngle property.
	 * 
	 */
	public void setPolygonAngle(boolean value) {
		this.polygonAngle = value;
	}

	/**
	 * Gets the value of the sizeCheck property.
	 * 
	 */
	public boolean isSizeCheck() {
		return sizeCheck;
	}

	/**
	 * Sets the value of the sizeCheck property.
	 * 
	 */
	public void setSizeCheck(boolean value) {
		this.sizeCheck = value;
	}

	/**
	 * Gets the value of the areaCheck property.
	 * 
	 */
	public boolean isAreaCheck() {
		return areaCheck;
	}

	/**
	 * Sets the value of the areaCheck property.
	 * 
	 */
	public void setAreaCheck(boolean value) {
		this.areaCheck = value;
	}

	/**
	 * Gets the value of the minArea property.
	 * 
	 */
	public int getMinArea() {
		return minArea;
	}

	/**
	 * Sets the value of the minArea property.
	 * 
	 */
	public void setMinArea(int value) {
		this.minArea = value;
	}

	/**
	 * Gets the value of the maxArea property.
	 * 
	 */
	public int getMaxArea() {
		return maxArea;
	}

	/**
	 * Sets the value of the maxArea property.
	 * 
	 */
	public void setMaxArea(int value) {
		this.maxArea = value;
	}

	/**
	 * Gets the value of the simplePolygonOnly property.
	 * 
	 */
	public boolean isSimplePolygonOnly() {
		return simplePolygonOnly;
	}

	/**
	 * Sets the value of the simplePolygonOnly property.
	 * 
	 */
	public void setSimplePolygonOnly(boolean value) {
		this.simplePolygonOnly = value;
	}

	/**
	 * Gets the value of the usePolygonAngle property.
	 * 
	 */
	public boolean isUsePolygonAngle() {
		return usePolygonAngle;
	}

	/**
	 * Sets the value of the usePolygonAngle property.
	 * 
	 */
	public void setUsePolygonAngle(boolean value) {
		this.usePolygonAngle = value;
	}

	public int getRotationPointX() {
		return rotationPointX;
	}

	public void setRotationPointX(int rotationPointX) {
		this.rotationPointX = rotationPointX;
	}

	public int getRotationPointY() {
		return rotationPointY;
	}

	public void setRotationPointY(int rotationPointY) {
		this.rotationPointY = rotationPointY;
	}

	public int getxVectorMinLength() {
		return xVectorMinLength;
	}

	public void setxVectorMinLength(int xVectorMinLength) {
		this.xVectorMinLength = xVectorMinLength;
	}

	public int getxVectorMaxLength() {
		return xVectorMaxLength;
	}

	public void setxVectorMaxLength(int xVectorMaxLength) {
		this.xVectorMaxLength = xVectorMaxLength;
	}

	public int getyVectorMinLength() {
		return yVectorMinLength;
	}

	public void setyVectorMinLength(int yVectorMinLength) {
		this.yVectorMinLength = yVectorMinLength;
	}

	public int getyVectorMaxLength() {
		return yVectorMaxLength;
	}

	public void setyVectorMaxLength(int yVectorMaxLength) {
		this.yVectorMaxLength = yVectorMaxLength;
	}

}
