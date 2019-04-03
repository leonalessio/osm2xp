package com.osm2xp.translators.xplane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.model.facades.SpecialFacadeType;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.xplane.XplaneDsf3DObject;
import com.osm2xp.model.xplane.XplaneDsfObject;
import com.osm2xp.stats.CountStats;
import com.osm2xp.stats.StatsProvider;
import com.osm2xp.translators.BuildingType;
import com.osm2xp.translators.IPolyHandler;
import com.osm2xp.translators.ITranslationListener;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.MiscUtils;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.writers.IHeaderedWriter;

import math.geom2d.Box2D;
import math.geom2d.Point2D;

public class XPlaneTranslatorImpl implements ITranslator{

	private static final int MIN_SPEC_BUILDING_PERIMETER = 30;
	private static final String BUILDING_TAG = "building";
	/**
	 * Residential buildings maximum area.
	 */
	private static final double ASSERTION_RESIDENTIAL_MAX_AREA = 0.5;
	/**
	 * Buildings minimum vectors.
	 */
	protected static final int BUILDING_MIN_VECTORS = 3;
	/**
	 * Buildings maximum vectors.
	 */
	protected static final int BUILDING_MAX_VECTORS = 512;	
	/**
	 * current lat/long tile.
	 */
	protected Point2D currentTile;
	/**
	 * Line separator
	 */
	public static final String LINE_SEP = System.getProperty("line.separator");
	
	/**
	 * file writer.
	 */
	protected IHeaderedWriter writer;
	/**
	 * start time.
	 */
	protected Date startTime;
	/**
	 * generated scenery folder path.
	 */
	protected String folderPath;
	/**
	 * dsf object provider.
	 */
	protected DsfObjectsProvider dsfObjectsProvider;
	
	protected XPForestTranslator forestTranslator;
	
	protected ITranslationListener translationListener;
	
	protected XPOutputFormat outputFormat;
	/**
	 * Building level height, 3 m by default 
	 */
	protected double levelHeight = GlobalOptionsProvider.getOptions().getLevelHeight();
	protected List<IPolyHandler> polyHandlers = new ArrayList<IPolyHandler>();

	public XPlaneTranslatorImpl(IHeaderedWriter writer,
			Point2D currentTile, String folderPath,
			DsfObjectsProvider dsfObjectsProvider) {
		this.currentTile = currentTile;
		this.writer = writer;
		this.folderPath = folderPath;
		this.dsfObjectsProvider = dsfObjectsProvider;
		this.startTime = new Date();
		
		outputFormat = createOutputFormat();
		
		IDRenumbererService idProvider = new IDRenumbererService();
		
		polyHandlers.add(new XPBarrierTranslator(writer, dsfObjectsProvider, outputFormat));
		polyHandlers.add(new XPRoadTranslator(writer, idProvider, outputFormat));
		polyHandlers.add(new XPRailTranslator(writer, idProvider, outputFormat));
		polyHandlers.add(new XPPowerlineTranslator(writer, idProvider, outputFormat));
		polyHandlers.add(new XPCoolingTowerTranslator(writer, dsfObjectsProvider));
		polyHandlers.add(new XPChimneyTranslator(writer, dsfObjectsProvider));
		polyHandlers.add(new XPDrapedPolyTranslator(writer, dsfObjectsProvider, outputFormat));
		forestTranslator = new XPForestTranslator(writer, dsfObjectsProvider, outputFormat);
		
	}

	protected XPOutputFormat createOutputFormat() {
		return new XPOutputFormat();
	}
	
	@Override
	public void init() {
		// writer initialization
		writer.init(currentTile);
		writer.setHeader(outputFormat.getHeaderString(currentTile, null, dsfObjectsProvider));
	}
	
