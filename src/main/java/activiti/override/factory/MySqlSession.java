package activiti.override.factory;

import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.DbSqlSessionFactory;

public class MySqlSession extends DbSqlSession {

	public MySqlSession(DbSqlSessionFactory dbSqlSessionFactory) {
		super(dbSqlSessionFactory);
	}
	

//	protected void dbSchemaCreateEngine() {
//		super.dbSchemaCreateEngine();
//		executeMandatorySchemaResource("chingee", "chingee");
//	}

//	public String getResourceForDbOperation(String directory, String operation, String component) {
//		String databaseType = dbSqlSessionFactory.getDatabaseType();
//		if (operation.equals("chingee")) {
//			return "com/chingee/sqlsession/chingee/init.sql";
//		} else {
//			return "org/activiti/db/" + directory + "/activiti." + databaseType + "." + operation + "." + component
//					+ ".sql";
//		}
//	}

}
