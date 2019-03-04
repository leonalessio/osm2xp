package com.osm2xp.datastore.impl;

import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;

/**
 * Jdbm3 data sink implementation.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class MapDBProcessorImpl extends AbstractDataProcessor {
	private DB db;
	private ConcurrentMap<Long, double[]> nodesMap;
	private ConcurrentMap<Long, long[]> waysMap;
	// private RecordManager recMan;
//	private Map<Long, double[]> sink = new HashMap<Long, double[]>();

	public MapDBProcessorImpl() throws DataSinkException {
		initDB();
	}

	/**
	 * initialization of the h2 database engine
	 * 
	 * @throws DataSinkException
	 */
	private void initDB() throws DataSinkException {
//		File jdbmDirectory = new File(Osm2xpConstants.JDBM_PATH);
//		// delete jdbm objects
//		if (jdbmDirectory.exists()) {
//			FilesUtils.deleteDirectory(jdbmDirectory);
//		}
//		// create directory
//		jdbmDirectory.mkdirs();
		db = DBMaker
		        .memoryDirectDB()
		        .make();
//		db = DBMaker
//				.tempFileDB()
//				.fileMmapEnableIfSupported()		        
//				.make();
//				DBMaker.openFile(
//				Osm2xpConstants.JDBM_PATH + File.separator + "nodes").make();
		// dbMaker.disableTransactions();
		// dbMaker.disableCache();
		// dbMaker=dbMaker.closeOnExit();
		// dbMaker=dbMaker.deleteFilesAfterClose();
		// dbMaker=dbMaker.disableTransactions();
		// dbMaker=dbMaker.enableHardCache();
		// db = dbMaker.make();
		

		nodesMap = (ConcurrentMap<Long, double[]>) db.hashMap("nodes", Serializer.LONG, Serializer.DOUBLE_ARRAY).create();
		waysMap = (ConcurrentMap<Long, long[]>) db.hashMap("ways", Serializer.LONG, Serializer.LONG_ARRAY).create();

	}

	@Override
	public void storeNode(Node node) throws DataSinkException {
		double[] coords = new double[] { node.getLat(), node.getLon() };
		nodesMap.put(node.getId(), coords);
//		sink.put(node.getId(), coords);
//
//		if (sink.size() == 100000) {
//			injectNodesIntoStorage();
//
//		}
	}

//	private void injectNodesIntoStorage() {
//		nodesMap.putAll(sink);
//		db.commit();
//		sink.clear();
//		db.clearCache();
//		System.out.println("taille= " + nodesMap.size());
//
//	}

	@Override
	public Node getNode(Long id) throws DataSinkException {
//		if (!sink.isEmpty()) {
//			injectNodesIntoStorage();
//		}
		Node result = null;
		double[] node = nodesMap.get(id);
		if (node != null) {
			result = new Node(null, node[0], node[1], id);
		}
		return result;

	}

	@Override
	public void complete() {
		db.close();

	}

	@Override
	public Long getNodesNumber() {
		return Long.valueOf(nodesMap.size());
	}

	@Override
	public void storeWayPoints(long wayId, long[] pointIds) {
		waysMap.put(wayId, pointIds);
	}

	@Override
	public long[] getWayPoints(long wayId) {
		return waysMap.get(wayId);
	}

}