	@Override
	public void complete() {
		for (IPolyHandler polyHandler : polyHandlers) {
			polyHandler.translationComplete();
		}
		writer.complete();
		CountStats tileStats = StatsProvider.getTileStats(currentTile, false);
		if (tileStats != null) {
			System.out.println("Tile " + currentTile + ", generated: " + tileStats.getSummary().toLowerCase());
		} else {
			System.out.println("No generation stats available");
		}
		if (translationListener != null) {
			translationListener.complete();
		}
	}	

	protected String getExclusionsStr() {
		// Override if necessary
		return null;
	}

	/**
	 * write a building in the dsf file.
	 * 
	 * @param polygon
	 * @param facade
	 * @param size
	 */
	protected void writeBuildingToDsf(OsmPolygon osmPolygon, Integer facade) {
		if (facade != null && osmPolygon.getHeight() != null) {
			osmPolygon.setPolygon(GeomUtils.setCCW(osmPolygon.getPolygon()));
			double area = osmPolygon.getPolygon().area();
			if (area < 0) {
				Osm2xpLogger.error("Building polygon winding is incorrect! Polygon:" + osmPolygon.getId());
			}
			writer.write(outputFormat.getPolygonString(osmPolygon, facade + "", osmPolygon.getHeight() + ""));
		}
	}

	protected String getBuildingComment(OsmPolygon osmPolygon, Integer facade) {
		String street = osmPolygon.getTagValue("addr:street");
		String name = osmPolygon.getTagValue("name");
		String number = osmPolygon.getTagValue("addr:housenumber");
		StringBuilder builder = new StringBuilder(BUILDING_TAG);
		
		if (name != null) {
			builder.append(" ");
			builder.append(name);
		}
		if (street != null) {
			builder.append(" ");
			builder.append(street);
		}
		if (number != null) {
			builder.append(" ");
			builder.append(number);
		}
		builder.append(" facade ");
		builder.append(facade);
		return builder.toString();
	}

	/**
	 * Compute the height for this polygon and osm tags.
	 * 
	 * 
	 * @return Integer the height.
	 */
	protected Integer computeBuildingHeight(OsmPolygon polygon) {
		Integer result = null;
		Integer osmHeight = polygon.getHeight();
		if (osmHeight != null) {
			result = osmHeight;
		} else {
			String value = polygon.getTagValue("building:levels");
			if (value != null) {
				try {
					int levels = Integer.parseInt(value);
					return (int) Math.round(levels * levelHeight);
				} catch (NumberFormatException e) {
					//Ignore. Should we log it?
				}
				
			}
			int height = tryGetHeightByType(polygon);
			if (height > 0) {
				return height;
			}
			
			//TODO hacky conditions here, should use smth more intelligent or even Neural Net for this
			double perimeter = GeomUtils.computeEdgesLength(polygon.getPolygon());
			if (perimeter <= 30) {
				return (int) Math.round(levelHeight);
			}
			if (perimeter <= 50) {
				return (int) Math.round(levelHeight * 2);
			}
			
			if (polygon.getArea() * 10000000 < 0.2) {
				result = XPlaneOptionsProvider.getOptions().getResidentialMin();
			} else {
				if (polygon.getArea() * 10000000 > ASSERTION_RESIDENTIAL_MAX_AREA)
					result = MiscUtils.getRandomSize(XPlaneOptionsProvider
							.getOptions().getBuildingMin(), XPlaneOptionsProvider
							.getOptions().getBuildingMax());
				else if (polygon.getArea() * 10000000 < ASSERTION_RESIDENTIAL_MAX_AREA)
					result = MiscUtils.getRandomSize(XPlaneOptionsProvider
							.getOptions().getResidentialMin(),
							XPlaneOptionsProvider.getOptions()
									.getResidentialMax());
			}
		}
		return result;
	}	

