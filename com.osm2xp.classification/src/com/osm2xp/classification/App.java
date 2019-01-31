package com.osm2xp.classification;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.eatthepath.jeospatial.SimpleGeospatialPoint;
import com.eatthepath.jeospatial.VPTreeGeospatialIndex;
import com.osm2xp.classification.learning.ModelGenerator;
import com.osm2xp.classification.output.ARFFWriter;
import com.osm2xp.classification.parsing.LearningDataParser;

import math.geom2d.Point2D;
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

	public static void main(String[] args) {
		buildGeoindex();
//		buildDataset();
//		buildClassifier();
	}

	protected static void buildDataset() {
		LearningDataParser parser = new LearningDataParser(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
		try (ARFFWriter<WayBuildingData> writer = new ARFFWriter<WayBuildingData>(new File("type_ways.arff"), "type")) {
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
		VPTreeGeospatialIndex<BuildingPoint> geospatialIndex = new VPTreeGeospatialIndex<>();
		List<WayBuildingData> typeWays = parser.getTypeWays();
		for (WayBuildingData wayBuildingData : typeWays) {
			Point2D center = wayBuildingData.getCenter();
			if (center != null) {
				geospatialIndex.add(new BuildingPoint(center.x, center.y, wayBuildingData));
			} 
		}
		System.out.println("App.buildGeoindex() "+ geospatialIndex.getAllWithinDistance(new SimpleGeospatialPoint(55.01, 82.55), 0.001));
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
