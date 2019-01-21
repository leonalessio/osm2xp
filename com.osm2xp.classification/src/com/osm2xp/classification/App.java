package com.osm2xp.classification;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.osm2xp.classification.learning.ModelGenerator;
import com.osm2xp.classification.output.ARFFWriter;
import com.osm2xp.classification.parsing.LearningDataParser;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Debug;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class App {

	// public static void main(String[] args) {
	// LearningDataParser parser = new LearningDataParser(new
	// File("F:/tmp/siberian-fed-district-latest.osm.pbf"));
	// try (ARFFWriter<WayBuildingData> writer = new ARFFWriter<WayBuildingData>(new
	// File("type_ways.arff"), "type")) {
	// List<WayBuildingData> typeWays = parser.getTypeWays();
	// for (WayBuildingData wayBuildingData : typeWays) {
	// writer.write(wayBuildingData);
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
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
			Classifier classifier = mg.buildClassifier(traindataset);
			Evaluation eval = new Evaluation(traindataset);
			eval.evaluateModel(classifier, testdataset);
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