	/**
	 * Guess height by tags, for buiildings like e.g. garages
	 * @param polygon
	 * @return
	 */
	protected int tryGetHeightByType(OsmPolygon polygon) {
		SpecialFacadeType specialType = getSpecialBuildingType(polygon);
		if (specialType == SpecialFacadeType.GARAGE) { //Garages are 1 level high by default
			return (int) Math.round(levelHeight);
		} else if (specialType == SpecialFacadeType.TANK) { //Tanks height == diameter, if not specified, 2 * diameter for gasometers			
			double length = GeomUtils.computeEdgesLength(polygon.getPolyline());
			int diameter = (int) Math.round(length / Math.PI);
			if ("gasometer".equalsIgnoreCase(polygon.getTagValue(OsmConstants.MAN_MADE_TAG))) {
				return diameter * 2;
			}
			return diameter;
		}
		return 0;
	}

	protected BuildingType getBuildingType(OsmPolygon polygon) {
		String typeStr = polygon.getTagValue(BUILDING_TAG);
		BuildingType type = BuildingType.fromId(typeStr);
		if (type != null) {
			return type;
		}
		if ("apartments".equals(typeStr)) {
			return BuildingType.RESIDENTIAL;
		}
		// first, do we are on a residential object?
		// yes if there is residential or house tag
		// or if surface of the polygon is under the max surface for a
		// residential house
		// and height is under max residential height
		if (OsmUtils.isValueInTags("residential", polygon.getTags())
				|| OsmUtils.isValueInTags("house", polygon.getTags())) {
			return BuildingType.RESIDENTIAL;
		}
		if (!StringUtils.stripToEmpty(polygon.getTagValue("shop")).isEmpty()) {
			return BuildingType.COMMERCIAL;
		}
		if (polygon.getArea() * 10000000 < ASSERTION_RESIDENTIAL_MAX_AREA
			&& polygon.getHeight() < XPlaneOptionsProvider.getOptions()
					.getResidentialMax()) {
			return BuildingType.RESIDENTIAL;
		}
		// do we are on a building object?
		// yes if there is industrial or Commercial tag
		// or if surface of the polygon is above the max surface for a
		// residential house
		// and height is above max residential height
		if (OsmUtils.isValueInTags("industrial", polygon.getTags())
				|| OsmUtils.isValueInTags("Commercial", polygon.getTags())
				|| polygon.getArea() * 10000000 > ASSERTION_RESIDENTIAL_MAX_AREA
				|| polygon.getHeight() > XPlaneOptionsProvider.getOptions()
						.getResidentialMax()) {
			return BuildingType.INDUSTRIAL;
		}
		return BuildingType.RESIDENTIAL;
	}
	
	/**
	 * Compute the facade index for given osm tags, polygon and height.
	 * 
	 * @param tags
	 * @param polygon
	 * @param height
	 * @return Integer the facade index.
	 */
	public Integer computeFacadeIndex(OsmPolygon polygon) {
		StatsProvider.getTileStats(currentTile, true).incCount("building");
		Integer result = null;
		int index = dsfObjectsProvider.computeFacadeIndexFromRules(polygon);
		if (index >= 0) {
			return index;
		}
		SpecialFacadeType specialFacadeType = getSpecialBuildingType(polygon);
		if (specialFacadeType != null) {
			StatsProvider.getTileStats(currentTile, true).incCount(specialFacadeType.name());
			return dsfObjectsProvider.computeSpecialFacadeDsfIndex(specialFacadeType, polygon);
		}
		// we check if we can use a sloped roof if the user wants them
		BuildingType buildingType = getBuildingType(polygon);
		StatsProvider.getTileStats(currentTile, true).incCount(buildingType.name());
		if (XPlaneOptionsProvider.getOptions().isGenerateSlopedRoofs()
				&& polygon.isSimplePolygon() && polygon.getHeight() < 20) { //Suggesting that buildings higher than 20m usually have flat roofs
			
			result = dsfObjectsProvider.computeFacadeDsfIndex(true, buildingType, true,
					polygon);
		}
		// no sloped roof, so we'll use a standard house facade
		else {
	
			// if the polygon is a simple rectangle, we'll use a facade made for
			// simple shaped buildings
			if (polygon.getPolygon().edgeNumber() == 4) {
				result = dsfObjectsProvider.computeFacadeDsfIndex(true, buildingType,
						false, polygon);
			}
			// the building has a complex footprint, so we'll use a facade made
			// for this case
			else {
				result = dsfObjectsProvider.computeFacadeDsfIndex(false, buildingType,
						false, polygon);
			}
		}
		return result;
	}

