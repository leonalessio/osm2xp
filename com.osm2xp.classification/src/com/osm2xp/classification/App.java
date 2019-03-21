package com.osm2xp.classification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.osm2xp.classification.index.KdTree;
import com.osm2xp.classification.index.PointData;
import com.osm2xp.classification.learning.ModelGenerator;
import com.osm2xp.classification.output.ARFFWriter;
import com.osm2xp.classification.output.CSVWithAdditionalsWriter;
import com.osm2xp.classification.output.StringDelimitedWriter;
import com.osm2xp.classification.parsing.LearningDataParser;
import com.osm2xp.core.model.osm.Tag;

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
//		buildWithGeoindex(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
		buildWithGeoindex(Arrays.asList(new File("f:\\tmp\\osm\\").listFiles(file -> file.getName().endsWith(".pbf") || file.getName().endsWith(".osm"))), data -> data.getData().getHeight() > 0 && data.getData().getHeight() < 100);
//		buildWithGeoindex(Arrays.asList(new File("f:\\tmp\\osm\\").listFiles()), data -> data.getData().getType() != null);
//		buildDataset();
//		buildClassifier();
	}

	protected static void buildDataset() {
		LearningDataParser parser = new LearningDataParser(new File("F:/tmp/siberian-fed-district-latest.osm.pbf"), getBuildingPredicate());
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
	
	protected static void buildWithGeoindex(List<File> files, Predicate<? super PointData<WayBuildingData>> classifiedPredicate) {
		try (CSVWithAdditionalsWriter<WayBuildingData> writer = new CSVWithAdditionalsWriter<>(new File(getName(files.get(0)) + "_" + NEIGHBOUR_COUNT + ".csv"), "types", "height", NEIGHBOUR_COUNT)) {
			for (File curFile : files) {
				processCurFile(writer, curFile, classifiedPredicate);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("App.buildGeoindex() "+ geospatialIndex.getNearestNeighbors(new SimpleGeospatialPoint(55.01, 82.55), 3));
	}

	protected static void processCurFile(CSVWithAdditionalsWriter<WayBuildingData> writer, File curFile, Predicate<? super PointData<WayBuildingData>> classifiedPredicate) {
		System.out.println("Processing " + curFile.getAbsolutePath());
		KdTree<PointData<WayBuildingData>> kdTree = new KdTree<>();
		List<PointData<WayBuildingData>> classifiedPoints = getPointData(curFile, kdTree, classifiedPredicate);
		for (PointData<WayBuildingData> pointData : classifiedPoints) {
			Collection<PointData<WayBuildingData>> neighbours = kdTree.nearestNeighbourSearch(NEIGHBOUR_COUNT + 1, pointData);
			neighbours.remove(pointData);
			writer.write(pointData.getData(), neighbours.stream().limit(NEIGHBOUR_COUNT).map(data -> data.getData()).collect(Collectors.toList()));
		}
	}

	protected static List<PointData<WayBuildingData>> getPointData(File file,
			KdTree<PointData<WayBuildingData>> kdTree, Predicate<? super PointData<WayBuildingData>> classifiedPredicate) {
		LearningDataParser parser = new LearningDataParser(file, getBuildingPredicate());
		List<WayBuildingData> typeWays = parser.getTypeWays();
		List<PointData<WayBuildingData>> pointsList = new ArrayList<PointData<WayBuildingData>>();
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
		List<PointData<WayBuildingData>> classifiedPoints = pointsList.stream().filter(classifiedPredicate).collect(Collectors.toList());
		return classifiedPoints;
	}
	
	private static Predicate<List<Tag>> getBuildingPredicate() {
		return (tags) -> TypeProvider.isBuilding(tags);
	}

	protected static String getName(File file) {
		String name = file.getName();
		int idx = name.indexOf('.');
		if (idx > 0) {
			return name.substring(0, idx);
		}
		return name;
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
