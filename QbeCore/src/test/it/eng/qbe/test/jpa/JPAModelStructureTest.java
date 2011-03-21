/**
 * 
 */
package it.eng.qbe.test.jpa;

import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADataSource;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelEntity;
import it.eng.qbe.model.structure.ModelField;
import it.eng.qbe.model.structure.ModelStructure;
import it.eng.qbe.model.structure.builder.IModelStructureBuilder;
import it.eng.qbe.model.structure.builder.jpa.JPAModelStructureBuilder;


import java.util.List;

import javax.persistence.EntityManager;

/**
 * @author giachino
 *
 */
public class JPAModelStructureTest {
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//test for jpa
		
		//create jpaDataSource (entityManager)
		
		
		JPADriver driver = new JPADriver();
		JPADataSource jpaDS = (JPADataSource)driver.getDataSource(new FileDataSourceConfiguration("TEST_JPA", null));

		
		//EntityManagerFactory emf = jpaDS.getEntityManagerFactory();
		EntityManager em = jpaDS.getEntityManager();
		IModelStructureBuilder dmb = new JPAModelStructureBuilder(jpaDS);
		//builds the jpa structure
		IModelStructure dms = dmb.build();
		
		//gets structure's informations
		List allEntities = dms.getRootEntities("TEST_JPA");
		for (int i=0; i< allEntities.size(); i++){
			ModelEntity entity = (ModelEntity)allEntities.get(i);
			System.out.println("*** Entity uniqueName: " + entity.getUniqueName());
			System.out.println("* Entity name: " + entity.getName());
			System.out.println("* Entity uniqueType: " + entity.getUniqueType());
			System.out.println("* Entity type: " + entity.getType());			
			System.out.println("* Entity path: " + entity.getPath());
			System.out.println("* Entity role: " + entity.getRole());
			
			List keyFields = entity.getKeyFields();
			for (int k=0; k< keyFields.size(); k++){
				ModelField key = (ModelField)keyFields.get(k);				
				System.out.println("*** key Unique Name: " + key.getUniqueName());
				System.out.println("* key Name: " + key.getName());
				System.out.println("* key type: " + key.getType());
				System.out.println("* key Length: " + key.getLength());
				System.out.println("* key Precision: " + key.getPrecision());
			}
			
			List fields = entity.getAllFields();
			for (int j=0; j< fields.size(); j++){
				ModelField field = (ModelField)fields.get(j);				
				System.out.println("*** Field Unique Name: " + field.getUniqueName());
				System.out.println("* Field Name: " + field.getName());
				System.out.println("* Field type: " + field.getType());
				System.out.println("* Field Length: " + field.getLength());
				System.out.println("* Field Precision: " + field.getPrecision());
			}
		}
		
		
		//close operations
		//em.clear();
		em.close();
		
	}

}