	/**
	 * Special building, which should use special facade. Only storage tanks/gasometers for now
	 * @param polygon Polygon to check 
	 * @return resulting type or <code>null</code> if this polygon soes not present special building
	 */
	private SpecialFacadeType getSpecialBuildingType(OsmPolygon polygon) {
		if (XPlaneOptionsProvider.getOptions().isGenerateTanks()) { 
			String manMade = polygon.getTagValue(OsmConstants.MAN_MADE_TAG);
			if ("storage_tank".equals(manMade) || "fuel_storage_tank".equals(manMade) || "gasometer".equals(manMade)) {
				return SpecialFacadeType.TANK;
			}
		}
		if ("garages".equals(polygon.getTagValue(BUILDING_TAG)) || "garage".equals(polygon.getTagValue(BUILDING_TAG))) { //For now - we always generate garages if we generate buildings 
			return SpecialFacadeType.GARAGE;
		}
		return null;
	}

	/**
	 * write a 3D object in the dsf file
	 * 
	 * @param object
	 *            a xplane dsf object
	 * @throws Osm2xpBusinessException
	 */
	protected void writeObjectToDsf(XplaneDsfObject object) throws Osm2xpBusinessException {
	
		String objectDsfText = object.asObjDsfText();
		writer.write(objectDsfText);

	}

	@Override
	public void processBoundingBox(Box2D bbox) {
		if (bbox != null && XPlaneOptionsProvider.getOptions().isExclusionsFromInput()) {
//			Box2D bboxRect = new Box2D(bbox.getMinX() / COORD_DIV_FACTOR,bbox.getMaxX() / COORD_DIV_FACTOR, bbox.getMinY() / COORD_DIV_FACTOR, bbox.getMaxY() / COORD_DIV_FACTOR);
			if (currentTile != null) {
				Box2D tileRect = new Box2D(currentTile, 1,1);
				writer.setHeader(outputFormat.getHeaderString(currentTile, tileRect.intersection(bbox), dsfObjectsProvider));
			} else {
				writer.setHeader(outputFormat.getHeaderString(currentTile, bbox, dsfObjectsProvider));
			}
		}
	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
		// process the node if we're on a single pass mode.
		// if not on single pass, only process if the node is on the current
		// lat/long tile
		if (XPlaneOptionsProvider.getOptions().isGenerateObj() && GeomUtils.compareCoordinates(currentTile, node)) {
			// write a 3D object in the dsf file if this node is in an
			// object
			// rule
			XplaneDsf3DObject object = dsfObjectsProvider
					.getRandomDsfObjectIndexAndAngle(node.getTags(),
							node.getId());
			if (object != null) {
				List<Node> nodes = new ArrayList<Node>();
				nodes.add(node);
				object.setPolygon(new OsmPolygon(node.getId(), node
						.getTags(), nodes, false));
				writeObjectToDsf(object);
			}
		}
	}

	@Override
	public void processPolyline(OsmPolyline osmPolyline) throws Osm2xpBusinessException {
		// polygon is null or empty don't process it
		if (osmPolyline.getNodes() != null && !osmPolyline.getNodes().isEmpty()) {
			List<OsmPolyline> polylines = preprocess(osmPolyline);
			// try to transform those polygons into dsf objects.
			for (OsmPolyline poly : polylines) {
				
				// look for light rules
				processLightObject(poly);
				//Try processing by registered handlers first - they ususally have more concrete rules
				if (!processByHandlers(poly))
				{	
					// try to generate a 3D object
					if (!process3dObject(poly)) {
						// nothing generated? try to generate a facade building.
						if (!processBuilding(poly)) {
							// nothing generated? try to generate a forest.
							if (forestTranslator.handlePoly(poly) && translationListener != null) {
								translationListener.processForest((OsmPolygon) poly);
								StatsProvider.getTileStats(currentTile, true).incCount(forestTranslator.getId());
							} 
						}
					}
				}
			}
		}
	}

