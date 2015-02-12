package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    // http://stackoverflow.com/questions/12835285/create-directory-if-exists-delete-directory-and-its-content-and-create-new-one
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            File indexFile = new File("/var/lib/lucene/index-items-name-categories-description");
            try {
                deleteDir(indexFile);
            } catch (Exception e) {
                System.out.println("Unable to delete previous index directory");
            }
            Directory indexDir = FSDirectory.open(indexFile);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }

    public void rebuildIndexes() throws IOException{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
    	try {
    	    conn = DbManager.getConnection(true);
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}


    	/*
    	 * Add your code here to retrieve Items using the connection
    	 * and add corresponding entries to your Lucene inverted indexes.
             *
             * You will have to use JDBC API to retrieve MySQL data from Java.
             * Read our tutorial on JDBC if you do not know how to use JDBC.
             *
             * You will also have to use Lucene IndexWriter and Document
             * classes to create an index and populate it with Items data.
             * Read our tutorial on Lucene as well if you don't know how.
             *
             * As part of this development, you may want to add
             * new methods and create additional Java classes.
             * If you create new classes, make sure that
             * the classes become part of "edu.ucla.cs.cs144" package
             * and place your class source files at src/edu/ucla/cs/cs144/.
    	 *
    	 */

        // create our index
        getIndexWriter(true);

        int id;
        String name, keywords;

        // try to get the relevant info from MySQL and store it in the indexer
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT id, Name as name, CONCAT(Name, ' ', Category, ' ', Description) as keywords " +
                           "FROM Items as i, (SELECT ItemId, GROUP_CONCAT(Name SEPARATOR ' ') as Category FROM Categories GROUP BY ItemId) as c " +
                           "WHERE i.id = c.ItemId;";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                id = rs.getInt("id");
                name = rs.getString("name");
                keywords = rs.getString("keywords");
                //System.out.println(id + ": " + name);

                Document doc = new Document();
                doc.add(new StringField("id", Integer.toString(id), Field.Store.YES));
                doc.add(new StringField("name", name, Field.Store.YES));
                doc.add(new TextField("keywords", keywords, Field.Store.NO));
                indexWriter.addDocument(doc);
            }

            closeIndexWriter();

        } catch (SQLException ex) {
            System.out.println(ex);
        }


        // close the database connection
    	try {
    	    conn.close();
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}
    }

    public static void main(String args[]) throws IOException {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}
