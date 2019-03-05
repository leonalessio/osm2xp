package com.osm2xp.classification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.osm2xp.classification.index.KdTree;
import com.osm2xp.classification.index.PointData;
import com.osm2xp.classification.learning.ModelGenerator;
import com.osm2xp.classification.output.ARFFWriter;
import com.osm2xp.classification.output.CSVWithAdditionalsWriter;
import com.osm2xp.classification.output.StringDelimitedWriter;
import com.osm2xp.classification.parsing.LearningDataParser;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.lmt.LogisticBase;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class App {

	private static final int NEIGHBOUR_COUNT = 4;

	public static void main(String[] args) {
		buildGeoindex();
//		buildDataset();
//		buildClassifier();
	}

	protected static void buildDataset() {
		LearningDataParser parser = new LearningDataParser(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
		try (StringDelimitedWriter<WayBuildingData> writer = new ARFFWriter<WayBuildingData>(new File("type_ways.arff"), "type")) {
			List<WayBuildingData> typeWays = parser.getTypeWays();
			for (WayBuildingData wayBuildingData : typeWays) {
				writer.write(wayBuildingData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void buildGeoindex() {
		LearningDataParser parser = new LearningDataParser(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
//		VPTreeGeospatialIndex<BuildingPoint> geospatialIndex = new VPTreeGeospatialIndex<>(new EquiRectDistanceFunction());
		List<WayBuildingData> typeWays = parser.getTypeWays();
//		STRtree tree = new STRtree(typeWays.size());
//		int i = 0;
//		for (WayBuildingData wayBuildingData : typeWays) {
//			i++;
//			if (i % 50000 == 0) {
//				System.out.println("Added " + i + " points");
//			}
//			Box2D bbox = wayBuildingData.getBoundingBox();
//			Envelope envelope = new Envelope(bbox.getMinX(), bbox.getMaxX(), bbox.getMinY(), bbox.getMaxY()); 
//			
//			tree.insert(envelope, wayBuildingData);
//		}
//		tree.build();
		List<PointData<WayBuildingData>> pointsList = new ArrayList<PointData<WayBuildingData>>();
		KdTree<PointData<WayBuildingData>> kdTree = new KdTree<>();
		int i = 0;
		for (WayBuildingData wayBuildingData : typeWays) {
			i++;
			if (i % 50000 == 0) {
				System.out.println("Added " + i + " points");
			}
			PointData<WayBuildingData> pointData = new PointData<WayBuildingData>(wayBuildingData.getCenter().x(), wayBuildingData.getCenter().y(), wayBuildingData);
			kdTree.add(pointData);
			pointsList.add(pointData);
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Built tree in millis: " + (System.currentTimeMillis() - t1));
//		ItemDistance itemDistance = new EquiRectDistanceFunction();
//		for (WayBuildingData wayBuildingData : typeWays) {
//			t1 = System.currentTimeMillis();
//			Object[] neighbours = tree.nearestNeighbour(getEnvelope(wayBuildingData.getBoundingBox()),wayBuildingData,itemDistance, 3);
//			System.out.println("Search took: " + (System.currentTimeMillis() - t1));
//			i++;
//			if (i % 50000 == 0) {
//				System.out.println("Found neighbors for " + i);
//			}
//		}
		List<PointData<WayBuildingData>> classifiedPoints = pointsList.stream().filter(data -> data.getData().getType() != null).collect(Collectors.toList());
		try (CSVWithAdditionalsWriter<WayBuildingData> writer = new CSVWithAdditionalsWriter<>(new File("type_ways" + NEIGHBOUR_COUNT + ".csv"), "types",NEIGHBOUR_COUNT)) {
			for (PointData<WayBuildingData> pointData : classifiedPoints) {
				Collection<PointData<WayBuildingData>> neighbours = kdTree.nearestNeighbourSearch(NEIGHBOUR_COUNT + 1, pointData);
				neighbours.remove(pointData);
				writer.write(pointData.getData(), neighbours.stream().limit(NEIGHBOUR_COUNT).map(data -> data.getData()).collect(Collectors.toList()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("App.buildGeoindex() "+ geospatialIndex.getNearestNeighbors(new SimpleGeospatialPoint(55.01, 82.55), 3));
	}	

	protected static void buildClassifier() {
		try {
			ModelGenerator mg = new ModelGenerator();

			Instances dataset = mg.loadDataset("type_ways.arff");

			Filter filter = new Normalize();

			// divide dataset to train dataset 80% and test dataset 20%
			int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
			int testSize = dataset.numInstances() - trainSize;

			dataset.randomize(new Debug.Random(1));// if you comment this line the accuracy of the model will be droped from
													// 96.6% to 80%

			// Normalize dataset
			filter.setInputFormat(dataset);
			Instances datasetnor = Filter.useFilter(dataset, filter);

			Instances traindataset = new Instances(datasetnor, 0, trainSize);
			Instances testdataset = new Instances(datasetnor, trainSize, testSize);
			J48 m = new J48();
	        m.setBatchSize("100");
	        m.setSeed(0);
	        evaluate(traindataset, testdataset, m);
	        NaiveBayes bayes = new NaiveBayes();
	        bayes.setBatchSize("100");
	        evaluate(traindataset, testdataset, bayes);
	        LogisticBase logisticBase = new LogisticBase();
	        logisticBase.setBatchSize("100");
	        evaluate(traindataset, testdataset, logisticBase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void evaluate(Instances traindataset, Instances testdataset, AbstractClassifier classifier) throws Exception {
		System.out.println("Classifier: " + classifier.getClass().getSimpleName());
		classifier.buildClassifier(traindataset);
		Evaluation eval = new Evaluation(traindataset);
		eval.evaluateModel(classifier, testdataset);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}

}