	protected List<OsmPolyline> preprocess(OsmPolyline osmPolyline) {
		if (osmPolyline instanceof OsmPolygon) {
			// polygon MUST be in clockwise order
			((OsmPolygon) osmPolyline).setPolygon(GeomUtils.forceCCW(((OsmPolygon) osmPolyline)
					.getPolygon()));
			
	//			if (!GeomUtils.isValid(osmPolygon.getPolygon())) {
	//				System.out.println("XPlaneTranslatorImpl.processPolygon()");
	//			}
			// if we're on a single pass mode
			// here we must check if the polygon is on more than one tile
			// if that's the case , we must split it into several polys
//			List<OsmPolyline> polygons = new ArrayList<>(); //TODO we shoudn't need to split it here anymore. Translator should get "clean" polygons - already splitted & clipped to tile size 
//			if (GlobalOptionsProvider.getOptions().isSinglePass()) {
//				polygons.addAll(osmPolyline.splitPolygonAroundTiles());
//			}
			// if not on a single pass mode, add this single polygon to the poly
			// list
//			else {
//				polygons.add(osmPolyline);
//			}
//			return polygons;
		}
		return Collections.singletonList(osmPolyline);
	}
	
	/**
	 * choose and write a 3D object in the dsf file.
	 * 
	 * @param polygon
	 *            osm polygon.
	 * @return true if a 3D object has been written in the dsf file.
	 */
	protected boolean process3dObject(OsmPolyline poly) {

		if (!(poly instanceof OsmPolygon) || poly.isPart() || !((OsmPolygon) poly).getPolygon().isClosed()) {
			return false;
		}
		
		if (XPlaneOptionsProvider.getOptions().isGenerateObj()) {
			// simplify shape if checked and if necessary
			if (GlobalOptionsProvider.getOptions().isSimplifyShapes()
					&& !((OsmPolygon) poly).isSimplePolygon()) {
				poly = ((OsmPolygon) poly).toSimplifiedPoly();
			}
			XplaneDsfObject object = dsfObjectsProvider
					.getRandomDsfObject((OsmPolygon) poly);
			if (object != null) {
				object.setPolygon((OsmPolygon) poly);
				try {
					writeObjectToDsf(object);
					StatsProvider.getTileStats(currentTile, true).incCount("object");
					return true;
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.error(e.getMessage(), e);
				}

			}
		}
		return false;
	}
	
	/**
	 * Construct and write a facade building in the dsf file.
	 * 
	 * @param osmPolygon
	 *            osm polygon
	 * @return true if a building has been gennerated in the dsf file.
	 */
	protected boolean processBuilding(OsmPolyline polyline) {
		if (!(polyline instanceof OsmPolygon)) {
			return false;
		}
		OsmPolygon osmPolygon = (OsmPolygon) polyline;
		if (XPlaneOptionsProvider.getOptions().isGenerateBuildings()
				&& OsmUtils.isBuilding(osmPolygon.getTags())
				&& !OsmUtils.isExcluded(osmPolygon.getTags(),
						osmPolygon.getId())
				&& !specialExcluded(osmPolygon)				
				&& osmPolygon.isValid()
				&& osmPolygon.getPolygon().vertexNumber() > BUILDING_MIN_VECTORS
				&& osmPolygon.getPolygon().vertexNumber() < BUILDING_MAX_VECTORS) {

			// check that the largest vector of the building
			// and that the area of the osmPolygon.getPolygon() are over the
			// minimum values set by the user
			Double maxVector = osmPolygon.getMaxVectorSize();
			if (maxVector > XPlaneOptionsProvider.getOptions()
					.getMinHouseSegment()
					&& maxVector < XPlaneOptionsProvider.getOptions()
							.getMaxHouseSegment()
					&& ((osmPolygon.getPolygon().area() * 100000) * 100000) > XPlaneOptionsProvider
							.getOptions().getMinHouseArea()) {

				// simplify shape if checked and if necessary
				if (GlobalOptionsProvider.getOptions().isSimplifyShapes()
						&& !osmPolygon.isSimplePolygon()) {
					osmPolygon = osmPolygon.toSimplifiedPoly();
				}

				// compute height and facade dsf index
				osmPolygon.setHeight(computeBuildingHeight(osmPolygon));
				Integer facade = computeFacadeIndex(osmPolygon);
				if (translationListener != null) {
					translationListener.processBuilding(osmPolygon, facade);
				}
				// write building in dsf file
				writeBuildingToDsf(osmPolygon, facade);
				return true;
			}
		}
		return false;
	}

	protected boolean specialExcluded(OsmPolygon osmPolygon) {
		if (getSpecialBuildingType(osmPolygon) != null) {
			return GeomUtils.computeEdgesLength(osmPolygon.getPolygon()) < MIN_SPEC_BUILDING_PERIMETER; 	//This check is needed to avoid generating a bunch of little garages, tanks etc.
		}
		String val = osmPolygon.getTagValue(OsmConstants.MAN_MADE_TAG);
		//We don't want to generate facade-based building for most of "man_made"-typed objects, since usually using facade for them is not a good idea.
		//Exclusions are e.g. storage tanks or works/factories
		//Please see https://wiki.openstreetmap.org/wiki/Key:man_made for examples
		if (val != null && 
				val.indexOf("tank") == -1 && 
				!"yes".equals(val) &&
				!"gasometer".equals(val) &&
				!"works".equals(val)) {  
			return true;
		}
		return false;
	}

	protected boolean processByHandlers(OsmPolyline poly) {
		for (IPolyHandler handler : polyHandlers) {
			if (handler.handlePoly(poly)) {
				if (translationListener != null) {
					translationListener.polyProcessed(poly, handler);
				}
				StatsProvider.getTileStats(currentTile,true).incCount(handler.getId());
				if (handler.isTerminating()) {
					return true;
				}
			}
		}
		return false;
	}

	private void processLightObject(OsmPolyline poly) {
		if (poly instanceof OsmPolygon && XPlaneOptionsProvider.getOptions().isGenerateLights()) {
			XplaneDsfObject object = dsfObjectsProvider
					.getRandomDsfLightObject((OsmPolygon) poly);
			if (object != null) {
				object.setPolygon((OsmPolygon) poly);
				try {
					writeObjectToDsf(object);
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.log(e);
				}
			}
		}
	}

	@Override
	public boolean mustStoreNode(Node node) {
		Boolean result = true;
//		if (!GlobalOptionsProvider.getOptions().isSinglePass()) { //XXX debug
//			result = GeomUtils.compareCoordinates(currentTile, node);
//		}
		return result;
	}


	@Override
	public boolean mustProcessPolyline(List<Tag> tags) {
		return (OsmUtils.isBuilding(tags) || OsmUtils.isForest(tags) || OsmUtils
				.isObject(tags) || OsmUtils.isRailway(tags) || OsmUtils.isRoad(tags) || OsmUtils.isPowerline(tags) || OsmUtils.isFence(tags) || OsmUtils.isAeroway(tags));
	}

	public void setTranslationListener(ITranslationListener translationListener) {
		this.translationListener = translationListener;
	}
	
	@Override
	public int getMaxHoleCount(List<Tag> tags) {
		if (OsmUtils.isBuilding(tags)) {
			return 0; //No holes for building supported
		}
		if (OsmUtils.isForest(tags)) {
			return 254; //255 windings at most - 1 outer winding = 254
		}
		return Integer.MAX_VALUE; 
	}

}